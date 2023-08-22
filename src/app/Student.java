package app;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import utils.exceptions.BlankDataException;
import utils.DatabaseAccessor;
import utils.PracticesFilter;
import utils.StudentDataAccessor;
import utils.exceptions.InvalidCompanyCountryChoiceException;
import utils.exceptions.InvalidDataLengthException;
import utils.exceptions.InvalidEmailAddressException;
import utils.exceptions.InvalidStudyProgrammesCombinationException;
import utils.exceptions.PasswordTooShortException;
import utils.exceptions.PracticeAlreadyAssignedException;
import utils.exceptions.PracticeIsNotForThisStudyProgrammeException;
import utils.exceptions.TooManyApplicationsException;
import utils.User;
import utils.exceptions.CouldNotReadPDFException;
import utils.exceptions.DidntSelectFileException;
import utils.exceptions.InvalidFileTypeException;
import utils.exceptions.InvalidPersonalNumberException;
import utils.exceptions.UserDoesntExistException;
import utils.exceptions.WrongPasswordException;
import utils.exceptions.WrongPracticeException;
import utils.exceptions.WrongStudyProgrammeException;


/**
 *
 * @author lukaskorinek
 */
public class Student extends User {

    private String personalNumber;
    private String name;
    private String lastName;
    private String email;
    private ArrayList<StudyProgramme> studyProgrammes;
    private ArrayList<Practice> acceptedPractices;
    private StudentDataAccessor studentDataAccessor;

    // privátní konstruktor pro tovární metodu login
    private Student(String username, String password) throws SQLException, ClassNotFoundException, UserDoesntExistException, WrongPasswordException {       
        super(username, password);
        //načtení z databáze zbytek properties
        studentDataAccessor = new StudentDataAccessor();
        Student student = studentDataAccessor.getStudent(username, password);
        this.personalNumber = student.personalNumber;
        this.name = student.name;
        this.lastName = student.lastName;
        this.email = student.getEmail();
        this.studyProgrammes = student.studyProgrammes;
        this.acceptedPractices = student.acceptedPractices;
    }

    // privátní konstruktor pro tovární metodu createAccount
    private Student(String username, String password, String personalNumber, String name, String lastName, String email, ArrayList<StudyProgramme> studyProgrammes) throws SQLException, ClassNotFoundException {
        super(username, password);
        this.personalNumber = personalNumber;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.studyProgrammes = studyProgrammes;
        this.acceptedPractices = new ArrayList<>();
        //vložení do databáze
        studentDataAccessor = new StudentDataAccessor();
        studentDataAccessor.saveStudent(this);
        //databaseAccessor.saveStudent(new Student(this.username, this.password, this.personalNumber, this.name, this.lastName, this.email, this.studyProgrammes));
    }
    
    // privátní konstruktor pro tovární metodu getInstance - slouží pro předání Company z databáze do prvního login konstruktoru
    private Student(String personalNumber, String name, String lastName, String email, ArrayList<StudyProgramme> studyProgrammes, ArrayList<Practice> acceptedPractices, String username, String password){
        super(username, password);
        this.personalNumber = personalNumber;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.studyProgrammes = studyProgrammes;
        this.acceptedPractices = acceptedPractices;
    }
    
    public static Student getStudentInstance(String username, String password, String personalNumber, String name, String lastName, String email, ArrayList<StudyProgramme> studyProgrammes, ArrayList<Practice> acceptedPractices){
        return new Student(personalNumber, name, lastName, email, studyProgrammes, acceptedPractices, username, password);
    }
    
    public static Student login(String username, String password) throws SQLException, ClassNotFoundException, UserDoesntExistException, WrongPasswordException{
        return new Student(username, password);
    }
    
    public static Student createAccount(String username, String password, String personalNumber, String name, String lastName, String email, ArrayList<StudyProgramme> studyProgrammes) throws SQLException, ClassNotFoundException, BlankDataException, InvalidEmailAddressException, InvalidDataLengthException, PasswordTooShortException, InvalidStudyProgrammesCombinationException, InvalidPersonalNumberException{
        validateData(username, password, personalNumber, name, lastName, email, studyProgrammes);
        return new Student(username, password, personalNumber, name, lastName, email, studyProgrammes);
    }

    public String getFullName(){
        return (this.name + " " + this.lastName);
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<StudyProgramme> getStudyProgrammes() {
        return studyProgrammes;
    }

    public ArrayList<Practice> getAcceptedPractices() {
        return acceptedPractices;
    }
    
    // ověření dat při vytváření nebo upravování Company
    private static void validateData(String username, String password, String personalNumber, String name, String lastName, String email, ArrayList<StudyProgramme> studyProgrammes) throws BlankDataException, InvalidEmailAddressException, InvalidDataLengthException, PasswordTooShortException, InvalidStudyProgrammesCombinationException, InvalidPersonalNumberException {
        if(username.equals("") || password.equals("") || personalNumber.equals("")
                || name.equals("") || lastName.equals("") || email.equals("")
                || studyProgrammes.isEmpty()){
            throw new BlankDataException();
        }
        if(username.length() > 15 || password.length() > 20 || personalNumber.length() > 15
                || name.length() > 20 || lastName.length() > 20 || email.length() > 40){
            throw new InvalidDataLengthException();
        }
        if(password.length() < 6){
            throw new PasswordTooShortException();
        }
        if(!(personalNumber.matches("[A-Z][0-9]{8}"))){
            throw new InvalidPersonalNumberException();
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
        
        if(!(email.matches(EMAIL_REGEX))){
            throw new InvalidEmailAddressException();
        }
        
        for (int i = 0; i < studyProgrammes.size(); i++) {
            for (int j = 0; j < studyProgrammes.size(); j++) {
                if(i != j){
                    if((studyProgrammes.get(i).getName().substring(0, 12)).equalsIgnoreCase(studyProgrammes.get(j).getName().substring(0, 12))){
                        throw new InvalidStudyProgrammesCombinationException();
                    }
                }
            }
        }
    }
    
    public void editProfile(String username, String password, String personalNumber, String name, String lastName, String email, ArrayList<StudyProgramme> studyProgrammes) throws BlankDataException, InvalidEmailAddressException, InvalidDataLengthException, PasswordTooShortException, InvalidStudyProgrammesCombinationException, SQLException, InvalidPersonalNumberException, ClassNotFoundException{
        String oldPersonalNumber = this.personalNumber;
        validateData(username, password, personalNumber, name, lastName, email, studyProgrammes);
        this.username = username;
        this.password = password;
        this.personalNumber = personalNumber;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.studyProgrammes = studyProgrammes;
        // upravení v databázi
        studentDataAccessor.updateStudent(this, oldPersonalNumber);
    }
    
    public ArrayList<Practice> viewFilteredPractices(PracticesFilter filter) throws SQLException, ClassNotFoundException{
        return DatabaseAccessor.getFilteredPractices(filter);
    }
    
    public void applyForPractice(Practice practice, StudyProgramme studyProgramme, LocalDateTime dateTime) throws SQLException, TooManyApplicationsException, PracticeIsNotForThisStudyProgrammeException, PracticeAlreadyAssignedException, WrongStudyProgrammeException, InvalidCompanyCountryChoiceException, ClassNotFoundException{
        validateApplyForPracticeData(practice, studyProgramme);
        studentDataAccessor.applyForPractice(this, practice, studyProgramme, dateTime);
    }
    
    public void validateApplyForPracticeData(Practice practice, StudyProgramme studyProgramme) throws PracticeIsNotForThisStudyProgrammeException, PracticeAlreadyAssignedException, WrongStudyProgrammeException, InvalidCompanyCountryChoiceException{
        //zda není praxe již zabraná
        if(practice.getAssignedStudentNumber() != null){
            throw new PracticeAlreadyAssignedException();
        }

        //zda student studuje program, ke kterému si praxi hlásí
        boolean studyThisProgramme = false;
        for (int i = 0; i < this.studyProgrammes.size(); i++) {
            if(studyProgrammes.get(i).getName().equals(studyProgramme.getName())){
                studyThisProgramme = true;
                break;
            }
        }
        if(!studyThisProgramme){
            throw new WrongStudyProgrammeException();
        }
        //zda je praxe vypsána na studijní program, ke kterému si praxi hlásí
        boolean isAlowed = false;
        ArrayList<StudyProgramme> allowedStudyProgramme = practice.getTypesOfAllowedStudyProgrammes();
        for (int j = 0; j < allowedStudyProgramme.size(); j++) {
            if(allowedStudyProgramme.get(j).getName().equals(studyProgramme.getName())){
                isAlowed = true;
                break;
            }
        }
        if(!isAlowed){
            throw new PracticeIsNotForThisStudyProgrammeException();
        }
        //zda je při bakalářském studiu praxe vypsáná českou firmou a při navazujícím zahraniční firmou
        if(practice.getCompany().getState().equalsIgnoreCase("Česká Republika") && studyProgramme.getName().contains("navazující")){
            throw new InvalidCompanyCountryChoiceException();
        }else if(!(practice.getCompany().getState().equalsIgnoreCase("Česká Republika")) && studyProgramme.getName().contains("bakalářský")){
            throw new InvalidCompanyCountryChoiceException();
        }
    }
    
    public ArrayList<String> getApplicationForPractices(StudyProgramme studyProgramme) throws SQLException, ClassNotFoundException {
        return DatabaseAccessor.getApplicationForPractices(this.personalNumber, studyProgramme);
    }
    
    public void deleteApplication(Practice practice, StudyProgramme studyProgramme) throws SQLException, ClassNotFoundException {
        studentDataAccessor.deleteApplication(this.personalNumber, practice, studyProgramme);
    }
    
    // odevzdání písemné zprávy z praxe v podobě textu
    public Practice submitMessageFromPractice(String message, Practice practice) throws WrongPracticeException, SQLException, InvalidDataLengthException{
        if(message.length() >= 500){
            throw new InvalidDataLengthException();
        }
        studentDataAccessor.updateReportFromPractice(message, practice);
        for (Practice acceptedPractice : acceptedPractices) {
            if(acceptedPractice.getNumberAndDescription().equalsIgnoreCase(practice.getNumberAndDescription())){
                return acceptedPractice.addWrittenReportFromStudent(message);
            }
        }
        throw new WrongPracticeException();
    } 
    
    // odevzdání písemné zprávy z praxe v podobě PDF souboru
    public Practice submitMessageFromPracticePDF(File pdfFile, Practice practice) throws InvalidFileTypeException, DidntSelectFileException, IOException, CouldNotReadPDFException, WrongPracticeException, SQLException, InvalidDataLengthException{
        if(pdfFile == null){
            throw new DidntSelectFileException();
        }
        if(!(pdfFile.getName().endsWith(".pdf"))){
            throw new InvalidFileTypeException();
        }
        String message = getPdfContent(pdfFile);
        return submitMessageFromPractice(message, practice);
    }
    
    private String getPdfContent(File pdfFile) throws IOException, CouldNotReadPDFException {
	String text = null;
        PDDocument document = PDDocument.load(pdfFile);
        if (!document.isEncrypted()) {
            PDFTextStripper stripper = new PDFTextStripper();
            text = stripper.getText(document);
        }
        document.close();
        if(text == null){
            throw new CouldNotReadPDFException();
        }else{
            return text;
        }
    }

    // statická metoda pro zjištění jména studenta pomocí jeho čísla (při vypsání přiřazeného studenta k praxi)
    public static String getNameByNumber(String number) throws SQLException, ClassNotFoundException{
        DatabaseAccessor databaseAccessor = new DatabaseAccessor();
        return databaseAccessor.getStudentNameByNumber(number);
    }
    
    @Override
    public String toString() {
        return "Student " + username;
    }
}