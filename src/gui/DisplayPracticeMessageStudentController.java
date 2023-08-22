package gui;

import app.Practice;
import app.PracticeSystem;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author lukaskorinek
 */
public class DisplayPracticeMessageStudentController implements Initializable {

    @FXML
    private Label loggedStudent;
    @FXML
    private Label title;
    @FXML
    private TextArea message;
    @FXML
    private Hyperlink backLink;
    
    private Main application;
    private PracticeSystem practiceSystem;
    private Practice practice;
    
    
    public void setApp(Main application, PracticeSystem practiceSystem, Practice practice){
        this.application = application;
        this.practiceSystem = practiceSystem;
        this.practice = practice;
        setLoggedUserName();
        setTitleAndPracticeMessage();
    }
    
    private void setLoggedUserName(){
        loggedStudent.setText(practiceSystem.getLoggedUserFullName());
    }
    
    private void setTitleAndPracticeMessage(){
        title.setText("Písemná zpráva z praxe " + practice.getNumberAndDescription());
        message.setText(practice.getWrittenReportFromStudent());
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void goBack(ActionEvent event) {
        application.goToSubmitPracticeMessage(practiceSystem, practice);
    } 
}
