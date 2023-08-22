package app;

import java.sql.SQLException;
import java.util.ArrayList;
import utils.exceptions.BlankDataException;
import utils.DatabaseAccessor;
import utils.exceptions.InvalidDataLengthException;
import utils.exceptions.InvalidEmailAddressException;
import utils.exceptions.InvalidStudyProgrammesCombinationException;
import utils.exceptions.PasswordTooShortException;
import utils.User;
import utils.exceptions.InvalidPersonalNumberException;
import utils.exceptions.UserDoesntExistException;
import utils.exceptions.WrongPasswordException;

/**
 *
 * @author lukaskorinek
 */
public class PracticeSystem {
    private User loggedUser;
    
    public User getLoggedUser() {
        return loggedUser;
    }
    
    // vrácení celého jména studenta nebo firmy - pro vypsání přihlášeného studenta
    public String getLoggedUserFullName() {
        if(loggedUser instanceof Student){
            return ((Student)loggedUser).getFullName();
        }else if(loggedUser instanceof Company){
            return ((Company)loggedUser).getName();
        }else{
            return null;
        }
    }
    
    public void createStudentAccount(String username, String password, String personalNumber, String name, String lastName, String email, ArrayList<StudyProgramme> studyProgrammes) throws SQLException, ClassNotFoundException, BlankDataException, InvalidEmailAddressException, InvalidDataLengthException, PasswordTooShortException, InvalidStudyProgrammesCombinationException, InvalidPersonalNumberException{
        loggedUser = Student.createAccount(username, password, personalNumber, name, lastName, email, studyProgrammes);
    }
    
    public void loginStudent(String username, String password) throws SQLException, ClassNotFoundException, UserDoesntExistException, WrongPasswordException{
        loggedUser = Student.login(username, password);
    }
    
    public void createCompanyAccount(String username, String password, String name, String address, String state, String contactPersonName, String contactPersonLastname, String contactPersonEmail) throws SQLException, ClassNotFoundException, BlankDataException, InvalidEmailAddressException, InvalidDataLengthException, PasswordTooShortException{
        loggedUser = Company.createAccount(username, password, name, address, state, contactPersonName, contactPersonLastname, contactPersonEmail);
    }
    
    public void loginCompany(String username, String password) throws SQLException, ClassNotFoundException, WrongPasswordException, UserDoesntExistException{
        loggedUser = Company.login(username, password);
    }
    
    public void logoutUser(){
        loggedUser = null;
    }

    // vrácení všech studijní oborů
    public ArrayList<StudyProgramme> getStudyProgrammes() throws SQLException, ClassNotFoundException {
        DatabaseAccessor databaseAccessor = new DatabaseAccessor();
        ArrayList<StudyProgramme> studyProgrammes = DatabaseAccessor.getAllStudyProgrammes();
        return studyProgrammes;
    }
}