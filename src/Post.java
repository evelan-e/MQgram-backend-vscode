import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Post {
     int postID;
     String postContent;
     ArrayList<Comment> postComments;
     ArrayList<Student> likes;
     Student postAuthor;
     Unit unitRelation;

    public Post(int postID, String postContent, Student postAuthor, Unit unitRelation) {
        this.postID = postID;
        this.postContent = postContent;
        this.postComments = new ArrayList<>();
        this.likes = new ArrayList<>();
        this.postAuthor = postAuthor;
        this.unitRelation = unitRelation;
    }
    //These are all getter methods to return values without any changes
    public int getPostID() {
        return postID;
    }

    public String getPostContent() {
        return postContent;
    }


    public ArrayList<Comment> getPostComments() {
        return postComments;
    }

    public ArrayList<Student> getLikes() {
        return likes;
    }

    public Student getPostAuthor() {
        return postAuthor;
    }

    public Unit getUnitRelation() {
        return unitRelation;
    }

    
     // Allows a student to create a post & Safely handles user input
     
    
    public static void createPost(Student user, PostMain postMain, Scanner scanner) {
        if (user.getstudentCourse().isEmpty()) {
            System.out.println("You're not enrolled in any units yet.");
            return;
        }

        System.out.println("\nSelect a unit number to post about:");
        for (int i = 0; i < user.getstudentCourse().size(); i++) {
            System.out.println((i + 1) + ". " + user.getstudentCourse().get(i).getUnitName());
        }

        String selection = scanner.nextLine();
        int index = -1;

        // Input validation using try-catch
        try {
            index = Integer.parseInt(selection) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (index < 0 || index >= user.getstudentCourse().size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Unit selectedUnit = user.getstudentCourse().get(index);

        System.out.print("Enter your post content: ");
        String postContent = scanner.nextLine();

        Post post = new Post(postMain.getAllPosts().size() + 1, postContent, user, selectedUnit);
        postMain.addPost(post);
        selectedUnit.addRelatedPost(post);

        // Try-catch to handle possible IO exception when saving
        try {
            postMain.saveToCSV("allPosts.csv");
        } catch (IOException e) {
            System.out.println("Error saving post to CSV: " + e.getMessage());
        }

        System.out.println("Posted!");
    }

    /**
     Displays menu options to search posts by unit or keyword. 
     */

    public static void searchPostsMenu(Student user, PostMain postMain, Scanner scanner) {
        if (user.getstudentCourse().isEmpty()) {
            System.out.println("You're not enrolled in any units yet.");
            return;
        }

        while (true) { // Infinite loop until user chooses to return
            System.out.println("\n====== Search Posts Menu ======");
            System.out.println("1. Search posts by enrolled unit");
            System.out.println("2. Search posts by keyword");
            System.out.println("3. Return to main menu");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                // List units
                System.out.println("\nSelect a unit to view related posts:");
                for (int i = 0; i < user.getstudentCourse().size(); i++) {
                    System.out.println((i + 1) + ". " + user.getstudentCourse().get(i).getUnitName());
                }

                System.out.print("Enter the number of the unit: ");
                String unitInput = scanner.nextLine();
                int index = -1;

                try {
                    index = Integer.parseInt(unitInput) - 1;
                    if (index < 0 || index >= user.getstudentCourse().size()) {
                        System.out.println("Invalid selection.");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                    continue;
                }

                Unit selectedUnit = user.getstudentCourse().get(index);
                System.out.println("\n=== Posts for Unit: " + selectedUnit.getUnitName() + " ===");

                boolean found = false;
                ArrayList<Post> posts = selectedUnit.getUnitRelatedPosts();

                // Print each post under the selected unit
                for (int i = 0; i < posts.size(); i++) {
                    Post post = posts.get(i);
                    System.out.println((i + 1) + ". " + post.getPostAuthor().getstudentName() + ": " + post.getPostContent());
                    found = true;
                }

                if (!found) {
                    System.out.println("No posts found for this unit.");
                    continue;
                }

                // Prompt user to select a post to view
                System.out.print("\nEnter the number of the post to view (or 0 to go back): ");
                String postChoice = scanner.nextLine();

                if (postChoice.equals("0")) {
                    continue;
                }

                int postIndex = -1;

                try {
                    postIndex = Integer.parseInt(postChoice) - 1;
                    if (postIndex < 0 || postIndex >= posts.size()) {
                        System.out.println("\nInvalid selection.");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                    continue;
                }

                // View selected post with comment interaction
                Post selectedPost = posts.get(postIndex);
                Comment.viewPostWithComments(selectedPost, user, scanner);

            } else if (choice.equals("2")) {
                // Search posts by keyword
                System.out.print("\nEnter a keyword or phrase to search in posts: ");
                String keyword = scanner.nextLine().toLowerCase();

                System.out.println("\n=== Search Results for: \"" + keyword + "\" ===");
                boolean found = false;

                // search in post contents
                ArrayList<Post> matchingPosts = new ArrayList<>();
                for (Post post : postMain.getAllPosts()) {
                    if (post.getPostContent().toLowerCase().contains(keyword)) {
                        matchingPosts.add(post);
                        found = true;
                    }
                }

                // Print matching posts
                for (int i = 0; i < matchingPosts.size(); i++) {
                    Post post = matchingPosts.get(i);
                    System.out.println((i + 1) + ". [" + post.getUnitRelation().getUnitName() + "] " + post.getPostAuthor().getstudentName() + ": " + post.getPostContent());
                }

                if (!found) {
                    System.out.println("No posts matched your search.");
                }
                System.out.print("\nEnter the number of the post to view (or 0 to go back): ");
                String postChoice = scanner.nextLine();

                if (postChoice.equals("0")) {
                    continue;
                }

                int postIndex = -1;

                try {
                    postIndex = Integer.parseInt(postChoice) - 1;
                    if (postIndex < 0 || postIndex >= matchingPosts.size()) {
                    System.out.println("\nInvalid selection.");
                    continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                    continue;
                }

                // View selected post with comment interaction
                Post selectedPost = matchingPosts.get(postIndex);
                Comment.viewPostWithComments(selectedPost, user, scanner);

            } else if (choice.equals("3")) {
                break;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
