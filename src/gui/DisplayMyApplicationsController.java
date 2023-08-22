package gui;

import app.Practice;
import app.PracticeSystem;
import app.Student;
import app.StudyProgramme;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import utils.boxes.AlertBox;
import utils.exceptions.BlankDataException;

/**
 * FXML Controller class
 *
 * @author lukaskorinek
 */
public class DisplayMyApplicationsController implements Initializable {

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
    private ListView applicationSP1List;
    @FXML
    private ListView applicationSP2List;
    @FXML
    private ListView applicationSP3List;
    @FXML
    private ListView applicationSP4List;
    @FXML
    private ListView applicationSP5List;
    @FXML
    private Button buttonDeleteApplication1;
    @FXML
    private Button buttonDeleteApplication2;
    @FXML
    private Button buttonDeleteApplication3;
    @FXML
    private Button buttonDeleteApplication4;
    @FXML
    private Button buttonDeleteApplication5;
    @FXML
    private Label errorMessage;
    @FXML
    private Hyperlink backLink;
    
    private Main application;
    private PracticeSystem practiceSystem;
    
    public void setApp(Main application, PracticeSystem practiceSystem) throws SQLException, ClassNotFoundException{
        this.application = application;
        this.practiceSystem = practiceSystem;
        setLoggedUserName();
        setStudiedStudyProgrammesAndApplications();
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
    
    private Practice getSelectedPractice(String practiceNameAndDescription) throws SQLException, ClassNotFoundException, BlankDataException{
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
    
    private StudyProgramme getStudyProgrammeByName(String name) throws SQLException, ClassNotFoundException{
        ArrayList<StudyProgramme> studyProgrammes = practiceSystem.getStudyProgrammes();
        for (int i = 0; i < studyProgrammes.size(); i++) {
            if(studyProgrammes.get(i).getName().equals(name)){
                return studyProgrammes.get(i); 
            }
        }
        throw new SQLException();
    }
    
    private void deleteApplication(String practiceNameAndDescription, StudyProgramme studyProgramme) throws SQLException, ClassNotFoundException, BlankDataException{
        Practice practice = getSelectedPractice(practiceNameAndDescription);
        ((Student)practiceSystem.getLoggedUser()).deleteApplication(practice, studyProgramme);
        errorMessage.setText("");
        setStudiedStudyProgrammesAndApplications();
        AlertBox.display("Zrušení žádosti", "Žádost o praxi úspěšně zrušena");
    }
    
    public void deleteApplication1(ActionEvent event) {
        try {
            String practiceNameAndDescription = (String)applicationSP1List.getSelectionModel().getSelectedItem();
            deleteApplication(practiceNameAndDescription, getStudyProgrammeByName(studyProgramme1.getText()));
        } catch (SQLException ex) {
            errorMessage.setText("SQLException");
        } catch (ClassNotFoundException ex) {
            errorMessage.setText("ClassNotFoundException");
        } catch (BlankDataException ex) {
            errorMessage.setText("Není zvolená žádná žádost");
        }
    }
    
    public void deleteApplication2(ActionEvent event) {
        try {
            String practiceNameAndDescription = (String)applicationSP2List.getSelectionModel().getSelectedItem();
            deleteApplication(practiceNameAndDescription, getStudyProgrammeByName(studyProgramme2.getText()));
        } catch (SQLException ex) {
            errorMessage.setText("SQLException");
        } catch (ClassNotFoundException ex) {
            errorMessage.setText("ClassNotFoundException");
        } catch (BlankDataException ex) {
            errorMessage.setText("Není zvolená žádná žádost");
        }
    }
    
    public void deleteApplication3(ActionEvent event) {
        try {
            String practiceNameAndDescription = (String)applicationSP3List.getSelectionModel().getSelectedItem();
            deleteApplication(practiceNameAndDescription, getStudyProgrammeByName(studyProgramme3.getText()));
        } catch (SQLException ex) {
            errorMessage.setText("SQLException");
        } catch (ClassNotFoundException ex) {
            errorMessage.setText("ClassNotFoundException");
        } catch (BlankDataException ex) {
            errorMessage.setText("Není zvolená žádná žádost");
        }
    }
    
    public void deleteApplication4(ActionEvent event) {
        try {
            String practiceNameAndDescription = (String)applicationSP4List.getSelectionModel().getSelectedItem();
            deleteApplication(practiceNameAndDescription, getStudyProgrammeByName(studyProgramme4.getText()));
        } catch (SQLException ex) {
            errorMessage.setText("SQLException");
        } catch (ClassNotFoundException ex) {
            errorMessage.setText("ClassNotFoundException");
        } catch (BlankDataException ex) {
            errorMessage.setText("Není zvolená žádná žádost");
        }
    }
    
    public void deleteApplication5(ActionEvent event) {
        try {
            String practiceNameAndDescription = (String)applicationSP5List.getSelectionModel().getSelectedItem();
            deleteApplication(practiceNameAndDescription, getStudyProgrammeByName(studyProgramme5.getText()));
        } catch (SQLException ex) {
            errorMessage.setText("SQLException");
        } catch (ClassNotFoundException ex) {
            errorMessage.setText("ClassNotFoundException");
        } catch (BlankDataException ex) {
            errorMessage.setText("Není zvolená žádná žádost");
        }
    }
    
    public void goBack(ActionEvent event) {
        application.goToMyPracticesStudent(practiceSystem);
    }
    
    private void setStudiedStudyProgrammesAndApplications() throws SQLException, ClassNotFoundException{
        ArrayList<StudyProgramme> studyProgrammes = ((Student)practiceSystem.getLoggedUser()).getStudyProgrammes();
        
        if(studyProgrammes.size() > 1){
            studiedProgrammes.setText("Studované obory:");
        }
        for (int i = 0; i < 5; i++) {
            switch(i){
                case 0: {
                    if((i+1) <= studyProgrammes.size()){
                        studyProgramme1.setText(studyProgrammes.get(i).getName());
                        ObservableList<String> items1 = FXCollections.observableArrayList();
                        ArrayList<String> applications1 = ((Student)practiceSystem.getLoggedUser()).getApplicationForPractices(studyProgrammes.get(i));
                        boolean haveAnyApplication = false;
                        if(!(applications1.isEmpty())){
                            for (int j = 0; j < applications1.size(); j++) {
                                items1.add(applications1.get(j));
                            }
                            applicationSP1List.setItems(items1);
                        } else {
                            buttonDeleteApplication1.setVisible(false);
                        }
                    }else{
                        studyProgramme1.setVisible(false);
                        applicationSP1List.setVisible(false);
                        buttonDeleteApplication1.setVisible(false);
                    }
                }
                    break;
                case 1: {
                    if((i+1) <= studyProgrammes.size()){
                        studyProgramme2.setText(studyProgrammes.get(i).getName());
                        ObservableList<String> items2 = FXCollections.observableArrayList();
                        ArrayList<String> applications2 = ((Student)practiceSystem.getLoggedUser()).getApplicationForPractices(studyProgrammes.get(i));
                        boolean haveAnyApplication = false;
                        if(!(applications2.isEmpty())){
                            for (int j = 0; j < applications2.size(); j++) {
                                items2.add(applications2.get(j));
                            }
                            applicationSP2List.setItems(items2);
                        } else {
                            buttonDeleteApplication2.setVisible(false);
                        }
                    }else{
                        studyProgramme2.setVisible(false);
                        applicationSP2List.setVisible(false);
                        buttonDeleteApplication2.setVisible(false);
                    }
                }
                    break;
                case 2: {
                    if((i+1) <= studyProgrammes.size()){
                        studyProgramme3.setText(studyProgrammes.get(i).getName());
                        ObservableList<String> items3 = FXCollections.observableArrayList();
                        ArrayList<String> applications3 = ((Student)practiceSystem.getLoggedUser()).getApplicationForPractices(studyProgrammes.get(i));
                        boolean haveAnyApplication = false;
                        if(!(applications3.isEmpty())){
                            for (int j = 0; j < applications3.size(); j++) {
                                items3.add(applications3.get(j));
                            }
                            applicationSP3List.setItems(items3);
                        } else {
                            buttonDeleteApplication3.setVisible(false);
                        }
                    }else{
                        studyProgramme3.setVisible(false);
                        applicationSP3List.setVisible(false);
                        buttonDeleteApplication3.setVisible(false);
                    }
                }
                    break;
                case 3: {
                    if((i+1) <= studyProgrammes.size()){
                        studyProgramme4.setText(studyProgrammes.get(i).getName());
                        ObservableList<String> items4 = FXCollections.observableArrayList();
                        ArrayList<String> applications4 = ((Student)practiceSystem.getLoggedUser()).getApplicationForPractices(studyProgrammes.get(i));
                        boolean haveAnyApplication = false;
                        if(!(applications4.isEmpty())){
                            for (int j = 0; j < applications4.size(); j++) {
                                items4.add(applications4.get(j));
                            }
                            applicationSP4List.setItems(items4);
                        } else {
                            buttonDeleteApplication4.setVisible(false);
                        }
                    }else{
                        studyProgramme4.setVisible(false);
                        applicationSP4List.setVisible(false);
                        buttonDeleteApplication4.setVisible(false);
                    }
                }
                    break;
                case 4: {
                    if((i+1) <= studyProgrammes.size()){
                        studyProgramme5.setText(studyProgrammes.get(i).getName());
                        ObservableList<String> items5 = FXCollections.observableArrayList();
                        ArrayList<String> applications5 = ((Student)practiceSystem.getLoggedUser()).getApplicationForPractices(studyProgrammes.get(i));
                        boolean haveAnyApplication = false;
                        if(!(applications5.isEmpty())){
                            for (int j = 0; j < applications5.size(); j++) {
                                items5.add(applications5.get(j));
                            }
                            applicationSP5List.setItems(items5);
                        } else {
                            buttonDeleteApplication5.setVisible(false);
                        }
                    }else{
                        studyProgramme5.setVisible(false);
                        applicationSP5List.setVisible(false);
                        buttonDeleteApplication5.setVisible(false);
                    }
                }
                    break;
            }
        }
    }
}
