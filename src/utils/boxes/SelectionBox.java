package utils.boxes;

import app.Student;
import app.StudyProgramme;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author lukaskorinek
 */
// box pro volbu studenta ze seznamu přihlášených studentů, nebo volbu programu, ke kterému chce student zažádat o praxi
public class SelectionBox {
    
//Create variable
    static StudyProgramme answerStudyProgramme;
    static Student answerStudent;
    static final String NORMAL_BUTTON_STYLE = ("-fx-background-color: \n" +
            "        rgba(0,0,0,0.08),\n" +
            "        linear-gradient(#9a9a9a, #909090),\n" +
            "        linear-gradient(white 0%, #f3f3f3 50%, #ececec 51%, #f2f2f2 100%);\n" +
            "    -fx-background-insets: 0 0 -1 0,0,1;\n" +
            "    -fx-background-radius: 5,5,4;\n" +
            "    -fx-padding: 3 30 3 30;\n" +
            "    -fx-text-fill: #242d35;\n" +
            "    -fx-font-size: 14px;");
    static final String HOVERED_BUTTON_STYLE = ("-fx-background-color:\n" +
            "        rgba(255, 255, 255, 0.08),\n" +
            "        rgba(0, 0, 0, 0.8),\n" +
            "        /*#090a0c,*/#cccccc,\n" +
            "        \n" +
            "        linear-gradient(#cccccc 0%, #cccccc 20%, #cccccc 100%),\n" +
            "        linear-gradient(#cccccc, #cccccc),\n" +
            "        radial-gradient(center 50% 0%, radius 100%, rgba(255,255,255,255),\n" +
            "        rgba(255,255,255,0));\n" +
            "    -fx-background-insets: 0 0 -1 0,0,1;\n" +
            "    -fx-background-radius: 5,5,4;\n" +
            "    -fx-padding: 3 30 3 30;\n" +
            "    -fx-text-fill: #242d35;\n" +
            "    -fx-font-size: 14px;");

    public static StudyProgramme display(String title, String message, ArrayList<StudyProgramme> studyProgramme) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        ArrayList<Button> buttons = new ArrayList<>();

        for (int i = 0; i < studyProgramme.size(); i++) {
            StudyProgramme selectedStudyProgramme = studyProgramme.get(i);
            Button button = new Button(studyProgramme.get(i).getName());
            // nastylování tlačítka
            button.setStyle(NORMAL_BUTTON_STYLE);
            button.setOnMouseEntered(e -> button.setStyle(HOVERED_BUTTON_STYLE));
            button.setOnMouseExited(e -> button.setStyle(NORMAL_BUTTON_STYLE));
            
            button.setOnAction(e -> {
                answerStudyProgramme = selectedStudyProgramme;
                window.close();
            });
            buttons.add(button);
        }

        VBox layout = new VBox(10);

        layout.getChildren().add(label);
        // přidání tlačítek
        for (int j = 0; j < buttons.size(); j++) {
            layout.getChildren().add(buttons.get(j));
        }

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20,20,20,20));
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return answerStudyProgramme;
    }
    
    public static Student display(ArrayList<Student> students, String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        ArrayList<Button> buttons = new ArrayList<>();

        for (int i = 0; i < students.size(); i++) {
            Student selectedStudent = students.get(i);
            Button button = new Button(students.get(i).getPersonalNumber() + " - " + students.get(i).getFullName());
            // nastylování tlačítka
            button.setStyle(NORMAL_BUTTON_STYLE);
            button.setOnMouseEntered(e -> button.setStyle(HOVERED_BUTTON_STYLE));
            button.setOnMouseExited(e -> button.setStyle(NORMAL_BUTTON_STYLE));
            
            button.setOnAction(e -> {
                answerStudent = selectedStudent;
                window.close();
            });
            buttons.add(button);
        }

        VBox layout = new VBox(10);

        layout.getChildren().add(label);
        // přidání tlačítek
        for (int j = 0; j < buttons.size(); j++) {
            layout.getChildren().add(buttons.get(j));
        }

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20,20,20,20));
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return answerStudent;
    }
}
