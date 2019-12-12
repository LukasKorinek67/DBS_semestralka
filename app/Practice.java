package app;

import java.util.ArrayList;

/**
 *
 * @author lukaskorinek
 */
public class Practice {
    
    private String description;
    private ArrayList<StudyProgramme> studyProgrammes;
    private Company company;
    private int assignedStudentNumber;
    private String academicYear;
    private String writtenReportFromStudent;
    
    public Practice(String description, ArrayList<StudyProgramme> studyProgrammes, Company company, String academicYear) {
        this.description = description;
        this.studyProgrammes = studyProgrammes;
        this.company = company;
        this.academicYear = academicYear;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<StudyProgramme> getStudyProgrammes() {
        return studyProgrammes;
    }

    public Company getCompany() {
        return company;
    }

    public int getAssignedStudentNumber() {
        return assignedStudentNumber;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public String getWrittenReportFromStudent() {
        return writtenReportFromStudent;
    }

    public void addWrittenReportFromStudent(String writtenReportFromStudent) {
        this.writtenReportFromStudent = writtenReportFromStudent;
    }
    
}
