import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;

public class CourseTest{

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst(){
    assertEquals(0, Course.all().size());
  }

  @Test
  public void equals_returnsTrueIfInputIsTheSame() {
    Course myCourse = new Course ("Herbology", "H - 101");
    Course myCourse2 = new Course ("Herbology", "H - 101");
    assertTrue(myCourse.equals(myCourse2));
  }

  @Test
  public void save_savesCorrectlyIntoDatabase(){
    Course myCourse = new Course ("Herbology", "H - 101");
    myCourse.save();
    Course savedCourse = Course.all().get(0);
    assertEquals(myCourse, savedCourse);
  }

  @Test
  public void getId_returnsId(){
    Course myCourse = new Course ("Herbology", "H - 101");
    myCourse.save();
    Course savedCourse = Course.all().get(0);
    assertEquals(savedCourse.getId(), myCourse.getId());
  }

  @Test
  public void getCourseName_returnsCourseName(){
    Course myCourse = new Course ("Herbology", "H-101");
    myCourse.save();
    Course savedCourse = Course.all().get(0);
    assertEquals(savedCourse.getCourseName(), myCourse.getCourseName());
  }

  @Test
  public void find_findsCourseInDatabase_True() {
    Course myCourse = new Course ("Herbology", "H - 101");
    myCourse.save();
    Course savedCourse = Course.find(myCourse.getId());
    assertTrue(myCourse.equals(savedCourse));
  }

  @Test
  public void updateCourse_changesCourse() {
    Course myCourse = new Course ("Herbology", "H - 101");
    myCourse.save();
    myCourse.updateCourse("Potions", "P - 201");
    Course savedCourse = Course.find(myCourse.getId());
    assertEquals("Potions", savedCourse.getCourseName());
    assertEquals("P - 201", savedCourse.getCourseNumber());
  }

  @Test
  public void delete_DeletesFromDatabase(){
    Course myCourse = new Course ("Herbology", "H - 101");
    myCourse.save();
    myCourse.delete();
    Course savedCourse = Course.find(myCourse.getId());
    assertEquals(false, myCourse.equals(savedCourse));
  }

  @Test
  public void getStudents_returnsStudents(){
    Student myStudent = new Student ("Harry Potter", "September 1, 1991");
    myStudent.save();
    Course myCourse = new Course ("Herbology", "H - 101");
    myCourse.save();
    myStudent.addCourse(myCourse);
    assertEquals(myStudent, myCourse.getStudents());
  }

}
