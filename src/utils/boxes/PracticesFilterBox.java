package utils.boxes;

import app.Company;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.PracticesFilter;

/**
 *
 * @author lukaskorinek
 */
// Box na zvolení filtrů
public class PracticesFilterBox {
    
    static PracticesFilter filter;
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
    
    public static PracticesFilter display(String title, String message) throws SQLException, ClassNotFoundException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(150);
        Label label = new Label();
        label.setText(message);

        
        //podle firmy
        Label labelCompany = new Label();
        labelCompany.setText("Firma:");
        ChoiceBox company = new ChoiceBox();
        ArrayList<Company> allCompanies = Company.getAllCompanies();
        ArrayList<String> allCompaniesChoices = new ArrayList<>();
        for (int i = 0; i < allCompanies.size(); i++) {
            allCompaniesChoices.add(allCompanies.get(i).getName());
        }
        allCompaniesChoices.add(0, "Nefiltrovat");
        company.getItems().addAll(allCompaniesChoices);
        company.setValue("Nefiltrovat");
        
        //podle akademického roku
        Label labelAcademicYear = new Label();
        labelAcademicYear.setText("Akademický rok:");
        ChoiceBox academicYear = new ChoiceBox();
        academicYear.getItems().add("Nefiltrovat");
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
        academicYear.setValue("Nefiltrovat");
        
        //praxe pro bakalářské vs navazující studium
        Label labelStudyProgrammeType = new Label();
        labelStudyProgrammeType.setText("Pro bakalářské/navující studium:");
        ChoiceBox studyProgrammeType = new ChoiceBox();
        studyProgrammeType.getItems().add("Nefiltrovat");
        studyProgrammeType.getItems().add("Praxe pro bakalářské studium");
        studyProgrammeType.getItems().add("Praxe pro navazující studium");
        studyProgrammeType.setValue("Nefiltrovat");
        
        //praxe přiřazené vs nepřiřazené
        Label labelAssigned = new Label();
        labelAssigned.setText("Nepřiřazené/již přiřazené:");
        ChoiceBox assigned = new ChoiceBox();
        assigned.getItems().add("Nefiltrovat");
        assigned.getItems().add("Přiřazené");
        assigned.getItems().add("Nepřiřazené");
        assigned.setValue("Nefiltrovat");
        
        labelCompany.setPadding(new Insets(20,0,0,0));
        company.setPrefWidth(200);
        academicYear.setPrefWidth(200);
        studyProgrammeType.setPrefWidth(200);
        assigned.setPrefWidth(200);
        
        // vytvoření tlačítka
        Button filterButton = new Button("Filtrovat");

        // nastylování tlačítek
        filterButton.setStyle(NORMAL_BUTTON_STYLE);
        filterButton.setOnMouseEntered(e -> filterButton.setStyle(HOVERED_BUTTON_STYLE));
        filterButton.setOnMouseExited(e -> filterButton.setStyle(NORMAL_BUTTON_STYLE));
        
        // přidání hodnoty po kliknutí do answer
        filterButton.setOnAction(e -> {
            filter = new PracticesFilter((String)company.getValue(), (String)academicYear.getValue(), (String)studyProgrammeType.getValue(), (String)assigned.getValue());
            window.close();
        });

        VBox layout = new VBox(10);

        // přidání tlačítek
        layout.getChildren().addAll(label, labelCompany, company, labelAcademicYear, academicYear, labelStudyProgrammeType, studyProgrammeType, labelAssigned, assigned, filterButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20,20,20,20));
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return filter;
    }
}
