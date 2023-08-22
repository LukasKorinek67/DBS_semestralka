package utils;

import app.Practice;
import app.StudyProgramme;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

/**
 *
 * @author lukaskorinek
 */
public class PracticeDataAccessor {
    private Connection connection;
    
    public PracticeDataAccessor() throws SQLException, ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://147.230.21.34;databaseName=DBS2019_LukasKorinek_semestral;user=student;password=student";
        connection = DriverManager.getConnection(url);
    }
    
    public int savePractice(Practice practice) throws SQLException, ClassNotFoundException{
        String insertPractice = "INSERT INTO Praxe VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertPractice, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, practice.getDescription());
        preparedStatement.setString(2, practice.getAcademicYear());
        String dateFormated = practice.getDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd hh:mm:ss a", new Locale("en")));
        preparedStatement.setString(3, dateFormated);
        preparedStatement.setInt(4, practice.getCompany().getId());
        preparedStatement.executeUpdate();
        ResultSet rs = preparedStatement.getGeneratedKeys();
        int practiceID;
        if(rs.next()){
            practiceID = rs.getInt(1);
        }else{
            throw new SQLException();
        }
        ArrayList<StudyProgramme> forStudyProgrammes = practice.getTypesOfAllowedStudyProgrammes();
        for (int i = 0; i < forStudyProgrammes.size(); i++) {
            String insertNalezi = "INSERT INTO náleží VALUES (?, ?)";
            PreparedStatement preparedStatementNalezi = connection.prepareStatement(insertNalezi);
            preparedStatementNalezi.setInt(1, practiceID);
            String studyProgrammeName = forStudyProgrammes.get(i).getName();
            preparedStatementNalezi.setInt(2, DatabaseAccessor.findStudyProgrammeIdByName(studyProgrammeName));
            preparedStatementNalezi.executeUpdate();
        }
        return practiceID;
    }
    
    public void editPractice(Practice practice) throws SQLException, ClassNotFoundException {
        String update = "UPDATE Praxe SET Popis = ?, Ak_Rok = ? WHERE Praxe_ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(update);
        preparedStatement.setString(1, practice.getDescription());
        preparedStatement.setString(2, practice.getAcademicYear());
        preparedStatement.setInt(3, practice.getNumber());
        preparedStatement.executeUpdate();
        
        //vymazání stávajících studijních oborů
        String deleteStudyProgrammes = "DELETE FROM náleží WHERE Praxe_ID = ?";
        PreparedStatement preparedStatementDelete = connection.prepareStatement(deleteStudyProgrammes);
        preparedStatementDelete.setInt(1, practice.getNumber());
        preparedStatementDelete.executeUpdate();
        //přidání nových oborů
        for (int i = 0; i < practice.getTypesOfAllowedStudyProgrammes().size(); i++) {
            String insertStudyProgrammes = "INSERT INTO náleží VALUES (?, ?)";
            PreparedStatement preparedStatementProgrammes = connection.prepareStatement(insertStudyProgrammes);
            preparedStatementProgrammes.setInt(1, practice.getNumber());
            preparedStatementProgrammes.setInt(2, DatabaseAccessor.findStudyProgrammeIdByName(practice.getTypesOfAllowedStudyProgrammes().get(i).getName()));
            preparedStatementProgrammes.executeUpdate();
        }
    }
    
    public void deletePractice(Practice practice) throws SQLException {
        String deletePractice = "DELETE FROM Praxe WHERE Praxe_ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deletePractice);
        preparedStatement.setInt(1, practice.getNumber());
        preparedStatement.executeUpdate();
    }   
}