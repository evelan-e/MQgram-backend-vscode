# **MQGram Social Network**

## **Project Topic Chosen**
Topic 3: 
You will create a social network aimed to help students share study tips and talk about how great university is. 
Students will be able to make posts, which can have comments from other students. 
These posts can be purely text based or include media. 
Students should be able to follow other students that they would like updates from.  

---

## **Project Overview**

- Students can create an account and/ or log in with their student ID.
- Upon login, students can enroll in specific units (or input a new unit into the program) which contextualise their discussions and posts. 
-Students can follow other students by typing in their studentID. A student can also be followed by other students. (The program will show how many followers and following a student has.)
- Students can create posts tied to a unit, saved in `allPosts.csv` .
- Students can search posts by unit or keyword, allowing students to quickly find relevant discussions. Another option is to find posts through the units enrolled.
- Posts support recursive, threaded comments. Students can add new comments or reply to existing ones.
- View and reply to comments in a recursive “threaded” view.


## How to run the project
- Open client class and run the program. 
- Main menu will show a few options of what users can do. 
- Create an account by selecting option 1 or log in with an existing account. 
-  Type in account name and studentID (must have 8 digits). 
- Log in using these credentials by then picking option 2
Once logged in, you can:
- Enroll in units or create a new unit. 
- Create text posts tied to a specific unit (NOTE: Must be enrolled in at least one unit).
- View, search, and reply to posts.
- Comment on posts in a threaded (recursive) format.
- Follow other students (if their StudentID is known and they have an account)


## File structure
COMP1010-MQGram/
├─ .vscode/
├─ bin/
├─ doc/
│   ├─ UML diagrams/
│   │   └─ UMLandConnections.png
│   └─ README.txt
├─ lib/
│   └─ junit-platform-console-standalone-1.7.2.jar
├─ src/
│   ├─ com/mqgram/
│   │   ├ Client.java
│   │   ├ PostMain.java
│   │   ├ Student.java
│   │   ├ Unit.java
│   │   ├ Post.java
│   │   ├ Comment.java
│   │   ├ StudentTest.java
│   │   ├ UnitTest.java
│   │   ├ PostTest.java
│   │   └ CommentTest.java
│   ├ allPosts.csv
│   ├ students.csv
│   └ units.csv
└─ README.md      



## Class Structure

| Class        | Description                                                                                                                                       |
|--------------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| Client       | Main entry point: displays menus, handles user input, and coordinates calls to Student and PostMain.                                             |
| PostMain     | Manages CSV I/O and in-memory data: loads/saves units, students, and posts, and maintains lists.                                                  |
| Student      | Represents a user account: holds ID, name, enrolled units, posts, comments, followers, and following; also provides signup/login logic.           |
| Unit         | Represents a course or unit: stores unit code, enrolled students, and related posts; handles enrollment and unit listing.                          |
| Post         | Represents a discussion post: holds post ID, content, timestamp, author, unit, comments, and likes; provides post creation and search methods.    |
| Comment      | Represents a threaded comment: holds author, content, timestamp, likes, and a list of replies; supports recursive printing and reply counting.     |
| StudentTest  | JUnit tests for Student: verifies account creation, follow/unfollow behavior, and edge cases.                                                     |
| UnitTest     | JUnit tests for Unit: verifies enrollment, unit listing, and CSV integration.                                                                      |
| PostTest     | JUnit tests for Post: verifies post creation, search by keyword, and comment integration.                                                         |
| CommentTest  | JUnit tests for Comment: verifies adding replies, recursive printing, and like counts.                                                            |


## Task allocation 
- Evelane: 25% Searching Posts, Testing, user input and interaction , comments
- Jenny: 25% Classes and Structuring, Menu and login creation, exception edits 
- Chaaru: 25% Classes, UML diagrams, Following and followers methods, edits and commenting
- Lara: 25% Post creation, saves to csv (I/O), Testing, UML diagrams, edits




## UML diagrams && A description of the structure of the program 
Seen in doc --> UML diagrams --> UMLandConnections.png
- A student can be in many units, but can have an account without being enrolled in any.
- A student can be an author of many posts. A student does not have to have any posts. 
- A student can follow multiple students. A student can be followed by multiple students. 
- A unit can have many students, but exists without any students being enrolled.
- A unit can have multiple posts connected to it. Unit can exist without any posts. 
- A post must be associated with a unit.
- Methods in the post class relies on PostMain
- A comment and a post must be under a post and must have one author(student).  



## An analysis of two of your methods implemented against alternative approaches in terms of efficiency and/or memory. 

#Method 1: 
public static void createAccount(List<Student> students, Scanner scanner) {
  System.out.print("\nEnter student name: ");
  String name = scanner.nextLine();


  System.out.print("Enter student ID: ");
  String id = scanner.nextLine();  


  if (PostMain.studentIdExists(students, id)) {
      System.out.println("Student ID already exists. Please try logging in or use a different ID.");
      return;
  }


  Student student = new Student(id, name);
  students.add(student);
  System.out.println("\nAccount created! Your Student ID is: " + id);   PostMain.saveStudentsToCSV(students, "students.csv");
}




#Alternative (was placed in client class)
if(option.equals("1")){
           System.out.print("\nEnter student name : ");
           String name = scanner.nextLine();
int studentID = random.nextInt(90000) + 10000;
           Student student = new Student(studentID, name);
students.add(student);
}




----- Method 1 Analysis & Justification  ------
- The method created and placed within the Student class is to structure the code cleaner. 
    Thus, making the code easier to understand, read, and reuse. 
- Lets the user choose their own/current student ID, which makes the social network more user friendly
- Method approach also allowed us to save student/s into a csv file which also let us make sure the student is saved after the next run of the program.





# Method 2

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
    }
}


----- Method 2 Analysis & Justification  ------

- This method is placed in PostMain as it is used for initializing and managing the overall program, 
    including existing components (students, units, posts) from CSV files. Acts more like the controller
    for data flow and placed in the postMain to create a cleaner code. 
- Each line is splitted into fields (ID, name, enrolled units) and a new student object is created using those fields. 
- By using the CSV files, it allows each student data to be saved within runs. When the app restarts, all previous student data is restored.
- It is designed to be user friendly, that works like a login system. This method is also reusable from anywhere in the app. 
