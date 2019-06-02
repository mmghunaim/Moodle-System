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
public class Section {
    private int sectionNumber ;
    private String sectionLab ; 
    private String sectionInstructor ; 
    private String days ;
    private String startTime ;
     private String endTime ;

    public Section(int sectionNumber, String sectionLab, String sectionInstructor, String days, String startTime, String endTime) {
        this.sectionNumber = sectionNumber;
        this.sectionLab = sectionLab;
        this.sectionInstructor = sectionInstructor;
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public String getSectionLab() {
        return sectionLab;
    }

    public String getSectionInstructor() {
        return sectionInstructor;
    }

    public String getDays() {
        return days;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public boolean isValid() {
        return this.startTime != null && this.endTime  != null && this.days  != null 
                && this.sectionLab != null && this.sectionNumber  != 0 && this.sectionInstructor  != null;
    }
    
    
}
