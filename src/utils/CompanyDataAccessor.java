package utils;

import app.Company;
import app.Practice;
import app.StudyProgramme;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import utils.exceptions.AlreadyHasAssignedPracticeException;
import utils.exceptions.PracticeAlreadyAssignedException;
import utils.exceptions.UserDoesntExistException;
import utils.exceptions.WrongPasswordException;

/**
 *
 * @author lukaskorinek
 */
public class CompanyDataAccessor {
    private Connection connection;
    
    public CompanyDataAccessor() throws SQLException, ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://147.230.21.34;databaseName=DBS2019_LukasKorinek_semestral;user=student;password=student";
        connection = DriverManager.getConnection(url);
    }
    
    public int saveCompany(Company company) throws SQLException{
        String insertCompany = "INSERT INTO Firma VALUES (?, ?, ?, ?, ?, ?, ?, ENCRYPTBYPASSPHRASE('Security', ?))";
        PreparedStatement preparedStatement = connection.prepareStatement(insertCompany, PreparedStatement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, company.getName());
        preparedStatement.setString(2, company.getAddress());
        preparedStatement.setString(3, company.getState());
        preparedStatement.setString(4, company.getContactPersonName());
        preparedStatement.setString(5, company.getContactPersonLastname());
        preparedStatement.setString(6, company.getContactPersonEmail());
        preparedStatement.setString(7, company.getUsername());
        preparedStatement.setString(8, company.getPassword());
        //preparedStatement.setNString(8, company.getPassword());
        preparedStatement.executeUpdate();
        ResultSet rs = preparedStatement.getGeneratedKeys();
        if(rs.next()){
            return rs.getInt(1);
        }else{
            throw new SQLException();
        }
    }
    
    public Company getCompany(String username, String password) throws SQLException, ClassNotFoundException, WrongPasswordException, UserDoesntExistException{
        /*String selectCompany = "SELECT Firma_ID, Název, Adresa, Stát, Kontakt_jmeno, Kontakt_prijmeni, Kontakt_email, CAST(DECRYPTBYPASSPHRASE('Security', Firma.password) AS VARCHAR(20)) AS password"
                + " FROM Firma WHERE username = ?";
        CONVERT(VARCHAR, DECRYPTBYPASSPHRASE('Security', Firma.password))*/
        String selectCompany = "SELECT Firma_ID, Název, Adresa, Stát, Kontakt_jmeno, Kontakt_prijmeni, Kontakt_email, CONVERT(NVARCHAR, DECRYPTBYPASSPHRASE('Security', Firma.password)) AS password"
                + " FROM Firma WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectCompany);
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            int companyID = rs.getInt("Firma_ID");
            String name = rs.getString("Název");
            String address = rs.getString("Adresa");
            String state = rs.getString("Stát");
            String contactPersonName = rs.getString("Kontakt_jmeno");
            String contactPersonSurname = rs.getString("Kontakt_prijmeni");
            String contactPersonEmail = rs.getString("Kontakt_email");
            String passwordInDatabase = rs.getString("password");
            if(!(passwordInDatabase.equals(password))){
                throw new WrongPasswordException();
            }
            return Company.getCompanyInstance(username, password, companyID, name, address, state, contactPersonName, contactPersonSurname, contactPersonEmail);
        }
        throw new UserDoesntExistException();
    }
    
    public void updateCompany(Company company) throws SQLException{
        String updateCompany = "UPDATE Firma SET Název = ?, Adresa = ?, Stát = ?, Kontakt_jmeno = ?, Kontakt_prijmeni = ?, Kontakt_email = ?, username = ?, password = ENCRYPTBYPASSPHRASE('Security', ?) WHERE Firma_ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateCompany);
        preparedStatement.setString(1, company.getName());
        preparedStatement.setString(2, company.getAddress());
        preparedStatement.setString(3, company.getState());
        preparedStatement.setString(4, company.getContactPersonName());
        preparedStatement.setString(5, company.getContactPersonLastname());
        preparedStatement.setString(6, company.getContactPersonEmail());
        preparedStatement.setString(7, company.getUsername());
        preparedStatement.setString(8, company.getPassword());
        preparedStatement.setInt(9, company.getId());
        preparedStatement.executeUpdate();
    }
    
    public StudyProgramme assignStudentToPractice(String studentNumber, Practice practice, LocalDateTime dateTime) throws SQLException, AlreadyHasAssignedPracticeException, PracticeAlreadyAssignedException, ClassNotFoundException{
        String selectFromApplication = "SELECT * FROM žádá WHERE Os_cislo = ? AND Praxe_ID = ?";
        PreparedStatement preparedStatementSelect = connection.prepareStatement(selectFromApplication);
        preparedStatementSelect.setString(1, studentNumber);
        preparedStatementSelect.setInt(2, practice.getNumber());
        ResultSet rs = preparedStatementSelect.executeQuery();
        int studyProgrammeId = -1;
        while(rs.next()){
            studyProgrammeId = rs.getInt("Obor_ID");
        }
        if(studyProgrammeId == -1){
            throw new SQLException();
        }
        //kontrola jestli má student už k oboru nějakou praxi přiřazenou
        String selectAssign = "SELECT * FROM je_přiřazen WHERE Os_cislo = ? AND Obor_ID = ?";
        PreparedStatement preparedStatementAssign = connection.prepareStatement(selectAssign);
        preparedStatementAssign.setString(1, studentNumber);
        preparedStatementAssign.setInt(2, studyProgrammeId);
        ResultSet rsAssign = preparedStatementAssign.executeQuery();
        if(rsAssign.next()){
            throw new AlreadyHasAssignedPracticeException();
        }
        
        String insertStudent = "INSERT INTO je_přiřazen VALUES (?, ?, ?, ?, null)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertStudent);
        preparedStatement.setString(1, studentNumber);
        preparedStatement.setInt(2, practice.getNumber());
        preparedStatement.setInt(3, studyProgrammeId);
        String dateFormated = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd hh:mm:ss a", new Locale("en")));
        preparedStatement.setString(4, dateFormated);
        preparedStatement.executeUpdate();
        
        
        //vymazání všech ostatních studentových žádsotí o jiné praxe ve stejném oboru na stejný akademický rok
        String selectApplication = "SELECT Praxe_ID FROM žádá WHERE Os_cislo = ? AND Obor_ID = ?";
        PreparedStatement preparedStatementSelectDelete = connection.prepareStatement(selectApplication);
        preparedStatementSelectDelete.setString(1, studentNumber);
        preparedStatementSelectDelete.setInt(2, studyProgrammeId);
        ResultSet rsDelete = preparedStatementSelectDelete.executeQuery();
        while(rsDelete.next()){
            int practiceID = rsDelete.getInt("Praxe_ID");
            if(DatabaseAccessor.findPracticeById(practiceID).getAcademicYear().equals(practice.getAcademicYear())) {
                //vymazat
                String deleteApplication = "DELETE FROM žádá WHERE Os_cislo = ? AND Obor_ID = ? AND Praxe_ID = ?";
                PreparedStatement preparedStatementDelete = connection.prepareStatement(deleteApplication);
                preparedStatementDelete.setString(1, studentNumber);
                preparedStatementDelete.setInt(2, studyProgrammeId);
                preparedStatementDelete.setInt(3, practiceID);
                preparedStatementDelete.executeUpdate();
            }
        }
        
        //návrat studijního programu
        return new StudyProgramme(DatabaseAccessor.findStudyProgrammeNameById(studyProgrammeId));
    }  
}