package gui;

import app.Company;
import app.Practice;
import app.PracticeSystem;
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
import utils.boxes.ConfirmBox;

/**
 * FXML Controller class
 *
 * @author lukaskorinek
 */
public class MyPracticesCompanyController implements Initializable {

    @FXML
    private Label loggedCompany;
    @FXML
    private ListView myPracticesList;
    @FXML
    private Button buttonDisplayPractice;
    @FXML
    private Button buttonDeletePractice;
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
        setListView();
    }
    
    private void setLoggedUserName(){
        loggedCompany.setText(practiceSystem.getLoggedUserFullName());
    }
    
    private void setListView() throws SQLException, ClassNotFoundException{
        Company company = (Company)practiceSystem.getLoggedUser();
        ObservableList<String> items = FXCollections.observableArrayList();
        ArrayList<Practice> myPractices = company.getMyPractices();
        for (int i = 0; i < myPractices.size(); i++) {
            items.add(myPractices.get(i).getNumberAndDescription());
        }
        myPracticesList.setItems(items);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMessage.setText("");
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
        String practiceNameAndDescription = (String)myPracticesList.getSelectionModel().getSelectedItem();
        try {
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
        application.goToDisplayPracticeCompany(practiceSystem, practice);
    }
    
    public void deleteSelectedPractice(ActionEvent event){
        String practiceNameAndDescription = (String)myPracticesList.getSelectionModel().getSelectedItem();
        try {
            deletePractice(getSelectedPractice(practiceNameAndDescription));
        } catch (SQLException ex) {
            errorMessage.setText("SQLException");
        } catch (ClassNotFoundException ex) {
            errorMessage.setText("ClassNotFoundException");
        } catch (BlankDataException ex) {
            errorMessage.setText("Není zvolena žádná praxe");
        }
    }
    
    private void deletePractice(Practice practice) throws SQLException, ClassNotFoundException{
        boolean confirmation = ConfirmBox.display("Smazání praxe", "Opravdu chcete smazat vybranou praxi?");
        if(confirmation){
            ((Company)practiceSystem.getLoggedUser()).deletePractice(practice);
            errorMessage.setText("");
            setListView();
            AlertBox.display("Smazání praxe", "Praxe byla úspěšně smazána");
        }
    }
    
    public void goBack(ActionEvent event) {
        application.goToMenuCompany(practiceSystem);
    }
}
