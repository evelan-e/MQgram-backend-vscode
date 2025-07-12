import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class StudentTest {
    @Test
    public void testStudentCreation() {
        Student s = new Student("48888888", "Alice");
        assertEquals("48888888", s.getstudentID());
        assertEquals("Alice", s.getstudentName());
        assertTrue(s.getstudentCourse().isEmpty()); //brand  new student should have nothing.
        assertTrue(s.getstudentPosts().isEmpty());
        assertTrue(s.getstudentFollowers().isEmpty());
    }

    public void testToCSV() {
        Student s = new Student("488888", "First Surname");
        assertEquals("488888,First Surname", s.toCSV());
    }

// following/followers tests 
    @Test
    public void testFollowStudentAdds() {
        Student alice = new Student("48888888", "Alice");
        Student bob = new Student("12345678", "Bob");
        List<Student> allStudents = new ArrayList<>();
        allStudents.add(alice);
        allStudents.add(bob);

        alice.followStudent("12345678", allStudents);

        assertTrue(alice.getstudentFollowing().contains(bob));
        assertTrue(bob.getstudentFollowers().contains(alice));
        assertEquals(1, alice.getFollowingCount()); // count should increase
        assertEquals(1, bob.getFollowerCount());
    }

    @Test
    public void testDuplicateFollow() {
        Student alice = new Student("48888888", "Alice");
        Student bob = new Student("12345678", "Bob");
        List<Student> allStudents = new ArrayList<>();
        allStudents.add(alice);
        allStudents.add(bob);

        alice.followStudent("12345678", allStudents); 
        alice.followStudent("12345678", allStudents); // alice tries to follow bob twice, should not duplicate

        assertTrue(alice.getstudentFollowing().contains(bob));
        assertEquals(1, alice.getFollowingCount());
        assertEquals(1, bob.getFollowerCount());
    }

    @Test
    public void testFollowSelf() {
        Student alice = new Student("48888888", "Alice");
        List<Student> allStudents = new ArrayList<>();
        allStudents.add(alice);
        
        alice.followStudent("48888888", allStudents); // follow self
        
        assertEquals(0, alice.getFollowingCount()); // should not be added to own following list
        assertEquals(0, alice.getFollowerCount());
        assertFalse(alice.getstudentFollowing().contains(alice));
        assertFalse(alice.getstudentFollowers().contains(alice));
    }

}
