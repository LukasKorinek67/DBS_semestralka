package app;

import java.util.ArrayList;
import utils.User;

/**
 *
 * @author lukaskorinek
 */
public class Student extends User {
    
    
    private int personalNumber;
    private String name;
    private String lastName;
    private String email;
    private ArrayList<StudyProgramme> studyProgrammes;
    // použít Trigger na omezení toho, že student může zažádat max o 3 praxe
    private ArrayList<Practice> acceptedPractices;

    private Student(String username, String password) {       
        super(username, password);
        //načíst z databáze zbytek properties
    }

    private Student(String username, String password, int personalNumber, String name, String lastName, String email, ArrayList<StudyProgramme> studyProgrammes) {
        super(username, password);
        this.personalNumber = personalNumber;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.studyProgrammes = studyProgrammes;
    }
    
    public static Student login(String username, String password){
        return new Student(username, password);
    }
    
    public static Student createAccount(String username, String password, int personalNumber, String name, String lastName, String email, ArrayList<StudyProgramme> studyProgrammes){
        return new Student(username, password, personalNumber, name, lastName, email, studyProgrammes);
    }


    public int getPersonalNumber() {
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
    
    public void editProfile(String username, String password, int personalNumber, String name, String lastName, String email, ArrayList<StudyProgramme> studyProgrammes){
        this.username = username;
        this.password = password;
        this.personalNumber = personalNumber;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.studyProgrammes = studyProgrammes;
        // plus upravit v databázi
    }
    
    public void viewAndFillterPractices(/**/){
        
    }
    
    public void applyForPractice(/**/){
        
    }
    
    public void submitMessageFromPractice(String message/* Předělat na PDF!! */, String practiceDescription){
        for (Practice acceptedPractice : acceptedPractices) {
            if(acceptedPractice.getDescription().equalsIgnoreCase(practiceDescription)){
                acceptedPractice.addWrittenReportFromStudent(message);
            }
        }
        // plus vložit do databáze
    }   
    
    @Override
    public String toString() {
        return "Student " + username;
    }
}
