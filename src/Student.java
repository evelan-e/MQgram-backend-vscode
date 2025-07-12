import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Student {
    String studentID;
    String studentName;

    // Data structures to hold the student's interactions
    ArrayList<Unit> studentCourse;           
    ArrayList<Post> studentPosts;            
    ArrayList<Comment> studentComments;     
    ArrayList<Student> studentFollowers;     
    ArrayList<Student> studentFollowing;    

    // CONSTRUCTOR
    // Initialises a new student object with empty lists for posts, comments, courses, etc.
    public Student(String studentID, String studentName) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.studentCourse = new ArrayList<>();
        this.studentPosts = new ArrayList<>();
        this.studentComments = new ArrayList<>();
        this.studentFollowers = new ArrayList<>();
        this.studentFollowing = new ArrayList<>();
    }


    public String getstudentID() {
        return studentID;
    }

    public String getstudentName() {
        return studentName;
    }

    public ArrayList<Unit> getstudentCourse() {
        return studentCourse;
    }

    public ArrayList<Post> getstudentPosts() {
        return studentPosts;
    }

    public ArrayList<Comment> getstudentComments() {
        return studentComments;
    }

    public ArrayList<Student> getstudentFollowers() {
        return studentFollowers;
    }

    public ArrayList<Student> getstudentFollowing() {
        return studentFollowing;
    }

    // Returns number of followers
    public int getFollowerCount() {
        return studentFollowers.size();
    }

    // Returns the number of people a student is following
    public int getFollowingCount() {
        return studentFollowing.size();
    }

    // FOLLOW FUNCTIONS 
    /* function allows a student to follow another existing students using their student ID
       function also stops a student from following themselves and duplicates
     */
    public void followStudent(String targetID, List<Student> allStudents) {
        if (this.studentID.equals(targetID)) {
            System.out.println("You cannot follow yourself.");
            return;
        }

        for (Student target : allStudents) {
            if (target.getstudentID().equals(targetID)) {
                if (studentFollowing.contains(target)) {
                    System.out.println("You're already following " + target.getstudentName());
                    return;
                }
                // Add target student to the current student's following list
                studentFollowing.add(target);
                // Add current student to the target's follower list
                target.studentFollowers.add(this);
                System.out.println("You are now following " + target.getstudentName());
                return;
            }
        }

        System.out.println("No student found with ID: " + targetID);
    }

    public static void createAccount(List<Student> students, Scanner scanner) {
        String name;
        // Prompt for and validate the student's name (letters and spaces only)
        while (true) {
            System.out.print("Enter student name: ");
            name = scanner.nextLine().trim();
            if (name.matches("[A-Za-z ]+")) {
                break;   // name is valid, exit loop
            }
            System.out.println("Invalid format. Use letters and spaces only.");
        }
    
        String id;
        // Prompt for and validate the student ID (digits only)
        while (true) {
            System.out.print("Enter student ID (digits only): ");
            id = scanner.nextLine().trim();
            if (id.matches("\\d{8}")) {
                break;   
            }
            System.out.println("Invalid format. Use digits only.");
        }
    
        // Check for duplicate IDs before creating the account
        if (PostMain.studentIdExists(students, id)) {
            System.out.println("Student ID already exists. Please try logging in or use a different ID.");
            return;
        }
    
        // Create the Student object and add it to the list
        Student student = new Student(id, name);
        students.add(student);
        System.out.println("Account created! Your Student ID is: " + id);
    
        // Persist the updated student list to the CSV file
        PostMain.saveStudentsToCSV(students, "students.csv");
    }

    // LOGIN & STUDENT MENU
    /* method that handles the login and post-login user interaction
      Provides options for unit enrollment, posting, viewing units, following, etc.
     */
    public static void loginAndManage(List<Student> students, PostMain postMain, List<Unit> units, Scanner scanner) {
        System.out.print("\nEnter your student ID: ");
        String input = scanner.nextLine();

        // Validates ID format
        if (input.isEmpty() || !input.matches("\\d{8}")) {
            System.out.println("Invalid ID format.");
            return;
        }

        // Locates student account
        Student user = null;
        for (Student s : students) {
            if (s.getstudentID().equals(input)) {
                user = s;
                break;
            }
        }

        if (user == null) {
            System.out.println("No account found for ID: " + input);
            return;
        }

        // Successful login
        System.out.println("\nWelcome Back, " + user.getstudentName());
        System.out.println("You are following " + user.getFollowingCount() + " student(s).");
        System.out.println("You are followed by " + user.getFollowerCount() + " student(s).");

        // Main user interaction menu loop
        while (true) {
            System.out.println("\n====== Menu ======");
            System.out.println("1. Enroll in a unit");
            System.out.println("2. Create a Post");
            System.out.println("3. View enrolled units");
            System.out.println("4. Logout");
            System.out.println("5. Search posts");
            System.out.println("6. Follow a student by ID");
            System.out.print("Select an option: ");
            String login = scanner.nextLine();

            switch (login) {
                case "1":
                System.out.println("Select a unit:");
                // 1) List each existing unit with 1-based indexing
                for (int i = 0; i < units.size(); i++) {
                    System.out.println((i + 1) + ". " + units.get(i).getUnitName());
                }
                // 2) Next number is “add new unit”
                int addOption = units.size() + 1;
                System.out.println(addOption + ". Can't find your unit? Enter a new one");
            
                System.out.print("Enter option number: ");
                String optionInput = scanner.nextLine().trim();
            
                // Validate numeric input
                if (optionInput.isEmpty() || !optionInput.matches("\\d+")) {
                    System.out.println("Invalid input. Please enter a number.");
                    break;
                }
                // Convert to integer
                int option = 0;
                for (char c : optionInput.toCharArray()) {
                    option = option * 10 + (c - '0');
                }
            
                if (option >= 1 && option <= units.size()) {
                    // enroll in existing unit
                    Unit selectedUnit = units.get(option - 1);
                    selectedUnit.enrollStudent(user);
                    user.getstudentCourse().add(selectedUnit);
                    System.out.println("Enrolled in " + selectedUnit.getUnitName());
            
                } else if (option == addOption) {
                    // create & enroll in a new unit
                    String newCode;
                    while (true) {
                        System.out.print("\nEnter new unit code (e.g. COMP1010): ");
                        newCode = scanner.nextLine().trim();
                        if (newCode.matches("^[A-Z]{4}\\d{4}$")) break;
                        System.out.println("Invalid unit format. Use 4 letters then 4 digits, e.g. COMP1010.");
                    }
                    Unit newUnit = new Unit(units.size() + 1, newCode);
                    units.add(newUnit);
                    newUnit.enrollStudent(user);
                    user.getstudentCourse().add(newUnit);
                    System.out.println("Enrolled in " + newCode);
            
                } else {
                    System.out.println("Invalid option.");
                }
            
                // persist changes
                PostMain.saveStudentsToCSV(students, "students.csv");
                PostMain.saveUnitsToCSV(units,     "units.csv");
                break;

                case "2":
                    // Create a post
                    Post.createPost(user, postMain, scanner);
                    break;

                case "3":
                    // View enrolled units
                    Unit.viewEnrolledUnits(user);
                    break;

                case "4":
                    // Logout
                    System.out.println("\nYou have been logged out.");
                    return;

                case "5":
                    // Search posts
                    Post.searchPostsMenu(user, postMain, scanner);
                    break;

                case "6":
                    // Follow another student
                    System.out.print("Enter the student ID you want to follow: ");
                    String targetID = scanner.nextLine();
                    user.followStudent(targetID, students);
                    System.out.println("Following count: " + user.getFollowingCount());
                    System.out.println("Followers count: " + user.getFollowerCount());
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ENROLL IN A UNIT
    /*Enrolls the student in a unit if not already enrolled.
      Also updates the unit to include this student.
     */
    public void enrollInUnit(Unit unit) {
        if (!studentCourse.contains(unit)) {
            studentCourse.add(unit);
            unit.enrollStudent(this);
        }
    }

    //CREATE A POST
    /*Creates a new post by the student and links it to the selected unit.
      Updates student's post list, the unit's related posts, and the post manager.
     */
    public void createPost(String content, Unit unit, PostMain postMain) {
        Post newPost = new Post(postMain.getAllPosts().size() + 1, content, this, unit);
        studentPosts.add(newPost);
        unit.addRelatedPost(newPost);
        postMain.addPost(newPost);
    }

    // EXPORT to CSV 
    /*
       Outputs student data in CSV format.
       Used for saving data to a file.
     */
    public String toCSV() {
        return studentID + "," + studentName;
    }
}
