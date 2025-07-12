import java.util.ArrayList;
import java.util.Scanner;

public class Comment {

    //INSTANCE VARIABLES
    Student commentAuthor;                 
    String commentContent;               
    ArrayList<Comment> replies;          

    // CONSTRUCTOR
    public Comment(Student author, String content) {
        this.commentAuthor = author;
        this.commentContent = content;
        this.replies = new ArrayList<>(); // Initialise replies list
    }


    // Adds a reply to comment
    public void addReply(Comment reply) {
        replies.add(reply);
    }

    /*
     Static method to display a post and interact with its comments.
     Displays a post and its comments, and allows users to add or reply to comments.
     Uses loops, conditionals, and input handling for interaction.
     Comments are displayed using a recursive structure.
     */
     
    public static void viewPostWithComments(Post post, Student user, Scanner scanner) {
        // Display post information
        System.out.println("\n--- Post by " + post.getPostAuthor().getstudentName() + " ---");
        System.out.println(post.getPostContent());

        ArrayList<Comment> comments = post.getPostComments();

        // If no comments exist yet
        if (comments.isEmpty()) {
            System.out.println("\nNo comments yet.");
        }

        // Otherwise, display all existing comments using recursive print method
        if (!comments.isEmpty()) {
            System.out.println("\n--- Comments ---");
            for (int i = 0; i < comments.size(); i++) {
                System.out.print((i + 1) + ". ");
                comments.get(i).printCommentTree(" ");
            }
        }

        //COMMENT INTERACTION LOOP
        // Continues until the user adds a comment, replies, or exits
        while (true) {
            System.out.println("\n===== Options =====");
            System.out.println("1. Add a new comment");
            if (!comments.isEmpty()) {
                System.out.println("2. Reply to an existing comment");
            }
            System.out.println("0. Go back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.print("Enter your comment: ");
                String content = scanner.nextLine();

                Comment newComment = new Comment(user, content);
                post.getPostComments().add(newComment); 

                System.out.println("Comment added!");
                break; 

            } else if (choice.equals("2") && !comments.isEmpty()) {
                System.out.print("Enter the number of the comment to reply to: ");
                String replyToStr = scanner.nextLine();
                int replyToIndex = -1;

                // this handles non-numeric input
                try {
                    replyToIndex = Integer.parseInt(replyToStr) - 1;
                    if (replyToIndex < 0 || replyToIndex >= comments.size()) {
                        System.out.println("Invalid comment number.");
                        continue; // Skip rest of loop and prompt again
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                    continue;
                }

                // Create a reply to the selected comment
                Comment parentComment = comments.get(replyToIndex);
                System.out.print("Enter your reply: ");
                String replyContent = scanner.nextLine();

                Comment reply = new Comment(user, replyContent);
                parentComment.addReply(reply); // Add as nested reply

                System.out.println("Reply added!");
                break; 
            } else if (choice.equals("0")) {
                break;
            } else {
                // Handle any invalid option
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /*
     Recursively prints a comment and its replies in a tree-like structure.
     Demonstrates the use of recursion by calling the method on each reply to build a threaded comment view.
     Indent String used to visually indent replies
     */

    public void printCommentTree(String indent) {
        System.out.println(indent + "  " + commentAuthor.getstudentName() + ": " + commentContent);
        for (Comment reply : replies) {
            // Recursive call to print each nested reply
            reply.printCommentTree(indent + "    -->");
        }
    }
}
