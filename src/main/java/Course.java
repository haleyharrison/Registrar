import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;

public class Course {
  private int id;
  private String course_name;
  private String course_number;

  public int getId(){
    return id;
  }

  public String getCourseName(){
    return course_name;
  }

  public String getCourseNumber(){
    return course_number;
  }

  public Course(String course_name, String course_number){
    this.course_name = course_name;
    this.course_number = course_number;
  }

  public static List<Course> all(){
    String sql = "SELECT id, course_name, course_number FROM courses";
    try(Connection con = DB.sql2o.open()){
      return con.createQuery(sql).executeAndFetch(Course.class);
    }
  }

  @Override
  public boolean equals (Object otherCourse) {
    if (!(otherCourse instanceof Course)) {
      return false;
    } else {
      Course newCourse = (Course) otherCourse;
      return this.getCourseName().equals(newCourse.getCourseName()) &&
             this.getCourseNumber().equals(newCourse.getCourseNumber()) &&
             this.getId() == newCourse.getId();
    }
  }

  public void save(){
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO courses (course_name, course_number) VALUES (:course_name, :course_number)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("course_name", course_name)
      .addParameter("course_number", course_number)
      .executeUpdate()
      .getKey();
    }
  }

  public static Course find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM courses WHERE id = :id";
      Course course = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Course.class);
      return course;
    }
  }

  public void updateCourse(String course_name, String course_number) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "UPDATE courses SET course_name = :course_name, course_number = :course_number WHERE id = :id";
      con.createQuery(sql)
        .addParameter("course_name", course_name)
        .addParameter("course_number", course_number)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()){
      String deleteQuery = "DELETE FROM courses WHERE id=:id";
        con.createQuery(deleteQuery)
          .addParameter("id", id)
          .executeUpdate();

      String joinDeleteQuery = "DELETE FROM students_courses WHERE course_id =:course_id";
        con.createQuery(joinDeleteQuery)
          .addParameter("course_id", this.getId())
          .executeUpdate();
    }
  }

  public List<Student> getStudents(){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT students.* FROM"+
       " courses" +
      " JOIN students_courses ON (courses.id = students_courses.course_id)" +
      " JOIN students ON (students_courses.student_id = students.id)" +
      " WHERE courses.id =:id";
      List<Student> students = con.createQuery(sql)
          .addParameter("id", this.id)
          .executeAndFetch(Student.class);
          return students;

    }
  }

  public void addStudent(Student student){
      try(Connection con = DB.sql2o.open()){
        String sql = "INSERT INTO students_courses (student_id, course_id) VALUES (:student_id, :course_id)";
        con.createQuery(sql)
          .addParameter("student_id", student.getId())
          .addParameter("course_id", id)
          .executeUpdate();
      }
  }

  //HI, we forgot to add "(student_id, course_id)" on line 109. So this method wasn't adding anything to course_id. 

}
