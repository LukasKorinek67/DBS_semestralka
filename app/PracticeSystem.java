package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import utils.User;

/**
 *
 * @author lukaskorinek
 */
public class PracticeSystem {
    private User loggedUser;
    private Map<String, Boolean> studyProgrammes = new HashMap<String, Boolean>();
    //private ArrayList<Practice> listedPractices; - tohle asi bude jen v databázi

    public PracticeSystem() {
        studyProgrammes.put("Aplikované vědy v inženýrství bakalářský", Boolean.FALSE);
        studyProgrammes.put("Aplikované vědy v inženýrství navazující", Boolean.FALSE);
        studyProgrammes.put("Elektrotechnika a informatika bakalářský", Boolean.FALSE);
        studyProgrammes.put("Elektrotechnika a informatika navazující", Boolean.FALSE);
        studyProgrammes.put("Informační technologie bakalářský", Boolean.FALSE);
        studyProgrammes.put("Informační technologie navazující", Boolean.FALSE);
        studyProgrammes.put("Nanotechnologie bakalářský", Boolean.FALSE);
        studyProgrammes.put("Nanotechnologie navazující", Boolean.FALSE);
        studyProgrammes.put("Mechatronics bakalářský", Boolean.FALSE);
        studyProgrammes.put("Mechatronics navazující", Boolean.FALSE);
    }
    
    public void createStudentAccount(String username, String password, int personalNumber, String name, String lastName, String email, Map<String, Boolean> studyProgrammes){
        loggedUser = Student.createAccount(username, password, personalNumber, name, lastName, email, mapToArrayList(studyProgrammes));
    }
    
    public void loginStudent(String username, String password){
        loggedUser = Student.login(username, password);
    }
    
    public void createCompanyAccount(String username, String password, String name, String address, String state, String contactPersonName, String contactPersonLastname, String contactPersonEmail){
        loggedUser = Company.createAccount(username, password, name, address, state, contactPersonName, contactPersonLastname, contactPersonEmail);
    }
    
    public void loginCompany(String username, String password){
        loggedUser = Company.login(username, password);
    }
    
    public String getStudentProfileDetails(){
        StringBuilder builder = new StringBuilder();
        builder.append("Jméno: ").append(((Student) loggedUser).getName()).append(" ").append(((Student) loggedUser).getLastName()).append("\n");
        builder.append("Osobní číslo: ").append(((Student) loggedUser).getPersonalNumber()).append("\n");
        builder.append("Email: ").append(((Student) loggedUser).getEmail()).append("\n");      
        builder.append("Obor/y: ").append("\n");
        ArrayList<StudyProgramme> studyProgrammes = ((Student) loggedUser).getStudyProgrammes();
        for (StudyProgramme studyProgramme : studyProgrammes) {
            builder.append(studyProgramme.getName()).append("\n");
        }
        builder.append("Uživatelské jméno: ").append(((Student) loggedUser).getUsername()).append("\n");
        builder.append("Heslo: ").append(((Student) loggedUser).getPassword()).append("\n");
        String data = builder.toString();
        return data;
    }
    
    public void editStudentProfileDetails(String username, String password, int personalNumber, String name, String lastName, String email, Map<String, Boolean> studyProgrammes){
        ((Student) loggedUser).editProfile(username, password, personalNumber, name, lastName, email, mapToArrayList(studyProgrammes));
    }
    
    public String getCompanyProfileDetails(){
        StringBuilder builder = new StringBuilder();
        builder.append("Název firmy: ").append(((Company) loggedUser).getName()).append("\n");
        builder.append("Sídlo firmy: ").append(((Company) loggedUser).getAddress()).append("\n");
        builder.append("Stát: ").append(((Company) loggedUser).getState()).append("\n");
        builder.append("Jméno kontaktní osoby: ").append(((Company) loggedUser).getContactPersonName()).append(" ").append(((Company) loggedUser).getContactPersonLastname()).append("\n");
        builder.append("Email kontaktní osoby: ").append(((Company) loggedUser).getContactPersonEmail()).append("\n");
        builder.append("Uživatelské jméno: ").append(((Company) loggedUser).getUsername()).append("\n");
        builder.append("Heslo: ").append(((Company) loggedUser).getPassword()).append("\n");
        String data = builder.toString();
        return data;
    }
    
    public void editCompanyProfileDetails(String username, String password, String companyName, String address, String state, String contactPersonName, String contactPersonLastName, String contactPersonEmail){
        ((Company) loggedUser).editProfile(username, password, companyName, address, state, contactPersonName, contactPersonLastName, contactPersonEmail);
    }
    
    public void publishPractice(String academicYear, String companyName, String companyAddress, String contactPerson, String description, Map<String, Boolean> studyProgrammes){
        ((Company) loggedUser).publishPractice(academicYear, companyName, companyAddress, contactPerson, description, mapToArrayList(studyProgrammes));
    }
    
    public void submitReportFromPractice(String message /* Předělat na PDF!! */, String practiceDescription){
        ((Student) loggedUser).submitMessageFromPractice(message, practiceDescription);
    }
    
    public String /* Předělat na PDF!! */ downloadReportFromPractice(String practiceDescription){
        return ((Company) loggedUser).downloadMessageFromPractice(practiceDescription);
    }

    public Map<String, Boolean> getStudyProgrammes() {
        return studyProgrammes;
    }
    
    private ArrayList<StudyProgramme> mapToArrayList(Map<String, Boolean> studyProgrammes){
        ArrayList<StudyProgramme> studyProgrammesList = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : studyProgrammes.entrySet()) {
            if(entry.getValue()){
                studyProgrammesList.add(new StudyProgramme(entry.getKey()));
            }
        }
        return studyProgrammesList;
    }
}
