import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;



public class Student{
  private int id;
  private String name;
  private String enrollment_day;

  public int getId(){
    return id;
  }

  public String getName(){
    return name;
  }

  public String getEnrollmentDay(){
    return enrollment_day;
  }

  public Student(String name, String enrollment_day){
    this.name = name;
    this.enrollment_day = enrollment_day;
  }

  public static List<Student> all(){
    String sql = "SELECT id, name, enrollment_day FROM students";
    try(Connection con = DB.sql2o.open()){
      return con.createQuery(sql).executeAndFetch(Student.class);
    }
  }


  @Override
  public boolean equals (Object otherStudent) {
    if (!(otherStudent instanceof Student)) {
      return false;
    } else {
      Student newStudent = (Student) otherStudent;
      return this.getName().equals(newStudent.getName()) &&
             this.getEnrollmentDay().equals(newStudent.getEnrollmentDay()) &&
             this.getId() == newStudent.getId();
    }
  }

  public void save(){
    try (Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO students (name, enrollment_day) VALUES (:name, :enrollment_day)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("name", name)
      .addParameter("enrollment_day", enrollment_day)
      .executeUpdate()
      .getKey();
    }
  }

  public static Student find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM students WHERE id = :id";
      Student student = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Student.class);
        return student;
    }
  }

  public void updateName(String name) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "UPDATE students SET name = :name WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("id", id)
        .executeUpdate();
    }
  }


    public void updateEnrollment(String enrollment_day) {
      try (Connection con = DB.sql2o.open()) {
        String sql = "UPDATE students SET enrollment_day=:enrollment_day WHERE id=:id";
        con.createQuery(sql)
          .addParameter("enrollment_day", enrollment_day)
          .addParameter("id", id)
          .executeUpdate();
      }
    }

    public void delete() {
      try (Connection con = DB.sql2o.open()) {
        String deleteQuery = "DELETE FROM students WHERE id =:id;";
          con.createQuery(deleteQuery)
            .addParameter("id", id)
            .executeUpdate();

        String joinDeleteQuery = "DELETE FROM students_courses WHERE student_id =:student_id";
          con.createQuery(joinDeleteQuery)
            .addParameter("student_id", this.getId())
            .executeUpdate();
      }
    }

    public void addCourse(Course course){
        try(Connection con = DB.sql2o.open()){
          String sql = "INSERT INTO students_courses VALUES (:student_id, :course_id)";
          con.createQuery(sql)
            .addParameter("student_id", this.getId())
            .addParameter("course_id", course.getId())
            .executeUpdate();
        }
    }





}
