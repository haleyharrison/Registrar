import java.util.HashMap;
import java.util.List;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;


public class App {
  public static void main(String[] args){
    //staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      List<Course> courses = Course.all();
      List<Student> students = Student.all();
      model.put("students", students);
      model.put("courses", courses);
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/courses", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String courseName = request.queryParams("courseName");
      String courseNumber = request.queryParams("courseNumber");
      Course newCourse = new Course(courseName, courseNumber);
      newCourse.save();
      response.redirect("/");
      return null;
    });

    post("/courses_delete", (request, response) -> {
      int courseId = Integer.parseInt(request.queryParams("course_id"));
      Course deadCourse = Course.find(courseId);
      deadCourse.delete();
      response.redirect("/");
      return null;
    });

    post("/students", (request, response) -> {
      //HashMap<String, Object> model = new HashMap<String, Object>();
      String studentName = request.queryParams("studentName");
      String studentEnrollmentDate = request.queryParams("enrollmentDate");
      Student newStudent = new Student(studentName, studentEnrollmentDate);
      newStudent.save();
      response.redirect("/");
      return null;
    });

    post("/students_delete", (request, response) -> {
      int studentId = Integer.parseInt(request.queryParams("student_id"));
      Student deadStudent = Student.find(studentId);
      deadStudent.delete();
      response.redirect("/");
      return null;
    });

    get("/courses/:course_id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int course_id = Integer.parseInt(request.params("course_id"));
      Course course = Course.find(course_id);
      List<Student> studentsInCourse = course.getStudents();
      model.put("studentsInCourse", studentsInCourse);
      model.put("course", course);
      model.put("allStudents", Student.all());
      model.put("template", "templates/course.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/courses/:course_id/add", (request, response) -> {
      int courseId = Integer.parseInt(request.queryParams("course_id"));
      Course course = Course.find(courseId);
      int newStudentId = Integer.parseInt(request.queryParams("studentid"));
      Student newStudent = Student.find(newStudentId);
      newStudent.addCourse(course);
      // model.put("course", course);
      // model.put("allStudents", Student.all());
      String courseIdPath = String.format("/courses/%d", courseId);
      response.redirect(courseIdPath);
      return null;
    });

    // get("/students/:student_id", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   int studentId = Integer.parseInt(request.queryParams("student_id"));
    //   Student student = Student.find(studentId);
    //   List<Course> thisKidsClasses = student.getCourses();
    //   model.put("thisKidsClasses", thisKidsClasses);
    //   model.put("student", student);
    //   model.put("allCourses", Course.all());
    //   model.put("template", "templates/student.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());

    // post("/students/:student_id/add", (request, response) -> {
    //   int studId = Integer.parseInt(request.queryParams("student_id"));
    //   Student student = Student.find(studId);
    //   int newCourseId = Integer.parseInt(request.queryParams("courseid"));
    //   Course newCourse = Course.find(newCourseId);
    //   newCourse.addStudent(student);
    //   String studIdPath = String.format("/students/%d", studId);
    //   response.redirect(studIdPath);
    //   return null;
    // });
  }
}
