package gui;

import app.PracticeSystem;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import utils.exceptions.UserDoesntExistException;
import utils.exceptions.WrongPasswordException;

/**
 * FXML Controller class
 *
 * @author lukaskorinek
 */
public class LoginController extends AnchorPane implements Initializable {

    @FXML
    private VBox studentSide;
    @FXML
    private TextField usernameStudent;
    @FXML
    private PasswordField passwordStudent;
    @FXML
    private Label errorMessageStudent;
    @FXML
    private Button buttonLoginStudent;
    @FXML
    private Button buttonCreatAccStudent;
    @FXML
    private VBox companySide;
    @FXML
    private TextField usernameCompany;
    @FXML
    private PasswordField passwordCompany;
    @FXML
    private Label errorMessageCompany;
    @FXML
    private Button buttonLoginCompany;
    @FXML
    private Button buttonCreatAccCompany;
    
    private Main application;
    private PracticeSystem practiceSystem;
    
    public void setApp(Main application, PracticeSystem practiceSystem){
        this.application = application;
        this.practiceSystem = practiceSystem;
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMessageStudent.setText("");
        errorMessageCompany.setText("");
    }    
    
    public void loginStudent(ActionEvent event) {
        try {
            practiceSystem.loginStudent(usernameStudent.getText(), passwordStudent.getText());
            application.goToMenuStudent(practiceSystem);
        } catch (UserDoesntExistException ex) {
            errorMessageStudent.setText("Špatné přihlašovací jméno");
        } catch (WrongPasswordException ex) {
            errorMessageStudent.setText("Špatné heslo");
        } catch (SQLException ex) {
            errorMessageStudent.setText("SQLException - špatné spojení s databází (funguje jen na školní IP adrese)");
        } catch (ClassNotFoundException ex) {
            errorMessageStudent.setText("ClassNotFoundException");
        }
    }
    
    public void loginCompany(ActionEvent event) {
        try {
            practiceSystem.loginCompany(usernameCompany.getText(), passwordCompany.getText());
            application.goToMenuCompany(practiceSystem);
        } catch (UserDoesntExistException ex) {
            errorMessageCompany.setText("Špatné přihlašovací jméno");
        } catch (WrongPasswordException ex) {
            errorMessageCompany.setText("Špatné heslo");
        } catch (SQLException ex) {
            errorMessageStudent.setText("SQLException - špatné spojení s databází (funguje jen na školní IP adrese)");
        } catch (ClassNotFoundException ex) {
            errorMessageCompany.setText("ClassNotFoundException");
        }
    }
    
    public void createStudentAcc(ActionEvent event) {
        application.goToCreateAccStudent(practiceSystem);
    }
    
    public void createCompanyAcc(ActionEvent event) {
        application.goToCreateAccCompany(practiceSystem);
    }   
}