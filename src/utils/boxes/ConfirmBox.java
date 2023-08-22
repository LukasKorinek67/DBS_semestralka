package utils.boxes;

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
// Box na utvrzení - např. "Opravdu se chcete odhlásit?" tlařítka "Ano", "Ne"
public class ConfirmBox {
    
    static boolean answer;
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

    public static boolean display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(150);
        Label label = new Label();
        label.setText(message);

        // vytvoření tlačítek
        Button yesButton = new Button("Ano");
        Button noButton = new Button("Ne");

        // nastylování tlačítek
        yesButton.setStyle(NORMAL_BUTTON_STYLE);
        yesButton.setOnMouseEntered(e -> yesButton.setStyle(HOVERED_BUTTON_STYLE));
        yesButton.setOnMouseExited(e -> yesButton.setStyle(NORMAL_BUTTON_STYLE));
        noButton.setStyle(NORMAL_BUTTON_STYLE);
        noButton.setOnMouseEntered(e -> noButton.setStyle(HOVERED_BUTTON_STYLE));
        noButton.setOnMouseExited(e -> noButton.setStyle(NORMAL_BUTTON_STYLE));
        
        // přidání hodnoty po kliknutí do answer
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        VBox layout = new VBox(10);

        // přidání tlačítek
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20,20,20,20));
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
