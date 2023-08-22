package gui;

import utils.boxes.AlertBox;
import app.PracticeSystem;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utils.exceptions.BlankDataException;
import utils.exceptions.InvalidDataLengthException;
import utils.exceptions.InvalidEmailAddressException;
import utils.exceptions.PasswordTooShortException;

/**
 * FXML Controller class
 *
 * @author lukaskorinek
 */
public class CreateAccCompanyController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private ChoiceBox state;
    @FXML
    private TextField nameContactPerson;
    @FXML
    private TextField surnameContactPerson;
    @FXML
    private TextField emailContactPerson;
    @FXML
    private Label errorMessage;
    @FXML
    private Button createAccButton;
    @FXML
    private Hyperlink backLink;
    
    private Main application;
    private PracticeSystem practiceSystem;
    private ArrayList<String> allStates = new ArrayList<>();
    
    public void setApp(Main application, PracticeSystem practiceSystem){
        this.application = application;
        this.practiceSystem = practiceSystem;
        addAllStates();
    }
    
    private void addAllStates(){
        String[] countryCodes = Locale.getISOCountries();
        for (String countryCode : countryCodes) {
            Locale locale = new Locale("", countryCode);
            if(!(locale.getDisplayCountry().equalsIgnoreCase("Česká republika"))){
                allStates.add(locale.getDisplayCountry());
            }
        }
        Collections.sort(allStates);
        allStates.add(0, "Česká Republika");
        state.getItems().addAll(allStates);
        state.setValue("Česká Republika");
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMessage.setText("");
    }    
    
    public void createCompanyAcc(ActionEvent event) {
        try{
            practiceSystem.createCompanyAccount(username.getText(), password.getText(), 
                    name.getText(), address.getText(), (String)state.getValue(), 
                    nameContactPerson.getText(), surnameContactPerson.getText(), 
                    emailContactPerson.getText());
            AlertBox.display("Nová firma", "Nový účet úspěšně vytvořen!");
            application.goToMenuCompany(practiceSystem);
        } catch (SQLException ex) {
            errorMessage.setText("Zadané přihlašovací jméno již někdo používá");
        } catch (ClassNotFoundException ex) {
            errorMessage.setText("ClassNotFoundException");
        } catch (BlankDataException ex) {
            errorMessage.setText("Nejsou vyplněné všechny údaje");
        } catch (InvalidDataLengthException ex) {
            errorMessage.setText("Některý z údajů je příliš dlouhý");
        } catch (PasswordTooShortException ex) {
            errorMessage.setText("Heslo příliš krátké - musí obsahovat 6 - 20 znaků.");
        } catch (InvalidEmailAddressException ex) {
            errorMessage.setText("Neplatná e-mailová adresa");
        }
    }
    
    public void goBack(ActionEvent event) {
        application.goToLogin(practiceSystem);
    }  
}
