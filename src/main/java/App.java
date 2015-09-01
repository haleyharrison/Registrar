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

  }
}
