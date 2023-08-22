package gui;

import utils.boxes.SelectionBox;
import utils.boxes.ConfirmBox;
import utils.boxes.AlertBox;
import app.Practice;
import app.PracticeSystem;
import app.Student;
import app.StudyProgramme;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
import utils.PracticesFilter;
import utils.boxes.PracticesFilterBox;
import utils.exceptions.BlankDataException;
import utils.exceptions.InvalidCompanyCountryChoiceException;
import utils.exceptions.PracticeAlreadyAssignedException;
import utils.exceptions.PracticeIsNotForThisStudyProgrammeException;
import utils.exceptions.TooManyApplicationsException;
import utils.exceptions.WrongStudyProgrammeException;

/**
 * FXML Controller class
 *
 * @author lukaskorinek
 */
public class ListPracticesController implements Initializable {

    @FXML
    private Label loggedStudent;
    @FXML
    private Button buttonFilterPractices;
    @FXML
    private Hyperlink cancelFilterLink;
    @FXML
    private ListView practicesList;
    @FXML
    private Button buttonDisplayPractice;
    @FXML
    private Button buttonApplyForPractice;
    @FXML
    private Label errorMessage;
    @FXML
    private Hyperlink backLink;
    
    private Main application;
    private PracticeSystem practiceSystem;
    
    public void setApp(Main application, PracticeSystem practiceSystem) throws SQLException, ClassNotFoundException {
        this.application = application;
        this.practiceSystem = practiceSystem;
        setLoggedUserName();
        displayAllPractices();
    }
    
    private void setLoggedUserName(){
        loggedStudent.setText(practiceSystem.getLoggedUserFullName());
    }
    
    private void displayAllPractices() throws SQLException, ClassNotFoundException {
        displayPractices(Practice.getAllPractices());
    }
    
    private void displayPractices(ArrayList<Practice> practices){
        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 0; i < practices.size(); i++) {
            String practice = practices.get(i).getNumberAndDescription();
            /*if(allPractices.get(i).getAssignedStudentNumber() == null){
                practice += " - zatím volná";
            }else{
                practice += " - již přidělena";
            }*/
            items.add(practice);
        }
        if(items.isEmpty()){
            errorMessage.setText("Žádna praxe nenalezena");
        } else {
            errorMessage.setText("");
        }
        practicesList.setItems(items);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMessage.setText("");
    }  
    
    public void filterPractices(ActionEvent event) throws SQLException, ClassNotFoundException {
        PracticesFilter filter = PracticesFilterBox.display("Filtrovat", "Vyberte prosím filtr");
        displayPractices(((Student)practiceSystem.getLoggedUser()).viewFilteredPractices(filter));
    }
    
    public void cancelFilter(ActionEvent event) throws SQLException, ClassNotFoundException {
        displayAllPractices();
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
    
    public void displaySelectedPractice(ActionEvent event) {
        String practiceNameAndDescription = (String)practicesList.getSelectionModel().getSelectedItem();
        try{
            displayPractice(getSelectedPractice(practiceNameAndDescription));
        } catch (SQLException ex) {
            errorMessage.setText("SQLException");
        } catch (ClassNotFoundException ex) {
            errorMessage.setText("ClassNotFoundException");
        } catch (BlankDataException ex) {
            errorMessage.setText("Není zvolena žádná praxe");
        }
    }
    
    private void displayPractice(Practice practice){
        application.goToDisplayPracticeStudent(practiceSystem, practice, this);
    }
    
    public void applyForSelectedPractice(ActionEvent event) {
        try{    
            String practiceNameAndDescription = (String)practicesList.getSelectionModel().getSelectedItem();
            Practice practice = getSelectedPractice(practiceNameAndDescription);
            String confirmText = "Opravdu chcete zažádat o praxi \"" + practiceNameAndDescription + "\"?";
            boolean confirmation = ConfirmBox.display("Zažádání o praxi", confirmText);
            if(confirmation){
                ArrayList<StudyProgramme> studyProgrammes = ((Student)practiceSystem.getLoggedUser()).getStudyProgrammes();
                ArrayList<Practice> acceptedPractices = ((Student)practiceSystem.getLoggedUser()).getAcceptedPractices();
                if(!(acceptedPractices.isEmpty()) || studyProgrammes.size() == acceptedPractices.size()){
                    errorMessage.setText("Ke každému z vašich studovaných oborů je již přiřazená praxe");
                }else{
                    if(studyProgrammes.size() == 1){
                        apply(practice, studyProgrammes.get(0));
                    }else{
                        StudyProgramme selectedStudyProgramme = selectStudyProgramme(studyProgrammes);
                        apply(practice, selectedStudyProgramme);
                    }
                }
            }
        } catch (BlankDataException ex) {
            errorMessage.setText("Není zvolena žádná praxe");
        } catch (SQLException ex) {
            errorMessage.setText("SQLException");
        } catch (ClassNotFoundException ex) {
            errorMessage.setText("ClassNotFoundException");
        } catch (WrongStudyProgrammeException ex) {
            errorMessage.setText("Nezvolil/a jste žádný obor");
        }
    }
    
    private void apply(Practice practice, StudyProgramme studyProgramme){
        try{
            ((Student)practiceSystem.getLoggedUser()).applyForPractice(practice, studyProgramme, LocalDateTime.now());
            errorMessage.setText("");
            AlertBox.display("Žádost", "Žádost o praxi úspěšně podána");
        } catch (SQLException ex) {
            errorMessage.setText("O tuto praxi jste již zažádal");
        } catch (TooManyApplicationsException ex) {
            errorMessage.setText("Můžete zažádat maximálně o 3 praxe ke každému z vašich studovaných oborů v jednom roce");
        } catch (PracticeIsNotForThisStudyProgrammeException ex) {
            errorMessage.setText("Praxe není vypsána na váš studijní program");
        } catch (PracticeAlreadyAssignedException ex) {
            errorMessage.setText("Tato praxe už byla přiřazena některému studentovi");
        } catch (WrongStudyProgrammeException ex) {
            errorMessage.setText("Student nestuduje zvolený obor");
        } catch (InvalidCompanyCountryChoiceException ex) {
            errorMessage.setText("Studenti bakalářského studia musí absolvovat praxi v české firmě a studenti navazujícího studia musí absolvovat praxi v zahraniční firmě");
        } catch (ClassNotFoundException ex) {
            errorMessage.setText("ClassNotFoundException");
        }
    }
    
    private StudyProgramme selectStudyProgramme(ArrayList<StudyProgramme> studyProgrammes) throws WrongStudyProgrammeException {
        StudyProgramme selectedStudyProgramme;
        selectedStudyProgramme = SelectionBox.display("Výběr studijního oboru", 
                "Vyberte jeden z Vašich studovaných oborů, ke kterému chcete přiřadit žádost o praxi", studyProgrammes);
        if(selectedStudyProgramme == null){
           throw new WrongStudyProgrammeException(); 
        }
        return selectedStudyProgramme;
    }
    
    public void goBack(ActionEvent event) {
        application.goToMenuStudent(practiceSystem);
    }    
}