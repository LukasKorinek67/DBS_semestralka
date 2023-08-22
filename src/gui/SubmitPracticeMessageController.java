package gui;

import app.Practice;
import app.PracticeSystem;
import app.Student;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import utils.boxes.AlertBox;
import utils.boxes.ConfirmBox;
import utils.boxes.FileChooserBox;
import utils.exceptions.BlankDataException;
import utils.exceptions.CouldNotReadPDFException;
import utils.exceptions.DidntSelectFileException;
import utils.exceptions.InvalidDataLengthException;
import utils.exceptions.InvalidFileTypeException;
import utils.exceptions.WrongPracticeException;

/**
 * FXML Controller class
 *
 * @author lukaskorinek
 */
public class SubmitPracticeMessageController implements Initializable {

    @FXML
    private Label loggedStudent;
    @FXML
    private Label practiceNumberAndDescription;
    @FXML
    private Label actualMessage;
    @FXML
    private Button buttonDisplayMessage;
    @FXML
    private TextArea message;
    @FXML
    private Button buttonSelectPDFFile;
    @FXML
    private Label selectedFileName;
    @FXML
    private Button buttonSubmitMessage;
    @FXML
    private Button buttonSubmitPDFMessage;
    @FXML
    private Label errorMessage;
    @FXML
    private Hyperlink backLink;
    
    private Main application;
    private PracticeSystem practiceSystem;
    private Practice practice;
    private File pdfFile;
    
    
    public void setApp(Main application, PracticeSystem practiceSystem, Practice practice){
        this.application = application;
        this.practiceSystem = practiceSystem;
        this.practice = practice;
        setLoggedUserName();
        setPracticeInfo();
    }
    
    private void setLoggedUserName(){
        loggedStudent.setText(practiceSystem.getLoggedUserFullName());
    }
    
    private void setPracticeInfo(){
        practiceNumberAndDescription.setText(practice.getNumberAndDescription());
        message.setText("");
        pdfFile = null;
        selectedFileName.setText("Žádný nevybrán");
        errorMessage.setText("");
        
        if(practice.getWrittenReportFromStudent() != null){
            actualMessage.setText("Již odevzdána");
            buttonDisplayMessage.setVisible(true);
            buttonSubmitMessage.setText("Změnit odevzdanou zprávu");
            buttonSubmitPDFMessage.setText("Změnit odevzdanou zprávu na zvolený PDF soubor");
        }else {
            actualMessage.setText("Zatím neexistuje");
            buttonDisplayMessage.setVisible(false);
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMessage.setText("");
    }    
    
    public void displayMessage(ActionEvent event) {
        if(practice.getWrittenReportFromStudent() == null){
            errorMessage.setText("Žádná zpráva zatím nebyla odevzdána");
        }else{
            application.goToDisplayPracticeMessageStudent(practiceSystem, practice);
        }
    }
    
    public void submitMessage(ActionEvent event) {
        try {
            if(message.getText().equals("")){
                throw new BlankDataException();
            }
            if(practice.getWrittenReportFromStudent() == null){
                boolean confirmation = ConfirmBox.display("Odevzdání písemné zprávy", "Opravdu chcete odevzdat tuto zprávu?");
                if(confirmation){
                    practice = ((Student)practiceSystem.getLoggedUser()).submitMessageFromPractice(message.getText(), practice);
                    setPracticeInfo();
                    AlertBox.display("Odevzdání písemné zprávy", "Úspěšně odevzdáno!");
                }
            }else{
                boolean confirmation = ConfirmBox.display("Odevzdání písemné zprávy", "Opravdu chcete změnit odevzdanou zprávu?");
                if(confirmation){
                    practice = ((Student)practiceSystem.getLoggedUser()).submitMessageFromPractice(message.getText(), practice);
                    setPracticeInfo();
                    AlertBox.display("Odevzdání písemné zprávy", "Úspěšně změněno!");
                }
            }
        } catch (WrongPracticeException ex) {
            errorMessage.setText("Nelze odevzdat zprávu k praxi, ke které nejste přiřazen");
        } catch (BlankDataException ex) {
            errorMessage.setText("Zpráva neobsahuje žádný text");
        } catch (SQLException ex) {
            errorMessage.setText("SQLException");
        } catch (InvalidDataLengthException ex) {
            errorMessage.setText("Zpráva je příliš dlouhá - maximální délka 500 znaků");
        }
    }
    
    public void selectPDF(ActionEvent event) {
        try {
            pdfFile = FileChooserBox.choose();
            selectedFileName.setText(pdfFile.getName());
            errorMessage.setText("");
        } catch (DidntSelectFileException ex) {
            pdfFile = null;
            selectedFileName.setText("Žádný nevybrán");
            errorMessage.setText("");
        }
    }
    
    public void submitPDF(ActionEvent event) {
        try {
            if(practice.getWrittenReportFromStudent() == null){
                boolean confirmation = ConfirmBox.display("Odevzdání písemné zprávy", "Opravdu chcete odevzdat tento soubor?");
                if(confirmation){
                    practice = ((Student)practiceSystem.getLoggedUser()).submitMessageFromPracticePDF(pdfFile, practice);
                    setPracticeInfo();
                    AlertBox.display("Odevzdání písemné zprávy", "Soubor úspěšně odevzdán!");
                }
            }else{
                boolean confirmation = ConfirmBox.display("Odevzdání písemné zprávy", "Opravdu chcete odevzdat tento soubor a změnit tak odevzdanou zprávu?");
                if(confirmation){
                    practice = ((Student)practiceSystem.getLoggedUser()).submitMessageFromPracticePDF(pdfFile, practice);
                    setPracticeInfo();
                    AlertBox.display("Odevzdání písemné zprávy", "Soubor úspěšně odevzdán, písemná zpráva změněna!");
                }
            }
        } catch (DidntSelectFileException ex) {
            errorMessage.setText("Nevybral/a jste soubor");
        } catch (InvalidFileTypeException ex) {
            errorMessage.setText("Soubor, který jste vybral/a není typu PDF, vyberte prosím znovu");
        } catch (IOException | CouldNotReadPDFException ex) {
            errorMessage.setText("Nepodařilo se přečíst PDF soubor");
        } catch (WrongPracticeException ex) {
            errorMessage.setText("Nelze odevzdat zprávu k praxi, ke které nejste přiřazen");
        } catch (SQLException ex) {
            errorMessage.setText("SQLException");
        } catch (InvalidDataLengthException ex) {
            errorMessage.setText("Zpráva je příliš dlouhá - maximální délka 500 znaků");
        }
    }
    
    public void goBack(ActionEvent event) {
        application.goToMyPracticesStudent(practiceSystem);
    }
    
}
