package utils;

import app.Practice;
import app.Student;
import app.StudyProgramme;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import utils.exceptions.TooManyApplicationsException;
import utils.exceptions.UserDoesntExistException;
import utils.exceptions.WrongPasswordException;

/**
 *
 * @author lukaskorinek
 */
public class StudentDataAccessor {
    private Connection connection;
    
    public StudentDataAccessor() throws SQLException, ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://147.230.21.34;databaseName=DBS2019_LukasKorinek_semestral;user=student;password=student";
        connection = DriverManager.getConnection(url);
    }
    
    public void saveStudent(Student student) throws SQLException, ClassNotFoundException{
        String insertStudentSql = "INSERT INTO Student VALUES (?, ?, ?, ?, ?, ENCRYPTBYPASSPHRASE('Security', ?))";
        PreparedStatement preparedStatement = connection.prepareStatement(insertStudentSql);
        preparedStatement.setString(1, student.getPersonalNumber());
        preparedStatement.setString(2, student.getName());
        preparedStatement.setString(3, student.getLastName());
        preparedStatement.setString(4, student.getEmail());
        preparedStatement.setString(5, student.getUsername());
        preparedStatement.setString(6, student.getPassword());
        preparedStatement.executeUpdate();
        for (int i = 0; i < student.getStudyProgrammes().size(); i++) {
            String insertStudyProgrammes = "INSERT INTO studuje VALUES (?, ?)";
            PreparedStatement preparedStatementProgrammes = connection.prepareStatement(insertStudyProgrammes);
            preparedStatementProgrammes.setString(1, student.getPersonalNumber());
            preparedStatementProgrammes.setInt(2, DatabaseAccessor.findStudyProgrammeIdByName(student.getStudyProgrammes().get(i).getName()));
            preparedStatementProgrammes.executeUpdate();  
        }
    }
    
    public Student getStudent(String username, String password) throws SQLException, WrongPasswordException, ClassNotFoundException, UserDoesntExistException{
        String selectSql = "SELECT Student.Os_cislo, Student.Jmeno, Student.Prijmeni, Student.Email, CONVERT(NVARCHAR, DECRYPTBYPASSPHRASE('Security', Student.password)) AS password, Obor.nazev, je_přiřazen.Praxe_ID\n" +
        "FROM ((Student JOIN studuje ON Student.Os_cislo = studuje.Os_cislo) JOIN Obor ON studuje.Obor_ID = Obor.Obor_ID) LEFT JOIN je_přiřazen ON je_přiřazen.Os_cislo = Student.Os_cislo\n" +
        "WHERE Student.username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();
        String personalNumber = null;
        String name = null;
        String lastName = null;
        String email = null;
        ArrayList<StudyProgramme> studyProgrammes = new ArrayList();
        ArrayList<Practice> acceptedPractices = new ArrayList<>();
        
        while(rs.next()){
            personalNumber = rs.getString("Os_cislo");
            name = rs.getString("Jmeno");
            lastName = rs.getString("Prijmeni");
            email = rs.getString("Email");
            String passwordInDatabase = rs.getString("password");
            if(!(passwordInDatabase.equals(password))){
                throw new WrongPasswordException();
            }
            studyProgrammes.add(new StudyProgramme(rs.getString("nazev")));
            int practiceID = rs.getInt("Praxe_ID");
            if(practiceID != 0){
                acceptedPractices.add(DatabaseAccessor.findPracticeById(practiceID));
            }
        }
        if(personalNumber != null && name != null && lastName != null && email != null && !studyProgrammes.isEmpty()){
            return Student.getStudentInstance(username, password, personalNumber, name, lastName, email, studyProgrammes, acceptedPractices);    
        }else{
            throw new UserDoesntExistException();
        }
    }
    
    public void updateStudent(Student student, String oldPersonalNumber) throws SQLException, ClassNotFoundException{
        String updateStudentSql = "UPDATE Student SET Os_cislo = ?, Jmeno = ?, Prijmeni = ?, Email = ?, username = ?, password = ENCRYPTBYPASSPHRASE('Security', ?) WHERE Os_cislo = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateStudentSql);
        preparedStatement.setString(1, student.getPersonalNumber());
        preparedStatement.setString(2, student.getName());
        preparedStatement.setString(3, student.getLastName());
        preparedStatement.setString(4, student.getEmail());
        preparedStatement.setString(5, student.getUsername());
        preparedStatement.setString(6, student.getPassword());
        preparedStatement.setString(7, oldPersonalNumber);
        preparedStatement.executeUpdate();
        //vymazání oborů
        String deleteStudyProgrammesSql = "DELETE FROM studuje WHERE Os_cislo = ?";
        PreparedStatement preparedStatementDelete = connection.prepareStatement(deleteStudyProgrammesSql);
        preparedStatementDelete.setString(1, student.getPersonalNumber());
        preparedStatementDelete.executeUpdate();
        //přidání nových oborů
        for (int i = 0; i < student.getStudyProgrammes().size(); i++) {
            String insertStudyProgrammesSql = "INSERT INTO studuje VALUES (?, ?)";
            PreparedStatement preparedStatementProgrammes = connection.prepareStatement(insertStudyProgrammesSql);
            preparedStatementProgrammes.setString(1, student.getPersonalNumber());
            preparedStatementProgrammes.setInt(2, DatabaseAccessor.findStudyProgrammeIdByName(student.getStudyProgrammes().get(i).getName()));
            preparedStatementProgrammes.executeUpdate();  
        }
    }
    
    public void applyForPractice(Student student, Practice practice, StudyProgramme studyProgramme, LocalDateTime dateTime) throws SQLException, TooManyApplicationsException, ClassNotFoundException{
        int studyProgrammeID = DatabaseAccessor.findStudyProgrammeIdByName(studyProgramme.getName());
        //zkontrolování počtu žádostí
        String selectFromApplication = "SELECT * FROM žádá WHERE Os_cislo = ? AND Obor_ID = ?";
        PreparedStatement preparedStatementSelect = connection.prepareStatement(selectFromApplication);
        preparedStatementSelect.setString(1, student.getPersonalNumber());
        preparedStatementSelect.setInt(2, studyProgrammeID);
        ResultSet rs = preparedStatementSelect.executeQuery();
        int counter = 0;
        while(rs.next()){
            String personalNumber = rs.getString("Os_Cislo");
            int practiceID = rs.getInt("Praxe_ID");
            int studyProgrammeIDinDatabase = rs.getInt("Obor_ID");
            String date = rs.getString("datum");
            LocalDateTime createdDateTime = LocalDateTime.parse(date.substring(0, 19), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", new Locale("en")));
            // pokud jde o rok, kdy jsou ty praxe - nezáleží na datumu žádosti o praxi            
            if(DatabaseAccessor.findPracticeById(practiceID).getAcademicYear().equals(practice.getAcademicYear())){
                counter++;
            }
            //
            
            /*
            // pokud záleží na datumu - může podat 3 žádosti v (školním?) jednom roce
            int applicationYear = dateTime.getYear();
                //začatek školního roku - třeba 1.9.20XX 00:00
            LocalDateTime academyYearStart = LocalDateTime.of(applicationYear, 9, 1, 0, 0);
                //konec školního roku - třeba 1.7.20XX 00:00
            LocalDateTime academyYearEnd = LocalDateTime.of(applicationYear, 7, 1, 0, 0);
            if(createdDateTime.isAfter(academyYearStart) && createdDateTime.isBefore(academyYearEnd)){
                counter++;
            }
            //
            */
        }
        if(counter >= 3){
            throw new TooManyApplicationsException();
        }
        String insertApplication = "INSERT INTO žádá VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertApplication);
        preparedStatement.setString(1, student.getPersonalNumber());
        preparedStatement.setInt(2, practice.getNumber());
        preparedStatement.setInt(3, studyProgrammeID);
        String dateFormated = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd hh:mm:ss a", new Locale("en")));
        preparedStatement.setString(4, dateFormated);
        preparedStatement.executeUpdate();
    }
    
    public void deleteApplication(String studentNumber, Practice practice, StudyProgramme studyProgramme) throws SQLException, ClassNotFoundException {
        String deleteApplication = "DELETE FROM žádá WHERE Os_cislo = ? AND Praxe_ID = ? AND Obor_ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteApplication);
        preparedStatement.setString(1, studentNumber);
        preparedStatement.setInt(2, practice.getNumber());
        preparedStatement.setInt(3, DatabaseAccessor.findStudyProgrammeIdByName(studyProgramme.getName()));
        preparedStatement.executeUpdate();
    }
    
    public void updateReportFromPractice(String report, Practice practice) throws SQLException{
        String update = "UPDATE je_přiřazen SET Pisemna_zprava = ? WHERE Praxe_ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(update);
        preparedStatement.setString(1, report);
        preparedStatement.setInt(2, practice.getNumber());
        preparedStatement.executeUpdate();
    }
}