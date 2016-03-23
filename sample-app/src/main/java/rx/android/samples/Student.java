package rx.android.samples;
import java.util.List;

/**
 * Created by relicemxd on 16/1/11.
 */
public class Student {
    private List<Course> corses;
    private String       name;

    public Student(String name) {
        this.name = name;
    }

    public List<Course> getCorses() {
        return corses;
    }

    public void setCorses(List<Course> corses) {
        this.corses = corses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
