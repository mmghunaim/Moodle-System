/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

/**
 *
 * @author WH1108
 */
public class RegisteredCourses {
    private String studentid;
    private String coursename;
    private int sectionnumber;
    private String starttime;
    private String endtime;
    private String instructor;
    private String lab;
    private String days;

    public RegisteredCourses(String studentid, String coursename, int sectionnumber, String starttime, String endtime, String instructor, String lab, String days) {
        this.studentid = studentid;
        this.coursename = coursename;
        this.sectionnumber = sectionnumber;
        this.starttime = starttime;
        this.endtime = endtime;
        this.instructor = instructor;
        this.lab = lab;
        this.days = days;
    }
    
    public RegisteredCourses(String coursename, int sectionnumber) {
        this.coursename = coursename;
        this.sectionnumber = sectionnumber;
    }
    
    public String getStudentid() {
        return studentid;
    }

    public String getCoursename() {
        return coursename;
    }

    public int getSectionnumber() {
        return sectionnumber;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getLab() {
        return lab;
    }

    public String getDays() {
        return days;
    }
    
    
}
