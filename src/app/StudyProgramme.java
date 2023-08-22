package app;

/**
 *
 * @author lukaskorinek
 */
public class StudyProgramme {
    private String name;

    public StudyProgramme(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}