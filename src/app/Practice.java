package app;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import utils.exceptions.BlankDataException;
import utils.DatabaseAccessor;
import utils.PracticeDataAccessor;

/**
 *
 * @author lukaskorinek
 */
public class Practice {
    
    private int number;
    private String description;
    private Company company;
    private String academicYear;
    private LocalDateTime dateTime;
    private ArrayList<StudyProgramme> forStudyProgrammes;
    private String assignedStudentNumber;
    private StudyProgramme assignedStudentStudyProgramme;
    private String writtenReportFromStudent;
    private PracticeDataAccessor practiceDataAccessor;
    
    // konstruktor se zapsáním do databáze
    public Practice(String description, Company company, String academicYear, LocalDateTime dateTime, ArrayList<StudyProgramme> forStudyProgrammes) throws SQLException, ClassNotFoundException, BlankDataException {
        validateData(description, academicYear, dateTime, forStudyProgrammes);
        this.description = description;
        this.company = company;
        this.academicYear = academicYear;
        this.dateTime = dateTime;
        this.forStudyProgrammes = forStudyProgrammes;
        //number je ID z databáze
        //vložení do databáze
        this.practiceDataAccessor = new PracticeDataAccessor();
        this.number = practiceDataAccessor.savePractice(this);
    }

    // konstruktor bez zapisování do databáze - pro předávání praxe
    public Practice(int number, String description, Company company, String academicYear, LocalDateTime dateTime, ArrayList<StudyProgramme> forStudyProgrammes, String assignedStudentNumber, StudyProgramme assignedStudentStudyProgramme, String writtenReportFromStudent) throws SQLException, ClassNotFoundException {
        this.number = number;
        this.description = description;
        this.company = company;
        this.academicYear = academicYear;
        this.dateTime = dateTime;
        this.forStudyProgrammes = forStudyProgrammes;
        this.assignedStudentNumber = assignedStudentNumber;
        this.assignedStudentStudyProgramme = assignedStudentStudyProgramme;
        this.writtenReportFromStudent = writtenReportFromStudent;
        this.practiceDataAccessor = new PracticeDataAccessor();
    }
    
    public void assignStudent(String assignedStudentNumber, StudyProgramme assignedStudentStudyProgramme){
        this.assignedStudentNumber = assignedStudentNumber;
        this.assignedStudentStudyProgramme = assignedStudentStudyProgramme;
    }
    
    public Practice addWrittenReportFromStudent(String writtenReportFromStudent) {
        this.writtenReportFromStudent = writtenReportFromStudent;
        return this;
    }

    public int getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }
    
    public String getNumberAndDescription(){
        String formatedPracticeNumber = String.format("%02d", this.number);
        return "#" + formatedPracticeNumber + " " + this.description;
    }

    public ArrayList<StudyProgramme> getTypesOfAllowedStudyProgrammes() {
        return forStudyProgrammes;
    }

    public Company getCompany() {
        return company;
    }

    public String getAssignedStudentNumber() {
        return assignedStudentNumber;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public String getWrittenReportFromStudent() {
        return writtenReportFromStudent;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public StudyProgramme getAssignedStudentStudyProgramme() {
        return assignedStudentStudyProgramme;
    }
    
    public void editPractice(String description, String academicYear, ArrayList<StudyProgramme> forStudyProgrammes) throws BlankDataException, SQLException, ClassNotFoundException{
        validateData(description, academicYear, forStudyProgrammes);
        practiceDataAccessor.editPractice(this);
        this.description = description;
        this.academicYear = academicYear;
        this.forStudyProgrammes = forStudyProgrammes;
    }
    
    public void deletePractice() throws SQLException{
        practiceDataAccessor.deletePractice(this);
    }
    
    // validace dat při vytváření nebo upravování praxe
    private void validateData(String description, String academicYear, LocalDateTime dateTime, ArrayList<StudyProgramme> forStudyProgrammes) throws BlankDataException{
        if(description.equalsIgnoreCase("") || academicYear.equalsIgnoreCase("") || 
                forStudyProgrammes.isEmpty() || dateTime == null){
            throw new BlankDataException();
        }
    }
    
    // validace dat při vytváření nebo upravování praxe
    private void validateData(String description, String academicYear, ArrayList<StudyProgramme> forStudyProgrammes) throws BlankDataException{
        if(description.equalsIgnoreCase("") || academicYear.equalsIgnoreCase("") || 
                forStudyProgrammes.isEmpty()){
            throw new BlankDataException();
        }
    }
    
    public ArrayList<Student> getAllAppliedStudents() throws SQLException, ClassNotFoundException {
        return DatabaseAccessor.getAllAppliedStudents(this.number);
    }
    
    // statická metod, vrací všechny praxe -  pro vypsání všech praxí
    public static ArrayList<Practice> getAllPractices() throws SQLException, ClassNotFoundException {
        return DatabaseAccessor.getAllPractices();
    }
}