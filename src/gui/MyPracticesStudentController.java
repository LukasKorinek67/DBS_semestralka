package gui;

import app.Practice;
import app.PracticeSystem;
import app.Student;
import app.StudyProgramme;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import utils.exceptions.BlankDataException;

/**
 * FXML Controller class
 *
 * @author lukaskorinek
 */
public class MyPracticesStudentController implements Initializable {

    @FXML
    private Label loggedStudent;
    @FXML
    private Label studiedProgrammes;
    @FXML
    private Label studyProgramme1;
    @FXML
    private Label studyProgramme2;
    @FXML
    private Label studyProgramme3;
    @FXML
    private Label studyProgramme4;
    @FXML
    private Label studyProgramme5;
    @FXML
    private Label practice1;
    @FXML
    private Label practice2;
    @FXML
    private Label practice3;
    @FXML
    private Label practice4;
    @FXML
    private Label practice5;
    @FXML
    private Button buttonInfoPractice1;
    @FXML
    private Button buttonInfoPractice2;
    @FXML
    private Button buttonInfoPractice3;
    @FXML
    private Button buttonInfoPractice4;
    @FXML
    private Button buttonInfoPractice5;
    @FXML
    private Button buttonSubmitMessagePractice1;
    @FXML
    private Button buttonSubmitMessagePractice2;
    @FXML
    private Button buttonSubmitMessagePractice3;
    @FXML
    private Button buttonSubmitMessagePractice4;
    @FXML
    private Button buttonSubmitMessagePractice5;
    @FXML
    private Button buttonDisplayApplications;
    @FXML
    private Label errorMessage;
    @FXML
    private Hyperlink backLink;
    
    private Main application;
    private PracticeSystem practiceSystem;
    
    public void setApp(Main application, PracticeSystem practiceSystem){
        this.application = application;
        this.practiceSystem = practiceSystem;
        setLoggedUserName();
        setStudiedStudyProgrammesAndPractices();
    }
    
    private void setLoggedUserName(){
        loggedStudent.setText(practiceSystem.getLoggedUserFullName());
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMessage.setText("");
    } 
    
    private void displayPractice(Practice practice){
        application.goToDisplayPracticeStudent(practiceSystem, practice, this);
    }
    
    public Practice getSelectedPractice(String practiceNameAndDescription) throws SQLException, ClassNotFoundException, BlankDataException{
        Practice practice = null;
        ArrayList<Practice> allPractices = Practice.getAllPractices();
        for (int i = 0; i < allPractices.size(); i++) {
            if(allPractices.get(i).getNumberAndDescription().equals(practiceNameAndDescription)){
                practice = allPractices.get(i);
            }
        }
        if(practice != null){
            return practice;
        }else{
            throw new BlankDataException();
        }
    }
    
    public void displayPractice1(ActionEvent event) throws SQLException, ClassNotFoundException, BlankDataException {
        displayPractice(getSelectedPractice(practice1.getText()));
    }
    
    public void displayPractice2(ActionEvent event) throws SQLException, ClassNotFoundException, BlankDataException {
        displayPractice(getSelectedPractice(practice2.getText()));
    }
    
    public void displayPractice3(ActionEvent event) throws SQLException, ClassNotFoundException, BlankDataException {
        displayPractice(getSelectedPractice(practice3.getText()));
    }
    
    public void displayPractice4(ActionEvent event) throws SQLException, ClassNotFoundException, BlankDataException {
        displayPractice(getSelectedPractice(practice4.getText()));
    }
    
    public void displayPractice5(ActionEvent event) throws SQLException, ClassNotFoundException, BlankDataException {
        displayPractice(getSelectedPractice(practice5.getText()));
    }
    
    private void submitMessage(Practice practice){
        application.goToSubmitPracticeMessage(practiceSystem, practice);
    }
    
    public void submitMessagePractice1(ActionEvent event) throws SQLException, ClassNotFoundException, BlankDataException {
        submitMessage(getSelectedPractice(practice1.getText()));
    }
    
    public void submitMessagePractice2(ActionEvent event) throws SQLException, ClassNotFoundException, BlankDataException {
        submitMessage(getSelectedPractice(practice2.getText()));
    }
    
    public void submitMessagePractice3(ActionEvent event) throws SQLException, ClassNotFoundException, BlankDataException {
        submitMessage(getSelectedPractice(practice3.getText()));
    }
    
    public void submitMessagePractice4(ActionEvent event) throws SQLException, ClassNotFoundException, BlankDataException {
        submitMessage(getSelectedPractice(practice4.getText()));
    }
    
    public void submitMessagePractice5(ActionEvent event) throws SQLException, ClassNotFoundException, BlankDataException {
        submitMessage(getSelectedPractice(practice5.getText()));
    }
    
    public void displayMyApplications(ActionEvent event) {
        application.goToDisplayMyApplications(practiceSystem);
    }
    
    public void goBack(ActionEvent event) {
        application.goToMenuStudent(practiceSystem);
    }
    
    private void setStudiedStudyProgrammesAndPractices(){
        ArrayList<StudyProgramme> studyProgrammes = ((Student)practiceSystem.getLoggedUser()).getStudyProgrammes();
        ArrayList<Practice> studentPractices = ((Student)practiceSystem.getLoggedUser()).getAcceptedPractices();
        if(studyProgrammes.size() > 1){
            studiedProgrammes.setText("Studované obory:");
        }
        for (int i = 0; i < 5; i++) {
            switch(i){
                case 0: {
                    if((i+1) <= studyProgrammes.size()){
                        studyProgramme1.setText(studyProgrammes.get(i).getName());
                        boolean haveAssignedPractice = false;
                        if(studentPractices != null){
                            for (int j = 0; j < studentPractices.size(); j++) {
                                if(studyProgramme1.getText().equalsIgnoreCase(studentPractices.get(j).getAssignedStudentStudyProgramme().getName())){
                                    practice1.setText(studentPractices.get(j).getNumberAndDescription());
                                    haveAssignedPractice = true;
                                }
                            }
                        }
                        if(!haveAssignedPractice){
                            practice1.setText("Zatím žádná přiřazená praxe");
                            buttonInfoPractice1.setVisible(false);
                            buttonSubmitMessagePractice1.setVisible(false);
                        }
                    }else{
                        studyProgramme1.setVisible(false);
                        practice1.setVisible(false);
                        buttonInfoPractice1.setVisible(false);
                        buttonSubmitMessagePractice1.setVisible(false);
                    }
                }
                    break;
                case 1: {
                    if((i+1) <= studyProgrammes.size()){
                        studyProgramme2.setText(studyProgrammes.get(i).getName());
                        boolean haveAssignedPractice = false;
                        if(studentPractices != null){
                            for (int j = 0; j < studentPractices.size(); j++) {
                                if(studyProgramme2.getText().equalsIgnoreCase(studentPractices.get(j).getAssignedStudentStudyProgramme().getName())){
                                    practice2.setText(studentPractices.get(j).getNumberAndDescription());
                                    haveAssignedPractice = true;
                                }
                            }
                        }
                        if(!haveAssignedPractice){
                            practice2.setText("Zatím žádná přiřazená praxe");
                            buttonInfoPractice2.setVisible(false);
                            buttonSubmitMessagePractice2.setVisible(false);
                        }
                    }else{
                        studyProgramme2.setVisible(false);
                        practice2.setVisible(false);
                        buttonInfoPractice2.setVisible(false);
                        buttonSubmitMessagePractice2.setVisible(false);
                    }
                }
                    break;
                case 2: {
                    if((i+1) <= studyProgrammes.size()){
                        studyProgramme3.setText(studyProgrammes.get(i).getName());
                        boolean haveAssignedPractice = false;
                        if(studentPractices != null){
                            for (int j = 0; j < studentPractices.size(); j++) {
                                if(studyProgramme3.getText().equalsIgnoreCase(studentPractices.get(j).getAssignedStudentStudyProgramme().getName())){
                                    practice3.setText(studentPractices.get(j).getNumberAndDescription());
                                    haveAssignedPractice = true;
                                }
                            }
                        }
                        if(!haveAssignedPractice){
                            practice3.setText("Zatím žádná přiřazená praxe");
                            buttonInfoPractice3.setVisible(false);
                            buttonSubmitMessagePractice3.setVisible(false);
                        }
                    }else{
                        studyProgramme3.setVisible(false);
                        practice3.setVisible(false);
                        buttonInfoPractice3.setVisible(false);
                        buttonSubmitMessagePractice3.setVisible(false);
                    }
                }
                    break;
                case 3: {
                    if((i+1) <= studyProgrammes.size()){
                        studyProgramme4.setText(studyProgrammes.get(i).getName());
                        boolean haveAssignedPractice = false;
                        if(studentPractices != null){
                            for (int j = 0; j < studentPractices.size(); j++) {
                                if(studyProgramme4.getText().equalsIgnoreCase(studentPractices.get(j).getAssignedStudentStudyProgramme().getName())){
                                    practice4.setText(studentPractices.get(j).getNumberAndDescription());
                                    haveAssignedPractice = true;
                                }
                            }
                        }
                        if(!haveAssignedPractice){
                            practice4.setText("Zatím žádná přiřazená praxe");
                            buttonInfoPractice4.setVisible(false);
                            buttonSubmitMessagePractice4.setVisible(false);
                        }
                    }else{
                        studyProgramme4.setVisible(false);
                        practice4.setVisible(false);
                        buttonInfoPractice4.setVisible(false);
                        buttonSubmitMessagePractice4.setVisible(false);
                    }
                }
                    break;
                case 4: {
                    if((i+1) <= studyProgrammes.size()){
                        studyProgramme5.setText(studyProgrammes.get(i).getName());
                        boolean haveAssignedPractice = false;
                        if(studentPractices != null){
                            for (int j = 0; j < studentPractices.size(); j++) {
                                if(studyProgramme5.getText().equalsIgnoreCase(studentPractices.get(j).getAssignedStudentStudyProgramme().getName())){
                                    practice5.setText(studentPractices.get(j).getNumberAndDescription());
                                    haveAssignedPractice = true;
                                }
                            }
                        }
                        if(!haveAssignedPractice){
                            practice5.setText("Zatím žádná přiřazená praxe");
                            buttonInfoPractice5.setVisible(false);
                            buttonSubmitMessagePractice5.setVisible(false);
                        }
                    }else{
                        studyProgramme5.setVisible(false);
                        practice5.setVisible(false);
                        buttonInfoPractice5.setVisible(false);
                        buttonSubmitMessagePractice5.setVisible(false);
                    }
                }
                    break;
            }
        }
    }
}
