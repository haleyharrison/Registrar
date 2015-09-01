import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;

public class StudentTest{

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst(){
    assertEquals(0, Student.all().size());
  }

  @Test
  public void equals_returnsTrueIfInputIsTheSame() {
    Student myStudent = new Student ("Harry Potter", "September 1, 1991");
    Student myStudent2 = new Student ("Harry Potter", "September 1, 1991");
    assertTrue(myStudent.equals(myStudent2));
  }

  @Test
  public void save_savesCorrectlyIntoDatabase(){
    Student myStudent = new Student ("Harry Potter", "September 1, 1991");
    myStudent.save();
    Student savedStudent = Student.all().get(0);
    assertEquals(myStudent, savedStudent);
  }

  @Test
  public void getId_returnsId(){
    Student myStudent = new Student ("Harry Potter", "September 1, 1991");
    myStudent.save();
    Student savedStudent = Student.all().get(0);
    assertEquals(savedStudent.getId(), myStudent.getId());
  }

  @Test
  public void getName_returnsName() {
    Student myStudent = new Student ("Harry Potter", "September 1, 1991");
    myStudent.save();
    assertEquals("Harry Potter", myStudent.getName());
  }

  @Test
  public void getEnrollment_returnsGetEnrollment() {
    Student myStudent = new Student ("Harry Potter", "September 1, 1991");
    myStudent.save();
    assertEquals("September 1, 1991", myStudent.getEnrollmentDay());
  }

  @Test
  public void find_findsStudentInDatabase_True() {
    Student myStudent = new Student ("Harry Potter", "September 1, 1991");
    myStudent.save();
    Student savedStudent = Student.find(myStudent.getId());
    assertTrue(myStudent.equals(savedStudent));
  }

  @Test
  public void updateName_changesName(){
    Student myStudent = new Student ("Harry Potter", "September 1, 1991");
    myStudent.save();
    myStudent.updateName("Ron Weasley");
    Student savedStudent = Student.find(myStudent.getId());
    assertEquals("Ron Weasley", savedStudent.getName());
  }

  @Test
  public void updateEnrollment_changesEnrollment(){
    Student myStudent = new Student ("Harry Potter", "September 1, 1991");
    myStudent.save();
    myStudent.updateEnrollment("September 2, 1991");
    Student savedStudent = Student.find(myStudent.getId());
    assertEquals("September 2, 1991", savedStudent.getEnrollmentDay());
  }

  @Test
  public void delete_DeletesFromDatabase(){
    Student myStudent = new Student ("Harry Potter", "September 1, 1991");
    myStudent.save();
    myStudent.delete();
    Student savedStudent = Student.find(myStudent.getId());
    assertEquals(false, myStudent.equals(savedStudent));
  }

  @Test
  public void getCourses_returnsCourses(){
    Course myCourse = new Course ("Potions", "P - 204");
    myCourse.save();
    Student myStudent = new Student ("Hermione Granger", "September 1, 1991");
    myStudent.save();
    myStudent.addCourse(myCourse);
    assertEquals(myStudent.getCourses().size(), 1);
  }

}
