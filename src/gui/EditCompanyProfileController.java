package gui;

import utils.boxes.AlertBox;
import app.Company;
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
public class EditCompanyProfileController implements Initializable {

    @FXML
    private Label loggedCompany;
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
    private Button usernameButton;
    @FXML
    private Button passwordButton;
    @FXML
    private Button nameButton;
    @FXML
    private Button addressButton;
    @FXML
    private Button stateButton;
    @FXML
    private Button nameContactPersonButton;
    @FXML
    private Button surnameContactPersonButton;
    @FXML
    private Button emailContactPersonButton;
    @FXML
    private Label errorMessage;
    @FXML
    private Hyperlink backLink;
    
    private Main application;
    private PracticeSystem practiceSystem;
    private ArrayList<String> allStates = new ArrayList<>();
    
    public void setApp(Main application, PracticeSystem practiceSystem){
        this.application = application;
        this.practiceSystem = practiceSystem;
        setLoggedUserName();
        addAllStates();
        setLoggedUserDetails();
    }
    
    private void setLoggedUserName(){
        loggedCompany.setText(practiceSystem.getLoggedUserFullName());
    }
    
    private void setLoggedUserDetails(){
        Company company = (Company)practiceSystem.getLoggedUser();
        
        username.setText(company.getUsername());
        password.setText(company.getPassword());
        name.setText(company.getName());
        address.setText(company.getAddress());
        state.setValue(company.getState());
        nameContactPerson.setText(company.getContactPersonName());
        surnameContactPerson.setText(company.getContactPersonLastname());
        emailContactPerson.setText(company.getContactPersonEmail());
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
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMessage.setText("");
    }    
    
    private void editDetails(String username, String password, String companyName, String address, String state, String contactPersonName, String contactPersonLastName, String contactPersonEmail){
        try {
            ((Company)practiceSystem.getLoggedUser()).editProfile(username, password, companyName, address, state, contactPersonName, contactPersonLastName, contactPersonEmail);
            setLoggedUserDetails();
            AlertBox.display("Profil", "Úspěšně změněno!");
        } catch (SQLException ex) {
            errorMessage.setText("Zadané přihlašovací jméno již někdo používá");
        } catch (BlankDataException ex) {
            errorMessage.setText("Není vyplněný údaj, který chcete změnit");
        } catch (InvalidDataLengthException ex) {
            errorMessage.setText("Nový údaj je příliš dlouhý");
        } catch (PasswordTooShortException ex) {
            errorMessage.setText("Heslo příliš krátké - musí obsahovat 6 - 20 znaků.");
        } catch (InvalidEmailAddressException ex) {
            errorMessage.setText("Neplatná e-mailová adresa");
        }  
    }
    
    public void changeUsername(ActionEvent event) {
        Company company = (Company)practiceSystem.getLoggedUser();
        editDetails(username.getText(), company.getPassword(),
                    company.getName(), company.getAddress(), company.getState(), company.getContactPersonName(),
                    company.getContactPersonLastname(), company.getContactPersonEmail());
    }
    
    public void changePassword(ActionEvent event) {
        Company company = (Company)practiceSystem.getLoggedUser();
        editDetails(company.getUsername(), password.getText(),
                    company.getName(), company.getAddress(), company.getState(), company.getContactPersonName(),
                    company.getContactPersonLastname(), company.getContactPersonEmail());
    }
    
    public void changeName(ActionEvent event) {
        Company company = (Company)practiceSystem.getLoggedUser();
        editDetails(company.getUsername(), company.getPassword(),
                    name.getText(), company.getAddress(), company.getState(), company.getContactPersonName(),
                    company.getContactPersonLastname(), company.getContactPersonEmail());
    }
    
    public void changeAddress(ActionEvent event) {
        Company company = (Company)practiceSystem.getLoggedUser();
        editDetails(company.getUsername(), company.getPassword(),
                    company.getName(), address.getText(), company.getState(), company.getContactPersonName(),
                    company.getContactPersonLastname(), company.getContactPersonEmail());
    }
    
    public void changeState(ActionEvent event) {
        Company company = (Company)practiceSystem.getLoggedUser();
        editDetails(company.getUsername(), company.getPassword(),
                    company.getName(), company.getAddress(), (String)state.getValue(), company.getContactPersonName(),
                    company.getContactPersonLastname(), company.getContactPersonEmail());
    }
    
    public void changeNameContactPerson(ActionEvent event) {
        Company company = (Company)practiceSystem.getLoggedUser();
        editDetails(company.getUsername(), company.getPassword(),
                    company.getName(), company.getAddress(), company.getState(), nameContactPerson.getText(),
                    company.getContactPersonLastname(), company.getContactPersonEmail());
    }
    
    public void changeSurnameContactPerson(ActionEvent event) {
        Company company = (Company)practiceSystem.getLoggedUser();
        editDetails(company.getUsername(), company.getPassword(),
                    company.getName(), company.getAddress(), company.getState(), company.getContactPersonName(),
                    surnameContactPerson.getText(), company.getContactPersonEmail());
    }
    
    public void changeEmailContactPerson(ActionEvent event) {
        Company company = (Company)practiceSystem.getLoggedUser();
        editDetails(company.getUsername(), company.getPassword(),
                    company.getName(), company.getAddress(), company.getState(), company.getContactPersonName(),
                    company.getContactPersonLastname(), emailContactPerson.getText());
    }
    
    public void goBack(ActionEvent event) {
        application.goToMenuCompany(practiceSystem);
    }
}
