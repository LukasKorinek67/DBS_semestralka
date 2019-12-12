package ui;

import app.PracticeSystem;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author lukaskorinek
 */
public class Main {
    public static Scanner sc = new Scanner(System.in);
    public static PracticeSystem app = new PracticeSystem();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println("Start");
        System.out.println("---------------------------");
        System.out.println("Student:");
        System.out.println(" - přihlásit (stiskni 1)");
        System.out.println(" - registrovat (stiskni 2)");
        System.out.println("Firma:");
        System.out.println(" - přihlásit (stiskni 3)");
        System.out.println(" - registrovat (stiskni 4)");
        System.out.println("---------------------------");
        String volba = sc.next();
        
        
        String username;
        String password;
        
        switch(volba) {
            case "1": 
                System.out.println("Uživatelské jméno:");
                username = sc.next();
                System.out.println("Heslo:");
                password = sc.next();
                app.loginStudent(username, password);
                studentMenu();
                break;
            case "2": 
                System.out.println("Uživatelské jméno:");
                username = sc.next();
                System.out.println("Heslo:");
                password = sc.next();
                System.out.println("Osobní číslo:");
                int personalNumber = sc.nextInt();
                System.out.println("Vaše jméno:");
                String name = sc.next();
                System.out.println("Vaše příjmení:");
                String lastName = sc.next();
                System.out.println("Váš e-mail:");
                String email = sc.next();
                System.out.println("Váš obor:");
                //vypsání všech oborů
                int i = 1;
                Map<String, Boolean> studyProgrammes = app.getStudyProgrammes();;
                for (Map.Entry<String, Boolean> entry : studyProgrammes.entrySet()) {
                    System.out.println(i + " - " + entry.getKey());
                    i++;
                }
                System.out.println(" - zadejte čísla všech oborů, které v současnosti studujete - zadávání ukončete nulou.");
                int number;
                while((number = sc.nextInt()) != 0){
                    int j = 1;
                    for (Map.Entry<String, Boolean> entry : studyProgrammes.entrySet()) {
                        if(number == j){
                            studyProgrammes.replace(entry.getKey(), Boolean.TRUE);
                        }
                        j++;
                    }
                }
                app.createStudentAccount(username, password, personalNumber, name, lastName, email, studyProgrammes);
                studentMenu();
                break;
            case "3": 
                System.out.println("Uživatelské jméno:");
                username = sc.next();
                System.out.println("Heslo:");
                password = sc.next();
                app.loginCompany(username, password);
                companyMenu();
                break;
            case "4": 
                System.out.println("Uživatelské jméno:");
                username = sc.next();
                System.out.println("Heslo:");
                password = sc.next();
                System.out.println("Jméno Vaší firmy::");
                String companyName = sc.next();
                System.out.println("Adresa Vaší firmy:");
                String address = sc.next();
                System.out.println("Stát:");
                String state = sc.next();
                System.out.println("Jméno kontaktní osoby:");
                String contactPersonName = sc.next();
                System.out.println("Příjmení kontaktní osoby:");
                String contactPersonLastName = sc.next();
                System.out.println("Email kontaktní osoby:");
                String contactPersonEmail = sc.next();
                app.createCompanyAccount(username, password, companyName, address, state, contactPersonName, contactPersonLastName, contactPersonEmail);
                companyMenu();
                break;
        }
        
        System.out.println("Konec");
        
        
    }

    private static void studentMenu() {
        boolean end = false;
        
        while(!end){
            printStudentMenu();
            String choice = sc.next();
            switch(choice){
                case "1":{
                    editStudentProfile();
                }
                    break;
                case "2":
                    
                    break;
                case "3":
                    
                    break;
                case "4":{
                    submitReportFromPractice();
                }
                    break;
                case "0":
                    end = true;
                    break;
                default:
                    System.out.println("Neplatná volba, opakujte pokus.");
            }
        }
    }
    
    private static void printStudentMenu(){
        System.out.println();
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        System.out.println(" Menu Student: ");
        System.out.println(" - spravovat svůj profil (stiskni 1)");
        System.out.println(" - prohlížet praxe a filtrovat podle kritérií (stiskni 2)");
        System.out.println(" - projevit zájem o praxi (stiskni 3)");
        System.out.println(" - odevzdat písemnou zprávu z praxe (stiskni 4");
        System.out.println(" ------------------------");
        System.out.println(" - odhlásit se (stiski 0)");
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        System.out.println();
    }
    
    private static void editStudentProfile(){
        System.out.println("Zde jsou údaje o vašem profilu:");
        System.out.println(app.getStudentProfileDetails());
        System.out.println();
        System.out.println("Přejete si je změnit? (Ano - 1, Ne - 0)");
        if(sc.nextInt() == 1){
            System.out.println("Zadejte prosím nové údaje:");
            System.out.println();
            System.out.println("Osobní číslo:");
            int personalNumber = sc.nextInt();
            System.out.println("Vaše jméno:");
            String name = sc.next();
            System.out.println("Vaše příjmení:");
            String lastName = sc.next();
            System.out.println("Váš e-mail:");
            String email = sc.next();
            System.out.println("Váš obor:");
            //vypsání všech oborů
            int i = 1;
            Map<String, Boolean> studyProgrammes = app.getStudyProgrammes();;
            for (Map.Entry<String, Boolean> entry : studyProgrammes.entrySet()) {
                System.out.println(i + " - " + entry.getKey());
                i++;
            }
            System.out.println(" - zadejte čísla všech oborů, které v současnosti studujete - zadávání ukončete nulou.");
            int number;
            while((number = sc.nextInt()) != 0){
                int j = 1;
                for (Map.Entry<String, Boolean> entry : studyProgrammes.entrySet()) {
                    if(number == j){
                        studyProgrammes.replace(entry.getKey(), Boolean.TRUE);
                    }
                    j++;
                }
            }
            System.out.println("Uživatelské jméno:");
            String username = sc.next();
            System.out.println("Heslo:");
            String password = sc.next();
            app.editStudentProfileDetails(username, password, personalNumber, name, lastName, email, studyProgrammes);
        }
    }
    
    private static void submitReportFromPractice(){
        System.out.println("Zadejte popis vaší praxe, ke které chcete odevzdat písemnou zprávu.");
        String practiceDescription = sc.nextLine();
        System.out.println("Vložte zprávu - nyní jen text, později předělat na PDF");
        String message = sc.nextLine();
        app.submitReportFromPractice(message, practiceDescription);   
    }

    private static void companyMenu() {
        boolean end = false;
        
        while(!end){
            printCompanyMenu();
            String choice = sc.next();
            switch(choice){
                case "1": {
                    editCompanyProfile();
                }
                    break;
                case "2": {
                    publishPractice();
                }
                    
                    break;
                case "3":
                    
                    break;
                case "4":
                    
                    break;
                case "5":{
                    downloadReportFromPractice();
                }
                    
                    break;
                case "0":
                    end = true;
                    break;
                default:
                    System.out.println("Neplatná volba, opakujte pokus.");
            }
        }
    }
    
    private static void printCompanyMenu(){
        System.out.println();
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        System.out.println(" Menu Firma: ");
        System.out.println(" - spravovat svůj profil (stiskni 1)");
        System.out.println(" - zveřejnit praxi/popis a kritéria praxe (stiskni 2)");
        System.out.println(" - editovat, kopírovat nebo smazat existující praxe (stiskni 3)");
        System.out.println(" - akceptovat studenta na praxi (stiskni 4)");
        System.out.println(" - stáhnout si písemnou zprávu z uskutečněné praxe (stiskni 5)");
        System.out.println(" ------------------------");
        System.out.println(" - odhlásit se (stiski 0)");
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        System.out.println();
    }
    
    private static void editCompanyProfile(){
        System.out.println("Zde jsou údaje o vašem profilu:");
        System.out.println(app.getCompanyProfileDetails());
        System.out.println();
        System.out.println("Přejete si je změnit? (Ano - 1, Ne - 0)");
        if(sc.nextInt() == 1){
            System.out.println("Zadejte prosím nové údaje:");
            System.out.println();
            System.out.println("Jméno Vaší firmy::");
            String companyName = sc.next();
            System.out.println("Adresa Vaší firmy:");
            String address = sc.next();
            System.out.println("Stát:");
            String state = sc.next();
            System.out.println("Jméno kontaktní osoby:");
            String contactPersonName = sc.next();
            System.out.println("Příjmení kontaktní osoby:");
            String contactPersonLastName = sc.next();
            System.out.println("Email kontaktní osoby:");
            String contactPersonEmail = sc.next();
            System.out.println("Uživatelské jméno:");
            String username = sc.next();
            System.out.println("Heslo:");
            String password = sc.next();
            app.editCompanyProfileDetails(username, password, companyName, address, state, contactPersonName, contactPersonLastName, contactPersonEmail);
            
        }
    }

    private static void publishPractice() {
        sc.nextLine(); //jen pro vyčištění bufferu
        System.out.println("Formulář pro vypsání praxe.");
        System.out.println("Akademický rok: př. \"2019/2020\" ");
        String academicYear = sc.nextLine();
        System.out.println("Název firmy:");
        String companyName = sc.nextLine();
        System.out.println("Sídlo firmy - adresa, stát");
        String companyAddress = sc.nextLine();
        System.out.println("Kontaktní osoba - jméno, příjmení, email:");
        String contactPerson = sc.nextLine();
        System.out.println("Popis praxe:");
        String description = sc.nextLine();
        //obory ??
        System.out.println("Vhodné pro obory:");
        //vypsání všech oborů
        int i = 1;
        Map<String, Boolean> studyProgrammes = app.getStudyProgrammes();;
        for (Map.Entry<String, Boolean> entry : studyProgrammes.entrySet()) {
            System.out.println(i + " - " + entry.getKey());
            i++;
        }
        System.out.println(" Zadejte čísla oborů, pro které je praxe určena - zadávání ukončete nulou.");
        
        int number;
        while((number = sc.nextInt()) != 0){
            int j = 1;
            for (Map.Entry<String, Boolean> entry : studyProgrammes.entrySet()) {
                if(number == j){
                    studyProgrammes.replace(entry.getKey(), Boolean.TRUE);
                }
                j++;
            }
        }
        //
        for (Map.Entry<String, Boolean> entry : studyProgrammes.entrySet()) {
            if(entry.getValue()){
                System.out.println(entry.getKey());
            }
        }
        //
        app.publishPractice(academicYear, companyName, companyAddress, contactPerson, description, studyProgrammes);
        System.out.println("Praxe byla přidána!");
    }
    
    private static void downloadReportFromPractice(){
        System.out.println("Zadejte popis vaší praxe, ze které chcete stáhnout písemnou zprávu.");
        String practiceDescription = sc.nextLine();
        String message = app.downloadReportFromPractice(practiceDescription); 
        System.out.println("Zde je zpráva z praxe:");
        System.out.println(message);
    }
    
}
