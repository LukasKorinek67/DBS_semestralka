package gui;

import app.Company;
import app.Practice;
import app.PracticeSystem;
import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.FileNotFoundException;
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
import javafx.scene.control.TextField;
import utils.boxes.AlertBox;
import utils.boxes.DirectoryChooserBox;
import utils.exceptions.BlankDataException;
import utils.exceptions.DidntSelectFileException;

/**
 * FXML Controller class
 *
 * @author lukaskorinek
 */
public class DisplayPracticeMessageCompanyController implements Initializable {

    @FXML
    private Label loggedCompany;
    @FXML
    private Label title;
    @FXML
    private TextArea message;
    @FXML
    private Button buttonSelectDirectory;
    @FXML
    private Label selectedDiretoryPath;
    @FXML
    private TextField pdfFileName;
    @FXML
    private Button buttonDownloadMessageToPDF;
    @FXML
    private Label errorMessage;
    @FXML
    private Hyperlink backLink;
    
    private Main application;
    private PracticeSystem practiceSystem;
    private Practice practice;
    private File directory;
    
    
    public void setApp(Main application, PracticeSystem practiceSystem, Practice practice){
        this.application = application;
        this.practiceSystem = practiceSystem;
        this.practice = practice;
        setLoggedUserName();
        setTitleAndPracticeMessage();
    }
    
    private void setLoggedUserName(){
        loggedCompany.setText(practiceSystem.getLoggedUserFullName());
    }
    
    private void setTitleAndPracticeMessage(){
        title.setText("Písemná zpráva z praxe " + practice.getNumberAndDescription());
        message.setText(practice.getWrittenReportFromStudent());
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMessage.setText("");
        selectedDiretoryPath.setText("");
    }    
    
    public void downloadMessageToPDF(ActionEvent event) {
        try{
            ((Company)practiceSystem.getLoggedUser()).downloadMessageFromPractice(practice, getFile());
            AlertBox.display("Stáhnout zprávu z praxe do PDF", "Úspěšně staženo!");
        }catch (BlankDataException ex){
            errorMessage.setText("Nevyplnil/a jste všechny údaje");
        } catch (FileNotFoundException | DocumentException ex) {
            errorMessage.setText("Nepodařilo se vytvořit PDF soubor");
        } catch (SQLException | ClassNotFoundException ex) {
            errorMessage.setText("Problém s načtením jména studenta do textu souboru PDF");
        }
    } 
    
    public void selectDirectory(ActionEvent event) {
        try {
            File selectedDirectory = DirectoryChooserBox.choose();
            if(selectedDirectory.isDirectory()){
                directory = selectedDirectory;
                selectedDiretoryPath.setText(directory.getAbsolutePath());
                errorMessage.setText("");
            }else{
                errorMessage.setText("Vybraný soubor není složka");
            }
        } catch (DidntSelectFileException ex) {
            directory = null;
            selectedDiretoryPath.setText("");
            errorMessage.setText("");
        }
    }
    
    private File getFile() throws BlankDataException{
        return new File(getDirectoryPath() + File.separator + getPdfFileName());
    }
    
    private String getDirectoryPath() throws BlankDataException{
        if(directory != null){
            return directory.getAbsolutePath();
        }else{
            throw new BlankDataException();
        }
    }
    
    private String getPdfFileName() throws BlankDataException{
        if("".equals(pdfFileName.getText())){
            throw new BlankDataException();
        }
        if(pdfFileName.getText().endsWith(".pdf")){
            return pdfFileName.getText();
        }else{
            return pdfFileName.getText() + ".pdf";
        }  
    }
    
    public void goBack(ActionEvent event) {
        application.goToDisplayPracticeCompany(practiceSystem, practice);
    }
}
