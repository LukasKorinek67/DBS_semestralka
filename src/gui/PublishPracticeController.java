package gui;

import app.Company;
import utils.boxes.AlertBox;
import app.PracticeSystem;
import app.StudyProgramme;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import utils.exceptions.BlankDataException;
import utils.exceptions.InvalidStudyProgrammesCombinationException;

/**
 * FXML Controller class
 *
 * @author lukaskorinek
 */
public class PublishPracticeController implements Initializable {

    @FXML
    private Label loggedCompany;
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
    private DatePicker datePicker;
    @FXML
    private Button buttonPublish;
    @FXML
    private Label errorMessage;
    @FXML
    private Hyperlink backLink;
    
    private Main application;
    private PracticeSystem practiceSystem;
    private LocalDateTime dateTime;
    
    public void setApp(Main application, PracticeSystem practiceSystem) throws SQLException, ClassNotFoundException{
        this.application = application;
        this.practiceSystem = practiceSystem;
        setLoggedUserName();
        setCheckBoxesNames();
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
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
        errorMessage.setText("");
    }    
    
    private void setDate() throws BlankDataException {
        if(datePicker.getValue() == null){
            throw new BlankDataException();
        }else{
            dateTime = datePicker.getValue().atTime(LocalTime.now());
        }
    }
    
    public void publishPractice(ActionEvent event) {
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
        try {
            setDate();
            ((Company)practiceSystem.getLoggedUser()).publishPractice((String)academicYear.getValue(), description.getText(), studyProgrammes, dateTime);
            AlertBox.display("Praxe", "Praxe úspěšně vypsána!");
            application.goToMenuCompany(practiceSystem);
        } catch (SQLException ex) {
            errorMessage.setText("SQLException");
        } catch (ClassNotFoundException ex) {
            errorMessage.setText("ClassNotFoundException");
        } catch (BlankDataException ex) {
            errorMessage.setText("Nejsou vyplněny všechny údaje");
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
    
    public void goBack(ActionEvent event) {
        application.goToMenuCompany(practiceSystem);
    }
}
