package gui;

import app.Practice;
import app.PracticeSystem;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;



/**
 *
 * @author lukaskorinek
 */
public class Main extends Application {
    
    private Stage stage;
    public static PracticeSystem practiceSystem = new PracticeSystem();
    private final double MINIMUM_WINDOW_WIDTH = 800.0;
    private final double MINIMUM_WINDOW_HEIGHT = 500.0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(Main.class, (java.lang.String[])null);
    }

    @Override
    public void start(Stage primaryStage) {
            stage = primaryStage;
            stage.setTitle("DBS - Semestrální práce - Böhm, Kořínek, Rubeš");
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            goToLogin(practiceSystem);
            primaryStage.show();
    }
    
    public void goToLogin(PracticeSystem practiceSystem) {
        try {
            this.practiceSystem = practiceSystem;
            LoginController login = (LoginController) replaceSceneContent("login.fxml");
            login.setApp(this, this.practiceSystem);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void goToCreateAccStudent(PracticeSystem practiceSystem) {
        try {
            this.practiceSystem = practiceSystem;
            CreateAccStudentController createAccStudent = (CreateAccStudentController) replaceSceneContent("createAccStudent.fxml");
            createAccStudent.setApp(this, this.practiceSystem);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void goToCreateAccCompany(PracticeSystem practiceSystem) {
        try {
            this.practiceSystem = practiceSystem;
            CreateAccCompanyController createAccCompany = (CreateAccCompanyController) replaceSceneContent("createAccCompany.fxml");
            createAccCompany.setApp(this, this.practiceSystem);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void goToMenuStudent(PracticeSystem practiceSystem) {
        try {
            this.practiceSystem = practiceSystem;
            MenuStudentController menuStudent = (MenuStudentController) replaceSceneContent("menuStudent.fxml");
            menuStudent.setApp(this, this.practiceSystem);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void goToMenuCompany(PracticeSystem practiceSystem) {
        try {
            this.practiceSystem = practiceSystem;
            MenuCompanyController menuCompany = (MenuCompanyController) replaceSceneContent("menuCompany.fxml");
            menuCompany.setApp(this, this.practiceSystem);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void goToEditStudentProfile(PracticeSystem practiceSystem) {
        try {
            this.practiceSystem = practiceSystem;
            EditStudentProfileController editStudent = (EditStudentProfileController) replaceSceneContent("editStudentProfile.fxml");
            editStudent.setApp(this, this.practiceSystem);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void goToEditCompanyProfile(PracticeSystem practiceSystem) {
        try {
            this.practiceSystem = practiceSystem;
            EditCompanyProfileController editCompany = (EditCompanyProfileController) replaceSceneContent("editCompanyProfile.fxml");
            editCompany.setApp(this, this.practiceSystem);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void goToMyPracticesStudent(PracticeSystem practiceSystem) {
        try {
            this.practiceSystem = practiceSystem;
            MyPracticesStudentController myPracticesStudent = (MyPracticesStudentController) replaceSceneContent("myPracticesStudent.fxml");
            myPracticesStudent.setApp(this, this.practiceSystem);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void goToMyPracticesCompany(PracticeSystem practiceSystem) {
        try {
            this.practiceSystem = practiceSystem;
            MyPracticesCompanyController myPracticesCompany = (MyPracticesCompanyController) replaceSceneContent("myPracticesCompany.fxml");
            myPracticesCompany.setApp(this, this.practiceSystem);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void goToDisplayMyApplications(PracticeSystem practiceSystem) {
        try {
            this.practiceSystem = practiceSystem;
            DisplayMyApplicationsController displayMyApplications = (DisplayMyApplicationsController) replaceSceneContent("displayMyApplications.fxml");
            displayMyApplications.setApp(this, this.practiceSystem);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void goToPublishPractice(PracticeSystem practiceSystem) {
        try {
            this.practiceSystem = practiceSystem;
            PublishPracticeController publishPractice = (PublishPracticeController) replaceSceneContent("publishPractice.fxml");
            publishPractice.setApp(this, this.practiceSystem);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void goToListPractices(PracticeSystem practiceSystem) {
        try {
            this.practiceSystem = practiceSystem;
            ListPracticesController listPractices = (ListPracticesController) replaceSceneContent("listPractices.fxml");
            listPractices.setApp(this, this.practiceSystem);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void goToDisplayPracticeStudent(PracticeSystem practiceSystem, Practice practiceToDisplay, Initializable previousPage) {
        try {
            this.practiceSystem = practiceSystem;
            DisplayPracticeStudentController displayPracticeStudent = (DisplayPracticeStudentController) replaceSceneContent("displayPracticeStudent.fxml");
            displayPracticeStudent.setApp(this, this.practiceSystem, practiceToDisplay, previousPage);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void goToDisplayPracticeCompany(PracticeSystem practiceSystem, Practice practiceToDisplay) {
        try {
            this.practiceSystem = practiceSystem;
            DisplayPracticeCompanyController displayPracticeCompany = (DisplayPracticeCompanyController) replaceSceneContent("displayPracticeCompany.fxml");
            displayPracticeCompany.setApp(this, this.practiceSystem, practiceToDisplay);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void goToEditPractice(PracticeSystem practiceSystem, Practice practiceToEdit) {
        try {
            this.practiceSystem = practiceSystem;
            EditPracticeController editPractice = (EditPracticeController) replaceSceneContent("editPractice.fxml");
            editPractice.setApp(this, this.practiceSystem, practiceToEdit);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void goToSubmitPracticeMessage(PracticeSystem practiceSystem, Practice practice) {
        try {
            this.practiceSystem = practiceSystem;
            SubmitPracticeMessageController submitPracticeMessage = (SubmitPracticeMessageController) replaceSceneContent("submitPracticeMessage.fxml");
            submitPracticeMessage.setApp(this, this.practiceSystem, practice);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void goToDisplayPracticeMessageStudent(PracticeSystem practiceSystem, Practice practice) {
        try {
            this.practiceSystem = practiceSystem;
            DisplayPracticeMessageStudentController displayPracticeMessageStudent = (DisplayPracticeMessageStudentController) replaceSceneContent("displayPracticeMessageStudent.fxml");
            displayPracticeMessageStudent.setApp(this, this.practiceSystem, practice);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void goToDisplayPracticeMessageCompany(PracticeSystem practiceSystem, Practice practice) {
        try {
            this.practiceSystem = practiceSystem;
            DisplayPracticeMessageCompanyController displayPracticeMessageCompany = (DisplayPracticeMessageCompanyController) replaceSceneContent("displayPracticeMessageCompany.fxml");
            displayPracticeMessageCompany.setApp(this, this.practiceSystem, practice);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // změna scény
    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        } 
        Scene scene = new Scene(page, 800, 600);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }   
}