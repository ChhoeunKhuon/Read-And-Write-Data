import lombok.*;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.Table;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.Random;
import java.util.Scanner;

interface CourseService{
    void addNewCourse(String addCourse);
    void getAllCourse(String allCourse);
    void getCourseById(int courseUuid);
}
class CourseServiceImp implements CourseService{
    public static boolean avilable = true;
    String isAvailable = avilable ? "Yes":"No";
    @Override
    public void addNewCourse(String addCourse) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("PrintOut/course.csv", true));
            Course newCourse = new Course();
            Random random = new Random();
            int courseId = random.nextInt(100);
            newCourse.setCourseId(String.valueOf(courseId));
            newCourse.setCourseTitle(addCourse);
            newCourse.setCourseStartedDate(LocalDate.now().toString());
            newCourse.setCourseEndDate(LocalDate.now().plusMonths(1).toString());
            newCourse.setAvailable(avilable);

            String data = newCourse.getCourseId() + "," + newCourse.getCourseTitle() + "," + newCourse.getCourseStartedDate() + ","
                    + newCourse.getCourseEndDate() + "," + isAvailable + "\n";
            writer.write(data);
            writer.close();
            System.out.println("[+] Course Title has been added successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void getAllCourse(String allCourse) {
        try{
            Table table = new Table(5, BorderStyle.UNICODE_BOX_HEAVY_BORDER);
            for(int i = 0;i<5;i++){
                table.setColumnWidth(i,20,50);
            }
            table.addCell("UUID");
            table.addCell("Title");
            table.addCell("Started Date");
            table.addCell("End Date");
            table.addCell("isAvailable");
            BufferedReader reader = new BufferedReader(new FileReader("PrintOut/course.csv"));
            String data;
            while((data = reader.readLine()) != null){
                System.out.println("Data: "+data);
                String [] result = data.split(",");
                table.addCell(result[0],1);
                table.addCell(result[1],1);
                table.addCell(result[2],1);
                table.addCell(result[3],1);
                table.addCell(result[4],1);
            }
            System.out.println(table.render());
            reader.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
         }
    }

    @Override
    public void getCourseById(int courseUuid) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("PrintOut/course.csv"));
            String line;
            boolean found = false;
            Table table = new Table(5, BorderStyle.UNICODE_BOX_HEAVY_BORDER);
            table.addCell("UUID");
            table.addCell("Title");
            table.addCell("Started Date");
            table.addCell("End Date");
            table.addCell("Is Available");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int courseId = Integer.parseInt(parts[0]);
                if (courseId == courseUuid) {
                    table.addCell(parts[0],1);
                    table.addCell(parts[1],1);
                    table.addCell(parts[2],1);
                    table.addCell(parts[3],1);
                    table.addCell(parts[4],1);
                    found = true;
                    break;
                }
            }
            reader.close();
            if (found) {
                System.out.println("Course Details:");
                System.out.println(table.render());
            } else {
                System.out.println("Course with ID " + courseUuid + " not found.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Course {
    private String courseId;
    private String courseTitle;
    private String courseStartedDate;
    private String courseEndDate;
    private boolean isAvailable;

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseTitle='" + courseTitle + '\'' +
                ", courseStartedDate='" + courseStartedDate + '\'' +
                ", courseEndDate='" + courseEndDate + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
public class Main {
    public static void main(String[] args) {
        CourseService courseService = new CourseServiceImp();

        View.menu();

        int choice = new Scanner(System.in).nextInt();
        switch (choice) {
            case 1:
                System.out.print("Enter The Course Title: ");
                String courseTitle = new Scanner(System.in).nextLine();
                courseService.addNewCourse(courseTitle);
                break;
            case 2:
                System.out.println("Show Courses List: ");
                break;
            case 3:
                System.out.print("Enter The Course Id: ");
                int courseId = new Scanner(System.in).nextInt();
                courseService.getCourseById(courseId);
                break;
            case 4:
                System.out.println("Exit!");
                System.exit(0);
                break;
            default:
                System.out.println("Failed, Invalid Option!");
        }
    }
}