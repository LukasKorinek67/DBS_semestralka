package app;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import utils.CompanyDataAccessor;
import utils.exceptions.AlreadyHasAssignedPracticeException;
import utils.exceptions.BlankDataException;
import utils.DatabaseAccessor;
import utils.exceptions.InvalidDataLengthException;
import utils.exceptions.InvalidEmailAddressException;
import utils.exceptions.PasswordTooShortException;
import utils.exceptions.PracticeAlreadyAssignedException;
import utils.User;
import utils.exceptions.InvalidStudyProgrammesCombinationException;
import utils.exceptions.UserDoesntExistException;
import utils.exceptions.WrongPasswordException;

/**
 *
 * @author lukaskorinek
 */
public class Company extends User {
    
    private int id;
    private String name;
    private String address;
    private String state;
    private String contactPersonName;
    private String contactPersonLastname;
    private String contactPersonEmail;
    private ArrayList<Practice> publishedPractices = new ArrayList<>();
    private CompanyDataAccessor companyDataAccessor;
    
    // privátní konstruktor pro tovární metodu login
    private Company(String username, String password) throws SQLException, ClassNotFoundException, WrongPasswordException, UserDoesntExistException {
        super(username, password);
        //načtení z databáze zbytek properties
        companyDataAccessor = new CompanyDataAccessor();
        Company company = companyDataAccessor.getCompany(username, password);
        this.id = company.getId();
        this.name = company.getName();
        this.address = company.getAddress();
        this.state = company.getState();
        this.contactPersonName = company.getContactPersonName();
        this.contactPersonLastname = company.getContactPersonLastname();
        this.contactPersonEmail = company.getContactPersonEmail();
    }

    // privátní konstruktor pro tovární metodu createAccount
    private Company(String username, String password, String name, String address, String state, String contactPersonName, String contactPersonLastname, String contactPersonEmail) throws SQLException, ClassNotFoundException {
        super(username, password);
        this.name = name;
        this.address = address;
        this.state = state;
        this.contactPersonName = contactPersonName;
        this.contactPersonLastname = contactPersonLastname;
        this.contactPersonEmail = contactPersonEmail;
        //vložení do databáze
        companyDataAccessor = new CompanyDataAccessor();
        id = companyDataAccessor.saveCompany(this);
    }
    
    // privátní konstruktor pro tovární metodu getInstance - slouží pro předání Company z databáze do prvního login konstruktoru 
    private Company(String username, String password, int id, String name, String address, String state, String contactPersonName, String contactPersonLastname, String contactPersonEmail){
        super(username, password);
        this.id = id;
        this.name = name;
        this.address = address;
        this.state = state;
        this.contactPersonName = contactPersonName;
        this.contactPersonLastname = contactPersonLastname;
        this.contactPersonEmail = contactPersonEmail;
    }
    
    public static Company getCompanyInstance(String username, String password, int id, String name, String address, String state, String contactPersonName, String contactPersonLastname, String contactPersonEmail){
        return new Company(username, password, id, name, address, state, contactPersonName, contactPersonLastname, contactPersonEmail);
    }
    
    public static Company login(String username, String password) throws SQLException, ClassNotFoundException, WrongPasswordException, UserDoesntExistException{
        return new Company(username, password);
    }
    
    public static Company createAccount(String username, String password, String name, String address, String state, String contactPersonName, String contactPersonLastname, String contactPersonEmail) throws SQLException, ClassNotFoundException, BlankDataException, InvalidEmailAddressException, InvalidDataLengthException, PasswordTooShortException{
        validateData(username, password, name, address, state, contactPersonName, contactPersonLastname, contactPersonEmail);
        return new Company(username, password, name, address, state, contactPersonName, contactPersonLastname, contactPersonEmail);
    }
    
    public ArrayList<Practice> getMyPractices() throws SQLException, ClassNotFoundException{
        return DatabaseAccessor.findPracticesByCompany(this.id);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getState() {
        return state;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public String getContactPersonLastname() {
        return contactPersonLastname;
    }

    public String getContactPersonEmail() {
        return contactPersonEmail;
    }
    
    // ověření dat při vytváření nebo upravování Company
    private static void validateData(String username, String password, String name, String address, String state, String contactPersonName, String contactPersonLastname, String contactPersonEmail) throws BlankDataException, InvalidEmailAddressException, InvalidDataLengthException, PasswordTooShortException {
        if(username.equals("") || password.equals("") || name.equals("") || address.equals("") 
                || state.equals("") || contactPersonName.equals("")
                || contactPersonLastname.equals("") || contactPersonEmail.equals("")){
            throw new BlankDataException();
        }
        if(username.length() > 15 || password.length() > 20 || name.length() > 30 || address.length() > 30
                || state.length() > 30 || contactPersonName.length() > 20
                || contactPersonLastname.length() > 20 || contactPersonEmail.length() > 40){
            throw new InvalidDataLengthException();
        }
        if(password.length() < 6){
            throw new PasswordTooShortException();
        }
        final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
                + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b"
                + "\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01"
                + "-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]"
                + "*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25"
                + "[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4]"
                + "[0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x"
                + "0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x"
                + "0b\\x0c\\x0e-\\x7f])+)\\])";
        
        if(!(contactPersonEmail.matches(EMAIL_REGEX))){
            throw new InvalidEmailAddressException();
        }
    }
    
    public void editProfile(String username, String password, String name, String address, String state, String contactPersonName, String contactPersonLastName, String contactPersonEmail) throws BlankDataException, InvalidEmailAddressException, InvalidDataLengthException, PasswordTooShortException, SQLException{
        String oldName = this.name;
        validateData(username, password, name, address, state, contactPersonName, contactPersonLastName, contactPersonEmail);
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.state = state;
        this.contactPersonName = contactPersonName;
        this.contactPersonLastname = contactPersonLastName;
        this.contactPersonEmail = contactPersonEmail;
        // upravení v databázi
        this.companyDataAccessor.updateCompany(this);
    }
    
    public void publishPractice(String academicYear, String description, ArrayList<StudyProgramme> studyProgrammes, LocalDateTime dateTime) throws SQLException, ClassNotFoundException, BlankDataException, InvalidStudyProgrammesCombinationException{
        validateStudyProgrammes(studyProgrammes);
        Practice practice = new Practice(description, this, academicYear, dateTime, studyProgrammes);
        publishedPractices.add(practice);
    }
    
    public Practice editPractice(Practice practice, String description, String academicYear, ArrayList<StudyProgramme> forStudyProgrammes) throws SQLException, BlankDataException, InvalidStudyProgrammesCombinationException, ClassNotFoundException{
        validateStudyProgrammes(forStudyProgrammes);
        practice.editPractice(description, academicYear, forStudyProgrammes);
        return practice;
    }
    
    public void deletePractice(Practice practice) throws SQLException{
        practice.deletePractice();
    }
    
    public void assignStudentToPractice(String studentNumber, Practice practice, LocalDateTime dateTime) throws SQLException, PracticeAlreadyAssignedException, AlreadyHasAssignedPracticeException, ClassNotFoundException{        
        //kontrola jestli už praxe není přiřazená
        if(practice.getAssignedStudentNumber() != null){
            throw new PracticeAlreadyAssignedException();
        }
        StudyProgramme assignedStudentStudyProgramme = companyDataAccessor.assignStudentToPractice(studentNumber, practice, dateTime); 
        practice.assignStudent(studentNumber, assignedStudentStudyProgramme);
    }
    
    public void downloadMessageFromPractice(Practice practice, File pdfFile) throws FileNotFoundException, DocumentException, SQLException, ClassNotFoundException{
        Document document = new Document(); 
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile)); 
        String assignedStudentNumber = practice.getAssignedStudentNumber();
        document.open(); 
        document.add(new Paragraph("Zpráva z praxe " + practice.getNumberAndDescription()));
        document.add(new Paragraph("Student: " + assignedStudentNumber + " - " + Student.getNameByNumber(assignedStudentNumber)));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(practice.getWrittenReportFromStudent()));
        document.close();
    }
    
    //kontrola jestli česká firma nevypisuje pro navazující studium a naopak
    public void validateStudyProgrammes(ArrayList<StudyProgramme> forStudyProgrammes) throws InvalidStudyProgrammesCombinationException{
        if(this.state.equalsIgnoreCase("Česká Republika")){
            for (int i = 0; i < forStudyProgrammes.size(); i++) {
                if(forStudyProgrammes.get(i).getName().contains("navazující")){
                    throw new InvalidStudyProgrammesCombinationException();
                }
            }
        } else{
            for (int i = 0; i < forStudyProgrammes.size(); i++) {
                if(forStudyProgrammes.get(i).getName().contains("bakalářský")){
                    throw new InvalidStudyProgrammesCombinationException();
                }
            }
        }
    }
    
    // statická metod, vrací všechny firmy -  pro vypsání všech firem
    public static ArrayList<Company> getAllCompanies() throws SQLException, ClassNotFoundException {
        DatabaseAccessor databaseAccessor = new DatabaseAccessor();
        return databaseAccessor.getAllCompanies();
    }
    
    @Override
    public String toString() {
        return "Company " + username;
    }
}