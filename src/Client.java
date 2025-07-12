import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Client {
    // This is the entry point of the program
    public static void main(String[] args) throws IOException {
        System.out.println("Working directory: " + System.getProperty("user.dir"));
        // Creation of an instance of PostMain to manage posts and interactions
        PostMain postMain = new PostMain();

        // The scanner is used to take user input
        Scanner scanner = new Scanner(System.in);

        // Load units and students from CSV files
        List<Unit> units = PostMain.loadUnitsFromCSV("units.csv"); 
        List<Student> students = PostMain.loadStudentsFromCSV("students.csv", units); 
        postMain.loadFromCSV("allPosts.csv", students, units);

        // Infinite loop to show main menu until user decides to exit
        while (true) {
            // Displays the main menu
            System.out.println("\n====== MQ Gram ======");
            System.out.println("1. Create an account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("\nSelect an option: ");
            
            // Read user's menu selection
            String option = scanner.nextLine();

            // Conditional branching using if-else to handle user selection
            if (option.equals("1")) {
                //Create a new account
                Student.createAccount(students, scanner);
            } else if (option.equals("2")) {
                // Log in and allow post management
                Student.loginAndManage(students, postMain, units, scanner);
            } else if (option.equals("3")) {
                // Exit the loop and program
                System.out.println("Goodbye!");
                break; // Exits the while loop
            } else {
                // Handles invalid input
                System.out.println("Invalid option.");
            }
        }

        // Close the scanner
        scanner.close();
    }
}
