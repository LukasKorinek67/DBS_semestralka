package gui;

import utils.boxes.AlertBox;
import app.PracticeSystem;
import app.Student;
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
public class EditStudentProfileController implements Initializable {

    @FXML
    private Label loggedStudent;
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
    private Button usernameButton;
    @FXML
    private Button passwordButton;
    @FXML
    private Button personalNumberButton;
    @FXML
    private Button nameButton;
    @FXML
    private Button surnameButton;
    @FXML
    private Button emailButton;
    @FXML
    private Button studyProgrammesButton;
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
        setCheckBoxesNames();
        setLoggedUserDetails();
    }
    
    private void setLoggedUserName(){
        loggedStudent.setText(practiceSystem.getLoggedUserFullName());
    }
    
    private void setLoggedUserDetails(){
        Student student = (Student)practiceSystem.getLoggedUser();
        
        username.setText(student.getUsername());
        password.setText(student.getPassword());
        personalNumber.setText(student.getPersonalNumber());
        name.setText(student.getName());
        surname.setText(student.getLastName());
        email.setText(student.getEmail());
        ArrayList<StudyProgramme> studyProgrammes = student.getStudyProgrammes();
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
    
    private void editDetails(String username, String password, String personalNumber, String name, String lastName, String email, ArrayList<StudyProgramme> studyProgrammes){
        try {
            ((Student)practiceSystem.getLoggedUser()).editProfile(username, password, personalNumber, name, lastName, email, studyProgrammes);
            setLoggedUserDetails();
            AlertBox.display("Profil", "Úspěšně změněno!");
        } catch (SQLException ex) {
            errorMessage.setText("Zadané přihlašovací jméno nebo osobní číslo již někdo používá");
        } catch (BlankDataException ex) {
            errorMessage.setText("Není vyplněný údaj, který chcete změnit");
        } catch (InvalidDataLengthException ex) {
            errorMessage.setText("Nový údaj je příliš dlouhý");
        } catch (PasswordTooShortException ex) {
            errorMessage.setText("Heslo příliš krátké - musí obsahovat 6 - 20 znaků");
        } catch (InvalidEmailAddressException ex) {
            errorMessage.setText("Neplatná e-mailová adresa");
        } catch (InvalidStudyProgrammesCombinationException ex) {
            errorMessage.setText("Nelze studovat stejný bakalářský a navazující obor zároveň");
        } catch (InvalidPersonalNumberException ex) {
            errorMessage.setText("Nesprávné osobní číslo - čísla na TUL jsou ve formátu \"X19000000\", kde X značí fakultu");
        } catch (ClassNotFoundException ex) {
            errorMessage.setText("ClassNotFoundException");
        } 
    }
    
    public void changeUsername(ActionEvent event) {
        Student student = (Student)practiceSystem.getLoggedUser();
        editDetails(username.getText(), student.getPassword(), student.getPersonalNumber(), 
                student.getName(), student.getLastName(), student.getEmail(), student.getStudyProgrammes());
    }
    
    public void changePassword(ActionEvent event) {
        Student student = (Student)practiceSystem.getLoggedUser();
        editDetails(student.getUsername(), password.getText(), student.getPersonalNumber(), 
                student.getName(), student.getLastName(), student.getEmail(), student.getStudyProgrammes());
    }
    
    public void changePersonalNumber(ActionEvent event) {
        Student student = (Student)practiceSystem.getLoggedUser();
        editDetails(student.getUsername(), student.getPassword(), personalNumber.getText(), 
                student.getName(), student.getLastName(), student.getEmail(), student.getStudyProgrammes());
    }
    
    public void changeName(ActionEvent event) {
        Student student = (Student)practiceSystem.getLoggedUser();
        editDetails(student.getUsername(), student.getPassword(), student.getPersonalNumber(), 
                name.getText(), student.getLastName(), student.getEmail(), student.getStudyProgrammes());
    }
    
    public void changeSurname(ActionEvent event) {
        Student student = (Student)practiceSystem.getLoggedUser();
        editDetails(student.getUsername(), student.getPassword(), student.getPersonalNumber(), 
                student.getName(), surname.getText(), student.getEmail(), student.getStudyProgrammes());
    }
    
    public void changeEmail(ActionEvent event) {
        Student student = (Student)practiceSystem.getLoggedUser();
        editDetails(student.getUsername(), student.getPassword(), student.getPersonalNumber(), 
                student.getName(), student.getLastName(), email.getText(), student.getStudyProgrammes());
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
        Student student = (Student)practiceSystem.getLoggedUser();
        editDetails(student.getUsername(), student.getPassword(), student.getPersonalNumber(), 
                student.getName(), student.getLastName(), student.getEmail(), studyProgrammes);
    }
    
    public void goBack(ActionEvent event) {
        application.goToMenuStudent(practiceSystem);
    }
}
