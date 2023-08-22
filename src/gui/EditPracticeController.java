package gui;

import app.Company;
import app.Practice;
import app.PracticeSystem;
import app.StudyProgramme;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import utils.boxes.AlertBox;
import utils.exceptions.BlankDataException;
import utils.exceptions.InvalidStudyProgrammesCombinationException;

/**
 * FXML Controller class
 *
 * @author lukaskorinek
 */
public class EditPracticeController implements Initializable {

    @FXML
    private Label loggedCompany;
    @FXML
    private Label title;
    @FXML
    private ChoiceBox academicYear;
    @FXML
    private TextArea description;
    @FXML
    private CheckBox studyProgramme1;
    @FXML
    private CheckBox studyProgramme2;
    @FXML
    private CheckBox studyProgramme3;
    @FXML
    private CheckBox studyProgramme4;
    @FXML
    private CheckBox studyProgramme5;
    @FXML
    private CheckBox studyProgramme6;
    @FXML
    private CheckBox studyProgramme7;
    @FXML
    private CheckBox studyProgramme8;
    @FXML
    private CheckBox studyProgramme9;
    @FXML
    private CheckBox studyProgramme10;
    @FXML
    private Button buttonChangeAcademicYear;
    @FXML
    private Button buttonChangeDescription;
    @FXML
    private Button buttonChangeStudyProgrammes;
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
        setCheckBoxesNames();
        setPracticeInfo();
    }
    
    private void setLoggedUserName(){
        loggedCompany.setText(practiceSystem.getLoggedUserFullName());
    }
    
    private void setCheckBoxesNames() throws SQLException, ClassNotFoundException{
        ArrayList<StudyProgramme> studyProgrammes = practiceSystem.getStudyProgrammes();
        // seřazení
        Collections.sort(studyProgrammes, new Comparator<StudyProgramme>() {
            @Override
            public int compare (StudyProgramme t, StudyProgramme t1) {
                return t.getName().compareTo(t1.getName());
            }
        });
        for (int i = 0; i < studyProgrammes.size(); i++) { 		      
            switch(i){
                case 0: studyProgramme1.setText(studyProgrammes.get(i).getName());
                    break;
                case 1: studyProgramme2.setText(studyProgrammes.get(i).getName());
                    break;
                case 2: studyProgramme3.setText(studyProgrammes.get(i).getName());
                    break;
                case 3: studyProgramme4.setText(studyProgrammes.get(i).getName());
                    break;
                case 4: studyProgramme5.setText(studyProgrammes.get(i).getName());
                    break;
                case 5: studyProgramme6.setText(studyProgrammes.get(i).getName());
                    break;
                case 6: studyProgramme7.setText(studyProgrammes.get(i).getName());
                    break;
                case 7: studyProgramme8.setText(studyProgrammes.get(i).getName());
                    break;
                case 8: studyProgramme9.setText(studyProgrammes.get(i).getName());
                    break;
                case 9: studyProgramme10.setText(studyProgrammes.get(i).getName());
                    break;
            }
        }   
    }
    
    private void setPracticeInfo(){
        title.setText("Úprava praxe " + practice.getNumberAndDescription());
        academicYear.getItems().add("2019/2020");
        academicYear.getItems().add("2020/2021");
        academicYear.getItems().add("2021/2022");
        academicYear.getItems().add("2022/2023");
        academicYear.getItems().add("2023/2024");
        academicYear.getItems().add("2024/2025");
        academicYear.getItems().add("2025/2026");
        academicYear.getItems().add("2026/2027");
        academicYear.getItems().add("2027/2028");
        academicYear.getItems().add("2028/2029");
        academicYear.setValue(practice.getAcademicYear());
        description.setText(practice.getDescription());
        ArrayList<StudyProgramme> studyProgrammes = practice.getTypesOfAllowedStudyProgrammes();
        studyProgramme1.setSelected(false);
        studyProgramme2.setSelected(false);
        studyProgramme3.setSelected(false);
        studyProgramme4.setSelected(false);
        studyProgramme5.setSelected(false);
        studyProgramme6.setSelected(false);
        studyProgramme7.setSelected(false);
        studyProgramme8.setSelected(false);
        studyProgramme9.setSelected(false);
        studyProgramme10.setSelected(false);
        for (int i = 0; i < studyProgrammes.size(); i++) {
            if(studyProgrammes.get(i).getName().equals(studyProgramme1.getText())){
                studyProgramme1.setSelected(true);
            } else if(studyProgrammes.get(i).getName().equals(studyProgramme2.getText())){
                studyProgramme2.setSelected(true);
            } else if(studyProgrammes.get(i).getName().equals(studyProgramme3.getText())){
                studyProgramme3.setSelected(true);
            } else if(studyProgrammes.get(i).getName().equals(studyProgramme4.getText())){
                studyProgramme4.setSelected(true);
            } else if(studyProgrammes.get(i).getName().equals(studyProgramme5.getText())){
                studyProgramme5.setSelected(true);
            } else if(studyProgrammes.get(i).getName().equals(studyProgramme6.getText())){
                studyProgramme6.setSelected(true);
            } else if(studyProgrammes.get(i).getName().equals(studyProgramme7.getText())){
                studyProgramme7.setSelected(true);
            } else if(studyProgrammes.get(i).getName().equals(studyProgramme8.getText())){
                studyProgramme8.setSelected(true);
            } else if(studyProgrammes.get(i).getName().equals(studyProgramme9.getText())){
                studyProgramme9.setSelected(true);
            } else if(studyProgrammes.get(i).getName().equals(studyProgramme10.getText())){
                studyProgramme10.setSelected(true);
            }
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMessage.setText("");
    }    
    
    private void editDetails(String description, String academicYear, ArrayList<StudyProgramme> studyProgrammes){
        try {
            practice = ((Company)practiceSystem.getLoggedUser()).editPractice(practice, description, academicYear, studyProgrammes);
            setPracticeInfo();
            errorMessage.setText("");
            AlertBox.display("Praxe", "Úspěšně změněno!");
        } catch (SQLException | ClassNotFoundException ex) {
            errorMessage.setText("Nepodařilo se změnit údaj");
        } catch (BlankDataException ex) {
            errorMessage.setText("Není vyplněný údaj, který chcete změnit");
        } catch (InvalidStudyProgrammesCombinationException ex) {
            if(((Company)practiceSystem.getLoggedUser()).getState().equalsIgnoreCase("Česká Republika")){
                errorMessage.setText("Jelikož jste česká firma, tak se na Vaše praxe mohou hlásit pouze studenti "
                        + "z bakalářských studijních oborů");
            } else {
                errorMessage.setText("Jelikož jste zahraniční firma, tak se na Vaše praxe mohou hlásit pouze studenti "
                        + "z navazujících studijních oborů");
            }
        }
    }
    
    public void changeAcademicYear(ActionEvent event) {
        editDetails(practice.getDescription(), (String)academicYear.getValue(), practice.getTypesOfAllowedStudyProgrammes());
    }
    
    public void changeDescription(ActionEvent event) {
        editDetails(description.getText(), practice.getAcademicYear(), practice.getTypesOfAllowedStudyProgrammes());
    }
    
    public void changeStudyProgrammes(ActionEvent event) {
        ArrayList<StudyProgramme> studyProgrammes = new ArrayList();
        if(studyProgramme1.isSelected()){  
            studyProgrammes.add(new StudyProgramme(studyProgramme1.getText()));
        }
        if(studyProgramme2.isSelected()){
            studyProgrammes.add(new StudyProgramme(studyProgramme2.getText()));    
        }
        if(studyProgramme3.isSelected()){
            studyProgrammes.add(new StudyProgramme(studyProgramme3.getText()));     
        }
        if(studyProgramme4.isSelected()){
            studyProgrammes.add(new StudyProgramme(studyProgramme4.getText())); 
        }
        if(studyProgramme5.isSelected()){
            studyProgrammes.add(new StudyProgramme(studyProgramme5.getText())); 
        }
        if(studyProgramme6.isSelected()){
            studyProgrammes.add(new StudyProgramme(studyProgramme6.getText()));   
        }
        if(studyProgramme7.isSelected()){
            studyProgrammes.add(new StudyProgramme(studyProgramme7.getText()));    
        }
        if(studyProgramme8.isSelected()){
            studyProgrammes.add(new StudyProgramme(studyProgramme8.getText()));     
        }
        if(studyProgramme9.isSelected()){
            studyProgrammes.add(new StudyProgramme(studyProgramme9.getText()));    
        }
        if(studyProgramme10.isSelected()){
            studyProgrammes.add(new StudyProgramme(studyProgramme10.getText()));   
        }
        editDetails(practice.getDescription(), practice.getAcademicYear(), studyProgrammes);
    }
    
    public void goBack(ActionEvent event) {
        application.goToDisplayPracticeCompany(practiceSystem, practice);
    }
}
