package utils;

import app.Company;
import app.Practice;
import app.Student;
import app.StudyProgramme;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

/**
 *
 * @author lukaskorinek
 */
// třída se statickými metodami pro ulehčení práce s databází
public class DatabaseAccessor {
    private static Connection connection;
    
    private static void setConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://147.230.21.34;databaseName=DBS2019_LukasKorinek_semestral;user=student;password=student";
        connection = DriverManager.getConnection(url);
    }
    
    public static ArrayList<Student> getAllStudents() throws SQLException, ClassNotFoundException {
        setConnection();
        
        ArrayList<Student> allStudents = new ArrayList<>();
        String selectSql = "SELECT Student.Os_cislo, Student.Jmeno, Student.Prijmeni, Student.Email, Student.username, Student.password, Obor.nazev, je_přiřazen.Praxe_ID\n" +
        "FROM ((Student JOIN studuje ON Student.Os_cislo = studuje.Os_cislo) JOIN Obor ON studuje.Obor_ID = Obor.Obor_ID) LEFT JOIN je_přiřazen ON je_přiřazen.Os_cislo = Student.Os_cislo";
        PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
        ResultSet rs = preparedStatement.executeQuery();
        String personalNumber = null;
        String name = null;
        String lastName = null;
        String email = null;
        String username = null;
        String password = null;
        ArrayList<StudyProgramme> studyProgrammes = new ArrayList<>();
        ArrayList<Practice> acceptedPractices = new ArrayList<>();
        
        String lastPersonalNumber = "-1";
        while(rs.next()){
            personalNumber = rs.getString("Os_cislo");
            if(!lastPersonalNumber.equals(personalNumber)){
                if(!lastPersonalNumber.equals("-1")){
                    allStudents.add(Student.getStudentInstance(username, password, lastPersonalNumber, name, lastName, email, studyProgrammes, acceptedPractices));
                }
                studyProgrammes.clear();
                acceptedPractices.clear();
                
                name = rs.getString("Jmeno");
                lastName = rs.getString("Prijmeni");
                email = rs.getString("Email");
                username = rs.getString("username");
                password = rs.getString("password");
                studyProgrammes.add(new StudyProgramme(rs.getString("nazev")));
                int practiceID = rs.getInt("Praxe_ID");
                if(practiceID != 0){
                    acceptedPractices.add(findPracticeById(practiceID));
                }
                lastPersonalNumber = personalNumber;
            }else{
                studyProgrammes.add(new StudyProgramme(rs.getString("nazev")));
                int practiceID = rs.getInt("Praxe_ID");
                if(practiceID != 0){
                    acceptedPractices.add(findPracticeById(practiceID));
                }
            }
        }
        if(!lastPersonalNumber.equals("-1") && personalNumber != null && name != null && lastName != null && email != null && !studyProgrammes.isEmpty()){
            allStudents.add(Student.getStudentInstance(username, password, lastPersonalNumber, name, lastName, email, studyProgrammes, acceptedPractices));    
        }
        return allStudents;
    }
    
    public static ArrayList<Company> getAllCompanies() throws SQLException, ClassNotFoundException{
        setConnection();
        
        ArrayList<Company> companies = new ArrayList<>();
        String selectCompanies = "SELECT * FROM Firma";
        PreparedStatement preparedStatementSelect = connection.prepareStatement(selectCompanies);
        ResultSet rs = preparedStatementSelect.executeQuery();
        while(rs.next()){
            int id = rs.getInt("Firma_ID");
            String name = rs.getString("Název");
            String address = rs.getString("Adresa");
            String state = rs.getString("Stát");
            String contactPersonName = rs.getString("Kontakt_jmeno");
            String contactPersonLastname = rs.getString("Kontakt_prijmeni");
            String contactPersonEmail = rs.getString("Kontakt_email");
            String username = rs.getString("username");
            String password = rs.getString("password");
            companies.add(Company.getCompanyInstance(username, password, id, name, address, state, contactPersonName, contactPersonLastname, contactPersonEmail));
        }
        return companies;
    }
    
    public static ArrayList<Practice> getAllPractices() throws SQLException, ClassNotFoundException{
        setConnection();
        
        ArrayList<Practice> practices = new ArrayList<>();
        String selectAllPractices = "SELECT * FROM Praxe";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(selectAllPractices);
        while(rs.next()){
            int number = rs.getInt("Praxe_ID");
            String description = rs.getString("Popis");
            String academicYear = rs.getString("Ak_Rok");
            String created = rs.getString("Vytvoreno");
            LocalDateTime dateTime = LocalDateTime.parse(created.substring(0, 19), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", new Locale("en")));
            int companyID = rs.getInt("Firma_ID");
            Company company = findCompanyById(companyID);
            
            ArrayList<StudyProgramme> studyProgrammes = new ArrayList<>();
            
            String selectStudyProgrammes = "SELECT Obor_ID FROM náleží WHERE Praxe_ID = ?";
            PreparedStatement preparedStatementSP = connection.prepareStatement(selectStudyProgrammes);
            preparedStatementSP.setInt(1, number);
            ResultSet rsSP = preparedStatementSP.executeQuery();
            while(rsSP.next()){
                int oborID = rsSP.getInt("Obor_ID");
                studyProgrammes.add(new StudyProgramme(findStudyProgrammeNameById(oborID)));
            }
            
            String assignedStudentNumber = null;
            StudyProgramme assignedStudentStudyProgramme = null;
            String writtenReportFromStudent = null;
            String selectAssigned = "SELECT * FROM je_přiřazen WHERE Praxe_ID = ?";
            PreparedStatement preparedStatementAssigned = connection.prepareStatement(selectAssigned);
            preparedStatementAssigned.setInt(1, number);
            ResultSet rsAssigned = preparedStatementAssigned.executeQuery();
            while(rsAssigned.next()){
                assignedStudentNumber = rsAssigned.getString("Os_cislo");
                assignedStudentStudyProgramme = new StudyProgramme(findStudyProgrammeNameById(rsAssigned.getInt("Obor_ID")));
                writtenReportFromStudent = rsAssigned.getString("Pisemna_zprava");
            }
            practices.add(new Practice(number, description, company, academicYear, dateTime, studyProgrammes, assignedStudentNumber, assignedStudentStudyProgramme, writtenReportFromStudent));
        }
        return practices;
    }
    
    public static ArrayList<StudyProgramme> getAllStudyProgrammes() throws SQLException, ClassNotFoundException{
        setConnection();
        
        ArrayList<StudyProgramme> studyProgrammes = new ArrayList<>();
        String selectAllStudyProgrammes = "SELECT nazev FROM Obor";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(selectAllStudyProgrammes);
        int id;
        String name;
        while(rs.next()){
            name = rs.getString("nazev");
            studyProgrammes.add(new StudyProgramme(name));
        }
        return studyProgrammes;
    }

    public static Student findStudentByNumber(String personalNumber) throws SQLException, ClassNotFoundException{
        setConnection();
        
        String selectSql = "SELECT Student.Jmeno, Student.Prijmeni, Student.Email, Student.username, Student.password, Obor.nazev, je_přiřazen.Praxe_ID\n" +
        "FROM ((Student JOIN studuje ON Student.Os_cislo = studuje.Os_cislo) JOIN Obor ON studuje.Obor_ID = Obor.Obor_ID) LEFT JOIN je_přiřazen ON je_přiřazen.Os_cislo = Student.Os_cislo\n" +
        "WHERE Student.Os_cislo = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
        preparedStatement.setString(1, personalNumber);
        ResultSet rs = preparedStatement.executeQuery();
        String name = null;
        String lastName = null;
        String email = null;
        String username = null;
        String password = null;
        ArrayList<StudyProgramme> studyProgrammes = new ArrayList();
        ArrayList<Practice> acceptedPractices = new ArrayList<>();
        
        while(rs.next()){
            name = rs.getString("Jmeno");
            lastName = rs.getString("Prijmeni");
            email = rs.getString("Email");
            username = rs.getString("username");
            password = rs.getString("password");
            studyProgrammes.add(new StudyProgramme(rs.getString("nazev")));
            int practiceID = rs.getInt("Praxe_ID");
            if(practiceID != 0){
                acceptedPractices.add(findPracticeById(practiceID));
            }
        }
        if(personalNumber != null && name != null && lastName != null && email != null && !studyProgrammes.isEmpty()){
            return Student.getStudentInstance(username, password, personalNumber, name, lastName, email, studyProgrammes, acceptedPractices);    
        }else{
            return null;
        }
    }
    
    public static Company findCompanyById(int id) throws SQLException, ClassNotFoundException{
        setConnection();
        
        String selectCompany = "SELECT * FROM Firma WHERE Firma_ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectCompany);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            int companyID = rs.getInt("Firma_ID");
            String name = rs.getString("Název");
            String address = rs.getString("Adresa");
            String state = rs.getString("Stát");
            String contactPersonName = rs.getString("Kontakt_jmeno");
            String contactPersonSurname = rs.getString("Kontakt_prijmeni");
            String contactPersonEmail = rs.getString("Kontakt_email");
            String username = rs.getString("username");
            String password = rs.getString("password");
            return Company.getCompanyInstance(username, password, id, name, address, state, contactPersonName, contactPersonSurname, contactPersonEmail);
        }
        return null;
    }
    
    public static int findStudyProgrammeIdByName(String studyProgrammeName) throws SQLException, ClassNotFoundException{
        setConnection();
        
        String selectStudyProgrammeID = "SELECT Obor_ID FROM Obor WHERE nazev = ?";
            PreparedStatement preparedStatementSP = connection.prepareStatement(selectStudyProgrammeID);
            preparedStatementSP.setString(1, studyProgrammeName);
            ResultSet rsStudyProgrammeID = preparedStatementSP.executeQuery();
            if(rsStudyProgrammeID.next()){
                return rsStudyProgrammeID.getInt("Obor_ID");
            }else{
                throw new SQLException();
            }
    }
    
    public static String findStudyProgrammeNameById(int id) throws SQLException, ClassNotFoundException{
        setConnection();
        
        String selectProgrammes = "SELECT nazev FROM Obor WHERE Obor_ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectProgrammes);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            return rs.getString("nazev");
        }
        return null;
    }

    public static Practice findPracticeById(int id) throws SQLException, ClassNotFoundException{
        setConnection();
        
        String selectPractices = "SELECT * FROM Praxe WHERE Praxe_ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectPractices);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            int number = rs.getInt("Praxe_ID");
            String description = rs.getString("Popis");
            String academicYear = rs.getString("Ak_Rok");
            String created = rs.getString("Vytvoreno");
            LocalDateTime dateTime = LocalDateTime.parse(created.substring(0, 19), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", new Locale("en")));
            int companyID = rs.getInt("Firma_ID");
            Company company = findCompanyById(companyID);
            ArrayList<StudyProgramme> studyProgrammes = new ArrayList<>();
            String selectStudyProgrammes = "SELECT Obor_ID FROM náleží WHERE Praxe_ID = ?";
            PreparedStatement preparedStatementSP = connection.prepareStatement(selectStudyProgrammes);
            preparedStatementSP.setInt(1, number);
            ResultSet rsSP = preparedStatementSP.executeQuery();
            while(rsSP.next()){
                int oborID = rsSP.getInt("Obor_ID");
                studyProgrammes.add(new StudyProgramme(findStudyProgrammeNameById(oborID)));
            }
            String assignedStudentNumber = null;
            StudyProgramme assignedStudentStudyProgramme = null;
            String writtenReportFromStudent = null;
            String selectAssigned = "SELECT * FROM je_přiřazen WHERE Praxe_ID = ?";
            PreparedStatement preparedStatementAssigned = connection.prepareStatement(selectAssigned);
            preparedStatementAssigned.setInt(1, number);
            ResultSet rsAssigned = preparedStatementAssigned.executeQuery();
            while(rsAssigned.next()){
                assignedStudentNumber = rsAssigned.getString("Os_cislo");
                assignedStudentStudyProgramme = new StudyProgramme(findStudyProgrammeNameById(rsAssigned.getInt("Obor_ID")));
                writtenReportFromStudent = rsAssigned.getString("Pisemna_zprava");
            }
            return (new Practice(number, description, company, academicYear, dateTime, studyProgrammes, assignedStudentNumber, assignedStudentStudyProgramme, writtenReportFromStudent));
        }
        return null;
    }
    
    public static ArrayList<Practice> findPracticesByCompany(int id) throws SQLException, ClassNotFoundException{
        setConnection();
        
        ArrayList<Practice> practices = new ArrayList<>();
        String selectPractices = "SELECT * FROM Praxe WHERE Firma_ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectPractices);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            int number = rs.getInt("Praxe_ID");
            String description = rs.getString("Popis");
            String academicYear = rs.getString("Ak_Rok");
            String created = rs.getString("Vytvoreno");
            LocalDateTime dateTime = LocalDateTime.parse(created.substring(0, 19), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", new Locale("en")));
            int companyID = rs.getInt("Firma_ID");
            Company company = findCompanyById(id);
            
            ArrayList<StudyProgramme> studyProgrammes = new ArrayList<>();
            
            String selectStudyProgrammes = "SELECT Obor_ID FROM náleží WHERE Praxe_ID = ?";
            PreparedStatement preparedStatementSP = connection.prepareStatement(selectStudyProgrammes);
            preparedStatementSP.setInt(1, number);
            ResultSet rsSP = preparedStatementSP.executeQuery();
            while(rsSP.next()){
                int oborID = rsSP.getInt("Obor_ID");
                studyProgrammes.add(new StudyProgramme(findStudyProgrammeNameById(oborID)));
            }
            
            String assignedStudentNumber = null;
            StudyProgramme assignedStudentStudyProgramme = null;
            String writtenReportFromStudent = null;
            String selectAssigned = "SELECT * FROM je_přiřazen WHERE Praxe_ID = ?";
            PreparedStatement preparedStatementAssigned = connection.prepareStatement(selectAssigned);
            preparedStatementAssigned.setInt(1, number);
            ResultSet rsAssigned = preparedStatementAssigned.executeQuery();
            while(rsAssigned.next()){
                assignedStudentNumber = rsAssigned.getString("Os_cislo");
                assignedStudentStudyProgramme = new StudyProgramme(findStudyProgrammeNameById(rsAssigned.getInt("Obor_ID")));
                writtenReportFromStudent = rsAssigned.getString("Pisemna_zprava");
            }
            practices.add(new Practice(number, description, company, academicYear, dateTime, studyProgrammes, assignedStudentNumber, assignedStudentStudyProgramme, writtenReportFromStudent));
        }
        return practices;
    }
    
    public static String getStudentNameByNumber(String number) throws SQLException, ClassNotFoundException{
        setConnection();
        
        String select = "SELECT Jmeno, Prijmeni FROM Student WHERE Os_cislo = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(select);
        preparedStatement.setString(1, number);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            return rs.getString("Jmeno") + " " + rs.getString("Prijmeni");
        }
        return null;
    }
    
    public static ArrayList<String> getApplicationForPractices(String studentNumber, StudyProgramme studyProgramme) throws SQLException, ClassNotFoundException{
        setConnection();
        
        ArrayList<String> application = new ArrayList<>();
        int studyProgrammeID = findStudyProgrammeIdByName(studyProgramme.getName());
        String selectApplication = "SELECT Praxe_ID FROM žádá WHERE Os_cislo = ? AND Obor_ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectApplication);
        preparedStatement.setString(1, studentNumber);
        preparedStatement.setInt(2, studyProgrammeID);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            int practiceID = rs.getInt("Praxe_ID");
            application.add(findPracticeById(practiceID).getNumberAndDescription());
        }
        return application;
    }
    
    public static ArrayList<Student> getAllAppliedStudents(int practiceId) throws SQLException, ClassNotFoundException{
        setConnection();
        
        ArrayList<Student> appliedStudents = new ArrayList<>();
        String selectFromApplication = "SELECT * FROM žádá WHERE Praxe_ID = ?";
        PreparedStatement preparedStatementSelect = connection.prepareStatement(selectFromApplication);
        preparedStatementSelect.setInt(1, practiceId);
        ResultSet rs = preparedStatementSelect.executeQuery();
        while(rs.next()){
            String personalNumber = rs.getString("Os_Cislo");
            appliedStudents.add(findStudentByNumber(personalNumber));
        }
        return appliedStudents;
    }
    
    public static ArrayList<Practice> getFilteredPractices(PracticesFilter filter) throws SQLException, ClassNotFoundException {
        setConnection();
        
        ArrayList<Practice> filteredPractices = new ArrayList<>();

        // vrácení všech pokud nezadán žádný filtr
        if(filter.getCompany().equals("Nefiltrovat") && filter.getAcademicYear().equals("Nefiltrovat") && filter.getStudyProgrammeType().equals("Nefiltrovat") && filter.getIsAssigned().equals("Nefiltrovat")){
            return getAllPractices();
        }
        // přiřazení hodnot WHERE klauzulím - když je "Nefiltrovat" tak použití injection, aby to na tuto podmínku nebralo ohled
        String choiceCompanyName;
        String choiceAcademicYear;
        String choiceStudyProgrammeType = null;
        String choiceIsAssigned = null;
        //company
        if(filter.getCompany().equals("Nefiltrovat")){
            choiceCompanyName = "'Název firmy' OR 1=1";
        } else {
            choiceCompanyName = "'" + filter.getCompany() + "'";
        }
        //academicYear
        if(filter.getAcademicYear().equals("Nefiltrovat")){
            choiceAcademicYear = "'Akademický rok' OR 1=1";
        } else {
            choiceAcademicYear = "'" + filter.getAcademicYear() + "'";
        }
        //studyProgrammeType
        if(filter.getStudyProgrammeType().equals("Nefiltrovat")){
            choiceStudyProgrammeType = "= 'Česká Republika' OR 1=1";
        } else {
            if(filter.getStudyProgrammeType().equals("Praxe pro bakalářské studium")){
                choiceStudyProgrammeType = "= 'Česká Republika'";
            }else if(filter.getStudyProgrammeType().equals("Praxe pro navazující studium")){
                choiceStudyProgrammeType = "!= 'Česká Republika'";
            }
        }
        //isAssigned
        if(filter.getIsAssigned().equals("Nefiltrovat")){
            choiceIsAssigned = "= 'Přiřazen/Nepřiřazen' OR 1=1";
        } else {
            if(filter.getIsAssigned().equals("Nepřiřazené")){
                choiceIsAssigned = "IS NULL";
            }else if(filter.getIsAssigned().equals("Přiřazené")){
                choiceIsAssigned = "IS NOT NULL";
            }
        }
        // SQL statement
        String select = "SELECT Praxe.Praxe_ID, Praxe.Popis, Praxe.Firma_ID , Praxe.AK_Rok, Praxe.Vytvoreno, Obor.nazev, je_přiřazen.Os_cislo, je_přiřazen.Obor_ID, je_přiřazen.Pisemna_zprava\n" +
        "FROM (((Praxe LEFT JOIN je_přiřazen ON je_přiřazen.Praxe_ID = Praxe.Praxe_ID) JOIN náleží ON náleží.Praxe_ID = Praxe.Praxe_ID) JOIN Obor ON náleží.Obor_ID = Obor.Obor_ID) JOIN Firma ON Firma.Firma_ID = Praxe.Firma_ID\n" +
        "WHERE (Firma.Název = " + choiceCompanyName + ") AND (Praxe.Ak_Rok = " + choiceAcademicYear + ") AND (Firma.Stát " + choiceStudyProgrammeType + ") AND (je_přiřazen.Os_cislo " + choiceIsAssigned + ")\n" +
        "ORDER BY Praxe.Praxe_ID";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(select);
        int number;
        String description = "";
        Company company = null;
        String academicYear = "";
        LocalDateTime dateTime = null;
        ArrayList<StudyProgramme> forStudyProgrammes = new ArrayList<>();
        String assignedStudentNumber = null;
        StudyProgramme assignedStudentStudyProgramme = null;
        String writtenReportFromStudent = null;
        int lastPracticeID = -1;
        while(rs.next()){
            number = rs.getInt("Praxe_ID");
            if(number != lastPracticeID){
                if(lastPracticeID != -1){
                    filteredPractices.add(new Practice(lastPracticeID, description, company, academicYear, dateTime, forStudyProgrammes, assignedStudentNumber, assignedStudentStudyProgramme, writtenReportFromStudent));
                }
                forStudyProgrammes.clear();
                
                description = rs.getString("Popis");
                company = findCompanyById(rs.getInt("Firma_ID"));
                academicYear = rs.getString("Ak_Rok");
                dateTime = LocalDateTime.parse(rs.getString("Vytvoreno").substring(0, 19), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", new Locale("en")));
                forStudyProgrammes.add(new StudyProgramme(rs.getString("nazev")));
                assignedStudentNumber = rs.getString("Os_cislo");
                assignedStudentStudyProgramme = new StudyProgramme(findStudyProgrammeNameById(rs.getInt("Obor_ID")));
                writtenReportFromStudent = rs.getString("Pisemna_zprava");
                lastPracticeID = number;
            }else{
                forStudyProgrammes.add(new StudyProgramme(rs.getString("nazev")));
            }
        }
        //přidání poslední praxe (nemohla se přidat protože už to nedošlo do tý "po ní"
        if(lastPracticeID != -1){
            filteredPractices.add(new Practice(lastPracticeID, description, company, academicYear, dateTime, forStudyProgrammes, assignedStudentNumber, assignedStudentStudyProgramme, writtenReportFromStudent));
        }
        return filteredPractices;
    }
}
