import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Unit {
    int unitID;
    String unitName;
    ArrayList<Student> studentsEnrolled;   
    ArrayList<Post> relatedPosts;          

    public Unit(String unitName) {
        this.unitName = unitName;
        this.studentsEnrolled = new ArrayList<>();
        this.relatedPosts = new ArrayList<>();
    }

    // Optional constructor to manually specify unit ID
    public Unit(int unitID, String unitName) {
        this.unitID = unitID;
        this.unitName = unitName;
        this.studentsEnrolled = new ArrayList<>();
        this.relatedPosts = new ArrayList<>();
    }


    public int getUnitID() {
        return unitID;
    }

    public String getUnitName() {
        return unitName;
    }

    public ArrayList<Student> getStudentsEnrolled() {
        return studentsEnrolled;
    }

    public ArrayList<Post> getRelatedPosts() {
        return relatedPosts;
    }

    public int countStudentsEnrolled() {
        return studentsEnrolled.size();  // Returns number of enrolled students
    }

    public ArrayList<Post> getUnitRelatedPosts() {
        return relatedPosts;
    }

    // Converts unit info into CSV format
    public String toCSV() {
        return unitID + "," + unitName;
    }

    // Nicely formatted unit string for display
    @Override
    public String toString() {
        return "Unit ID: " + unitID + ", Unit Name: " + unitName;
    }

    // ACTION METHODS

    // Enrolls a student if not already enrolled through the use of an if condition)
    public void enrollStudent(Student student) {
        if (!studentsEnrolled.contains(student)) {
            studentsEnrolled.add(student);
        }
    }

    public void addRelatedPost(Post post) {
        if (!relatedPosts.contains(post)) {
            relatedPosts.add(post);
        }
    }

    // Static method used to enroll user in a unit (used in main menu)
    public static void enrollInUnit(Student user, List<Unit> units, List<Student> students, Scanner scanner) {
        System.out.print("Enter unit name to enroll in: ");
        String unitName = scanner.nextLine();

        if (!unitName.matches("[A-Z]{4}[0-9]{4}")) {
        System.out.println("Invalid unit name format. It must be 4 capital letters followed by 4 digits (e.g., MATH1000).");
        return;
    }

        Unit unit = null;

        // Use of a for loop to search if unit already exists
        for (Unit u : units) {
            if (u.getUnitName().equals(unitName)) {  // If unit name matches
                unit = u;
                break;
            }
        }

        //If-else logic
        // If unit not found, create new one and add to list
        if (unit == null) {
            unit = new Unit(units.size() + 1, unitName);  
            units.add(unit);
            PostMain.saveUnitsToCSV(units, "units.csv");  
        }

        // Enrolls the user and save updated student list
        user.enrollInUnit(unit);
        PostMain.saveStudentsToCSV(students, "students.csv");
    }

    public static void viewEnrolledUnits(Student user) {
        System.out.println("\nYou are enrolled in the following units:");

        for (Unit u : user.getstudentCourse()) {
            System.out.println("- " + u.getUnitName());
        }
    }
}
