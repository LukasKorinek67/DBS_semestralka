package utils.boxes;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.exceptions.DidntSelectFileException;

/**
 *
 * @author lukaskorinek
 */
// Box na otevření okna, kde si uživatel zvolí složku, kam chce uložit soubor
public class DirectoryChooserBox {
    private static File directory;
    private static Stage window;
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
    
    public static File choose() throws DidntSelectFileException {
        display("Zvolit složku", "Pro zvolení stiskněte tlačítko");
        if(directory == null){
            throw new DidntSelectFileException();
        }else{
            return directory;
        }
    }
    
    private static void display(String title, String message) {
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);
        Button chooseButton = new Button("Zvolit");
        
        // nastylování tlačítka
        chooseButton.setStyle(NORMAL_BUTTON_STYLE);
        chooseButton.setOnMouseEntered(e -> chooseButton.setStyle(HOVERED_BUTTON_STYLE));
        chooseButton.setOnMouseExited(e -> chooseButton.setStyle(NORMAL_BUTTON_STYLE));

        chooseButton.setOnAction(new SingleFcButtonListener());
        
        VBox layout = new VBox(10);

        // přidání tlačítka
        layout.getChildren().addAll(label, chooseButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20,20,20,20));
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
    
    private static class SingleFcButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            showSingleFileChooser();
        }
    }
    
    private static void showSingleFileChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);
        directory = selectedDirectory;
        window.close();
    } 
}