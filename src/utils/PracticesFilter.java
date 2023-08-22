package utils;

/**
 *
 * @author lukaskorinek
 */
// filtr pro filtrování praxí
public class PracticesFilter {
    
    String company;
    String academicYear;
    String studyProgrammeType;
    String isAssigned;

    public PracticesFilter(String company, String academicYear, String studyProgrammeType, String isAssigned) {
        this.company = company;
        this.academicYear = academicYear;
        this.studyProgrammeType = studyProgrammeType;
        this.isAssigned = isAssigned;
    }

    public String getCompany() {
        return company;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public String getStudyProgrammeType() {
        return studyProgrammeType;
    }

    public String getIsAssigned() {
        return isAssigned;
    }
}