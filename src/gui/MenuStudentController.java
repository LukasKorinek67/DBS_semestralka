package gui;

import utils.boxes.ConfirmBox;
import app.PracticeSystem;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author lukaskorinek
 */
public class MenuStudentController implements Initializable {

    @FXML
    private Label loggedStudent;
    @FXML
    private Button buttonEditProfile;
    @FXML
    private Button buttonMyPractices;
    @FXML
    private Button buttonListPractices;
    @FXML
    private Hyperlink logoutLink;
    
    private Main application;
    private PracticeSystem practiceSystem;
    
    public void setApp(Main application, PracticeSystem practiceSystem){
        this.application = application;
        this.practiceSystem = practiceSystem;
        setLoggedUserName();
    }
    
    private void setLoggedUserName(){
        loggedStudent.setText(practiceSystem.getLoggedUserFullName());
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void editProfile(ActionEvent event) {
        application.goToEditStudentProfile(practiceSystem);
    }
    
    public void displayMyPractices(ActionEvent event) {
        application.goToMyPracticesStudent(practiceSystem);
    }
    
    public void listPractices(ActionEvent event) {
        application.goToListPractices(practiceSystem);
    }
    
    public void logout(ActionEvent event) {
        boolean confirmation = ConfirmBox.display("Odhlášení", "Opravdu se chcete odhlásit?");
        if(confirmation){
            practiceSystem.logoutUser();
            application.goToLogin(practiceSystem);
        }
    }
}
