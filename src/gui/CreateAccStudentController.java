package gui;

import utils.boxes.AlertBox;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utils.exceptions.BlankDataException;
import utils.exceptions.InvalidDataLengthException;
import utils.exceptions.InvalidEmailAddressException;
import utils.exceptions.InvalidPersonalNumberException;
import utils.exceptions.InvalidStudyProgrammesCombinationException;
import utils.exceptions.PasswordTooShortException;

/**
 * FXML Controller class
 *
 * @author lukaskorinek
 */
public class CreateAccStudentController implements Initializable {
    
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private TextField personalNumber;
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private TextField email;
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
    private Label errorMessage;
    @FXML
    private Button createAccButton;
    @FXML
    private Hyperlink backLink;
    
    private Main application;
    private PracticeSystem practiceSystem;
    
    public void setApp(Main application, PracticeSystem practiceSystem) throws SQLException, ClassNotFoundException{
        this.application = application;
        this.practiceSystem = practiceSystem;
        setCheckBoxesNames();
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
        errorMessage.setText("");
    }    
    
    public void createStudentAcc(ActionEvent event) {
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
        try{
            practiceSystem.createStudentAccount(username.getText(), password.getText(), personalNumber.getText(), name.getText(), surname.getText(), email.getText(), studyProgrammes);
            AlertBox.display("Nový student", "Nový účet úspěšně vytvořen!");
            application.goToMenuStudent(practiceSystem);
        } catch (SQLException ex) {
            errorMessage.setText("Zadané přihlašovací jméno nebo osobní číslo již někdo používá");
        } catch (ClassNotFoundException ex) {
            errorMessage.setText("ClassNotFoundException");
        } catch (BlankDataException ex) {
            errorMessage.setText("Nejsou vyplněné všechny údaje");
        } catch (InvalidDataLengthException ex) {
            errorMessage.setText("Některý z údajů je příliš dlouhý");
        } catch (PasswordTooShortException ex) {
            errorMessage.setText("Heslo příliš krátké - musí obsahovat 6 - 20 znaků.");
        } catch (InvalidEmailAddressException ex) {
            errorMessage.setText("Neplatná e-mailová adresa");
        } catch (InvalidStudyProgrammesCombinationException ex) {
            errorMessage.setText("Nelze studovat stejný bakalářský a navazující obor zároveň");
        } catch (InvalidPersonalNumberException ex) {
            errorMessage.setText("Nesprávné osobní číslo - čísla na TUL jsou ve formátu \"X19000000\", kde X značí fakultu");
        } 
        
    }
    
    public void goBack(ActionEvent event) {
        application.goToLogin(practiceSystem);
    }
}
