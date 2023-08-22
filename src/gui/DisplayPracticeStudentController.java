package gui;

import app.Practice;
import app.PracticeSystem;
import app.Student;
import app.StudyProgramme;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author lukaskorinek
 */
public class DisplayPracticeStudentController implements Initializable {

    @FXML
    private Label loggedStudent;
    @FXML
    private Label practiceNumberAndDescription;
    @FXML
    private Label assignedStudent;
    @FXML
    private Label academicYear;
    @FXML
    private Label company;
    @FXML
    private Label contactPerson;
    @FXML
    private ListView forStudyProgrammesList;
    @FXML
    private Label createdDate;
    @FXML
    private Hyperlink backLink;
    
    private Main application;
    private PracticeSystem practiceSystem;
    private Practice practice;
    
    private Initializable previousPage;
    
    public void setApp(Main application, PracticeSystem practiceSystem, Practice practiceToDisplay, Initializable previousPage) throws SQLException, ClassNotFoundException{
        this.application = application;
        this.practiceSystem = practiceSystem;
        this.practice = practiceToDisplay;
        this.previousPage = previousPage;
        setLoggedUserName();
        setPracticeInfo();
    }
    
    private void setLoggedUserName(){
        loggedStudent.setText(practiceSystem.getLoggedUserFullName());
    }
    
    private void setPracticeInfo() throws SQLException, ClassNotFoundException{
        practiceNumberAndDescription.setText(practice.getNumberAndDescription());
        
        String assignedStudentNumber = practice.getAssignedStudentNumber();
        if(assignedStudentNumber == null){
            assignedStudent.setText("Zatím nepřiřazena");
        }else{
            assignedStudent.setText(assignedStudentNumber + " - " + Student.getNameByNumber(assignedStudentNumber));
        }
        
        academicYear.setText(practice.getAcademicYear());
        
        String companyInfo = practice.getCompany().getName();
        company.setText(companyInfo);
        
        String contactPersonName = practice.getCompany().getContactPersonName();
        String contactPersonSurname = practice.getCompany().getContactPersonLastname();
        String contactPersonEmail = practice.getCompany().getContactPersonEmail();
        String contactPersonInfo = contactPersonName + " " + contactPersonSurname + ", " + contactPersonEmail; 
        contactPerson.setText(contactPersonInfo);
        
        ObservableList<String> items = FXCollections.observableArrayList();
        ArrayList<StudyProgramme> allowedStudyProgrammes = practice.getTypesOfAllowedStudyProgrammes();
        //ArrayList<Practice> myPractices = company.getMyPractices();
        for (int i = 0; i < allowedStudyProgrammes.size(); i++) {
            items.add(allowedStudyProgrammes.get(i).getName());
        }
        forStudyProgrammesList.setItems(items);
        
        String dateTime = practice.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        createdDate.setText(dateTime);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void goBack(ActionEvent event) {
        MyPracticesStudentController myPractices = new MyPracticesStudentController();
        //ListPracticesController listPractices = new ListPracticesController();
        if(previousPage.getClass().equals(myPractices.getClass())){
            application.goToMyPracticesStudent(practiceSystem);
        }else{
            application.goToListPractices(practiceSystem);
        }
    }
}
