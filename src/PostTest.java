import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PostTest {
    @Test
    public void testPostCreator() {
        Student s = new Student("48227722", "Alice"); // create data for post 
        Unit u = new Unit(1, "COMP1010");
        Post p = new Post(1, "This is a test post.", s, u);

        assertEquals(1, p.getPostID());
        assertEquals("This is a test post.", p.getPostContent());
        assertEquals(s, p.getPostAuthor());
        assertEquals(u, p.getUnitRelation());
        assertTrue(p.getPostComments().isEmpty()); // new post should have no comments 
    }
}