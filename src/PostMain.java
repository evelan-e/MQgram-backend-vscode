import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PostMain {
     List<Student> allStudents = new ArrayList<>();
     List<Post> posts;


    public PostMain() {
        this.posts = new ArrayList<>();
    }

    public void addPost(Post post) {
        this.posts.add(post);
    }

    public List<Post> getAllPosts() {
        return posts;
    }
    
public void saveToCSV(String fileName) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
        for (Post post : posts) {
            writer.write(post.getPostID() + "," +
                    post.getPostAuthor().getstudentID() + "," +  
                    post.getUnitRelation().getUnitName() + "," +
                    post.getPostContent().replace(",", " ") + "\n");
        }
    }
}


public void loadFromCSV(String filename, List<Student> students, List<Unit> units) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",", 4);
            if (parts.length == 4) {
                int id = Integer.parseInt(parts[0]);
                String studentId = parts[1];
                String unitName = parts[2];
                String content = parts[3];

                Student author = students.stream()
                    .filter(s -> s.getstudentID().equals(studentId))
                    .findFirst().orElse(null);

                Unit unit = units.stream()
                    .filter(u -> u.getUnitName().equals(unitName))
                    .findFirst().orElse(null);

                if (author != null && unit != null) {
                    Post post = new Post(id, content, author, unit);
                    posts.add(post);
                    unit.addRelatedPost(post);
                    author.getstudentPosts().add(post); 
                }
            }
        }
    }
}
    public static List<Unit> loadUnitsFromCSV(String filename) {
        List<Unit> units = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length >= 2) {
                    int unitID = Integer.parseInt(parts[0]);
                    String unitName = parts[1];
                    units.add(new Unit(unitID, unitName));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading units: " + e.getMessage());
        }
        return units;
    }

    
    public Student getStudentByName(String studentName) {
        for (Student student : allStudents) {
            if (student.getstudentName().equals(studentName)) {
                return student;
            }
        }
        
        Student newStudent = new Student("tempID", studentName); 
        allStudents.add(newStudent);
        return newStudent;
    }

    public static boolean studentIdExists(List<Student> students, String studentId) {
    for (Student s : students) {
        if (s.getstudentID().equals(studentId)) {
            return true;
        }
    }
    return false;
}
    public static void saveUnitsToCSV(List<Unit> units, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("unitCode,unitName\n"); // header
            for (Unit unit : units) {
                writer.write(unit.toCSV() + "\n");
            }
            System.out.println("Units saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error writing CSV: " + e.getMessage());
        }
    }

public static void saveStudentsToCSV(List<Student> students, String filename) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
        for (Student s : students) {
            // Build unit list string
            StringBuilder unitsStr = new StringBuilder();
            for (Unit u : s.getstudentCourse()) {
                if (unitsStr.length() > 0) unitsStr.append(";");
                unitsStr.append(u.getUnitName());
            }
            
            writer.println(s.getstudentID() + "," + s.getstudentName() + "," + unitsStr.toString());
        }
    } catch (IOException e) {
        System.out.println("Error saving students: " + e.getMessage());
    }
}

public static List<Student> loadStudentsFromCSV(String filename, List<Unit> units) {
    List<Student> students = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",", 3);
            if (parts.length >= 2) {
                Student s = new Student(parts[0], parts[1]);

                
                if (parts.length == 3 && !parts[2].isEmpty()) {
                    String[] unitNames = parts[2].split(";");
                    for (String name : unitNames) {
                        for (Unit u : units) {
                            if (u.getUnitName().equals(name)) {
                                s.enrollInUnit(u); 
                            }
                        }
                    }
                }
                students.add(s);
            }
        }
    } catch (IOException e) {
        System.out.println("Error loading students: " + e.getMessage());
    }
    return students;
}
}


