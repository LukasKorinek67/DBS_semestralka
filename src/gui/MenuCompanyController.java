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
public class MenuCompanyController implements Initializable {

    @FXML
    private Label loggedCompany;
    @FXML
    private Button buttonEditProfile;
    @FXML
    private Button buttonPublishPractice;
    @FXML
    private Button buttonMyPractices;
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
        loggedCompany.setText(practiceSystem.getLoggedUserFullName());
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    
    public void editProfile(ActionEvent event) {
        application.goToEditCompanyProfile(practiceSystem);
    }
    
    public void publishPractice(ActionEvent event) {
        application.goToPublishPractice(practiceSystem);
    }
    
    public void displayMyPractices(ActionEvent event) {
        application.goToMyPracticesCompany(practiceSystem);
    }
    
    public void logout(ActionEvent event) {
        boolean confirmation = ConfirmBox.display("Odhlášení", "Opravdu se chcete odhlásit?");
        if(confirmation){
            practiceSystem.logoutUser();
            application.goToLogin(practiceSystem);
        }
    }
}
