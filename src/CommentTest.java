import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

public class CommentTest {
    @Test
    public void testCommentCreation() {
        Student author = new Student("4888888", "Alice");
        Comment comment = new Comment(author, "This is a comment");

        assertEquals(author, comment.commentAuthor); //assigns author
        assertEquals("This is a comment", comment.commentContent); // assigns content
        assertTrue(comment.replies.isEmpty()); // brand new comment
    }

    @Test
    public void testAddSingleReply() {
        Student author = new Student("4888888", "Alice");
        Comment parent = new Comment(author, "Parent");
        Comment reply = new Comment(author, "Reply");

        parent.addReply(reply);

        assertEquals(1, parent.replies.size());
        assertEquals("Reply", parent.replies.get(0).commentContent);
    }   

}
