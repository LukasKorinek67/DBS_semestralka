package gui;

import app.Company;
import utils.boxes.SelectionBox;
import app.Practice;
import app.PracticeSystem;
import app.Student;
import app.StudyProgramme;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import utils.exceptions.AlreadyHasAssignedPracticeException;
import utils.boxes.ConfirmBox;
import utils.exceptions.BlankDataException;
import utils.exceptions.PracticeAlreadyAssignedException;

/**
 * FXML Controller class
 *
 * @author lukaskorinek
 */
public class DisplayPracticeCompanyController implements Initializable {

    @FXML
    private Label loggedCompany;
    @FXML
    private Label practiceNumberAndDescription;
    @FXML
    private Label assignedStudent;
    @FXML
    private ListView appliedStudentsList;
    @FXML
    private Label academicYear;
    @FXML
    private ListView forStudyProgrammesList;
    @FXML
    private Label createdDate;
    @FXML
    private Button buttonAssignStudent;
    @FXML
    private Button buttonDisplayMessage;
    @FXML
    private Button buttonEditPractice;
    @FXML
    private Label errorMessage;
    @FXML
    private Hyperlink backLink;
    
    private Main application;
    private PracticeSystem practiceSystem;
    private Practice practice;
    
    public void setApp(Main application, PracticeSystem practiceSystem, Practice practiceToDisplay) throws SQLException, ClassNotFoundException{
        this.application = application;
        this.practiceSystem = practiceSystem;
        this.practice = practiceToDisplay;
        setLoggedUserName();
        setPracticeInfo();
    }
    
    private void setLoggedUserName(){
        loggedCompany.setText(practiceSystem.getLoggedUserFullName());
    }
    
    private void setPracticeInfo() throws SQLException, ClassNotFoundException{
        practiceNumberAndDescription.setText(practice.getNumberAndDescription());
        
        String assignedStudentNumber = practice.getAssignedStudentNumber();
        if(assignedStudentNumber == null){
            assignedStudent.setText("Zatím nepřiřazena");
        }else{
            assignedStudent.setText(assignedStudentNumber + " - " + Student.getNameByNumber(assignedStudentNumber));
        }
        
        //appliedStudentsList
        ObservableList<String> students = FXCollections.observableArrayList();
        ArrayList<Student> appliedStudents = practice.getAllAppliedStudents();
        for (int i = 0; i < appliedStudents.size(); i++) {
            students.add(appliedStudents.get(i).getPersonalNumber() + " - " + appliedStudents.get(i).getFullName());
        }
        appliedStudentsList.setItems(students);
        academicYear.setText(practice.getAcademicYear());
        
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
        errorMessage.setText("");
    }   
    
    public void assignStudent(ActionEvent event) {
        try {
            ArrayList<Student> appliedStudents = practice.getAllAppliedStudents();
            if(appliedStudents.isEmpty()){
                errorMessage.setText("O tuto praxi zatím žádný student nepožádal");
            }else if(appliedStudents.size() == 1){
                boolean confirmation = ConfirmBox.display("Přiřazení studenta", "Opravdu chcete přiřadit studenta "
                + appliedStudents.get(0).getPersonalNumber() + " - " + appliedStudents.get(0).getFullName()
                + " k této praxi?");
                if(confirmation){
                    assign(appliedStudents.get(0).getPersonalNumber());
                }
            }else{    
                Student selectedStudent = SelectionBox.display(appliedStudents, "Přiřazení studenta",
                        "Vyberte studenta, kterého chcete přiřadit k praxi");
                if(selectedStudent == null){
                    throw new BlankDataException();
                }
                boolean confirmation = ConfirmBox.display("Přiřazení studenta", "Opravdu chcete přiřadit studenta "
                + selectedStudent.getPersonalNumber() + " - " + selectedStudent.getFullName()
                + " k této praxi?");
                if(confirmation){
                    assign(selectedStudent.getPersonalNumber());
                }
            }
        } catch (SQLException ex) {
            errorMessage.setText("SQLException");
        } catch (ClassNotFoundException ex) {
            errorMessage.setText("ClassNotFoundException");
        } catch (BlankDataException ex) {
            errorMessage.setText("Nevybral/a jste žádného studenta");
        }
    } 
    
    private void assign(String studentNumber){
        try {
            ((Company)practiceSystem.getLoggedUser()).assignStudentToPractice(studentNumber, this.practice, LocalDateTime.now());
            AlertBox.display("Přiřazení studenta", "Student úspěšně přiřazen");
            setPracticeInfo();
        } catch (SQLException ex) {
            errorMessage.setText("SQLException");
        } catch (PracticeAlreadyAssignedException ex) {
            errorMessage.setText("Tato praxe je již přiřazena jinému studentovi");
        } catch (AlreadyHasAssignedPracticeException ex) {
            errorMessage.setText("Tento student má již přiřazenou jinou praxi");
        } catch (ClassNotFoundException ex) {
            errorMessage.setText("ClassNotFoundException");
        }
    }
    
    public void displayMessage(ActionEvent event) {
        if(practice.getWrittenReportFromStudent() != null){
            application.goToDisplayPracticeMessageCompany(practiceSystem, practice);
        }else{
            errorMessage.setText("Žádná zpráva z této praxe zatím nebyla odevzdána");
        }
    }
    
    public void editPractice(ActionEvent event) {
        application.goToEditPractice(practiceSystem, practice);
    } 
    
    public void goBack(ActionEvent event) {
        application.goToMyPracticesCompany(practiceSystem);
    }   
}
