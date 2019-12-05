/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import static Models.DBConnection.writeQuery;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author WH1108
 */
public class Section {

    private int sectionNumber;
    private String sectionLab;
    private String sectionInstructor;
    private String days;
    private String startTime;
    private String endTime;

    String query;
    private Map statusMap;

    PreparedStatement prestatement;
    DBConnection connection = DBConnection.getDbConnection();

    public Section() {

    }

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
        return this.startTime != null && this.endTime != null && this.days != null
                && this.sectionLab != null && this.sectionNumber != 0 && this.sectionInstructor != null;
    }

    public ObservableList<Section> getSections(String courseName) throws ClassNotFoundException, SQLException {
        //getStatement();
        ObservableList sectionList = FXCollections.observableArrayList();
        query = "SELECT * FROM section s,instructor i WHERE coursename=? AND s.instructorid=i.id";
        writeQuery(connection.getLoggedStudent(), query);
        prestatement
                = connection.getStatement("SELECT * FROM section s,instructor i WHERE coursename=? AND s.instructorid=i.id");
        prestatement.setString(1, courseName);
        connection.setResultSet(prestatement.executeQuery());
        while (connection.getResultSet().next()) {
            Section section
                    = new Section(
                            connection.getResultSet().getInt("sectionnumber"),
                            connection.getResultSet().getString("lab"),
                            connection.getResultSet().getString("name"),
                            connection.getResultSet().getString("days"),
                            connection.getResultSet().getString("starttime"),
                            connection.getResultSet().getString("endtime")
                    );
            sectionList.add(section);
        }
        return sectionList;
    }

    public int deleteSection(String sectionName, int sectionNumber) throws ClassNotFoundException, SQLException {
        //getStatement();
        query = "DELETE FROM regcourses WHERE coursename=? AND sectionnumber=? AND studentid=?";
        writeQuery(connection.getLoggedStudent(), query);
        prestatement = connection.getStatement("DELETE FROM regcourses WHERE coursename=? AND sectionnumber=? AND studentid=?");
        prestatement.setString(1, sectionName);
        prestatement.setInt(2, sectionNumber);
        prestatement.setString(3, connection.getLoggedStudent());
        int success = prestatement.executeUpdate();
        if (success != 0) {
            return 1;
        }
        return 0;
    }

    public Map updateSection(String sectionName, int sectionNumber, int preSectionNumber) throws ClassNotFoundException, SQLException {
        statusMap = new HashMap();
        statusMap.put("updated", false);
        statusMap.put("conflict", false);
        //getStatement();
        query = "SELECT days,starttime,endtime FROM section WHERE coursename=? AND sectionnumber=?";
        writeQuery(connection.getLoggedStudent(), query);
        prestatement = connection.getStatement("SELECT days,starttime,endtime FROM section WHERE coursename=? AND sectionnumber=?");
        prestatement.setString(1, sectionName);
        prestatement.setInt(2, sectionNumber);
        connection.setResultSet(prestatement.executeQuery());
        String days = "";
        String starttime = "";
        String endtime = "";
        while (connection.getResultSet().next()) {
            days = connection.getResultSet().getString("days");
            starttime = connection.getResultSet().getString("starttime");
            endtime = connection.getResultSet().getString("endtime");
        }
        query = "SELECT * FROM regcourses WHERE starttime=? AND endtime=? AND days=? AND studentid=?";
        writeQuery(connection.getLoggedStudent(), query);
        prestatement = connection.getStatement("SELECT * FROM regcourses WHERE starttime=? AND endtime=? AND days=? AND studentid=?");
        prestatement.setString(1, starttime);
        prestatement.setString(2, endtime);
        prestatement.setString(3, days);
        prestatement.setString(4, connection.getLoggedStudent());
        connection.setResultSet(prestatement.executeQuery());
        if (connection.getResultSet().next()) {
            statusMap.put("conflict", true);
        } else {
            query = "UPDATE regcourses SET sectionnumber=?,days=?"
                    + " WHERE coursename=? AND sectionnumber=? AND studentid=?";
            writeQuery(connection.getLoggedStudent(), query);
            prestatement = connection.getStatement("UPDATE regcourses SET sectionnumber=?,days=?"
                    + " WHERE coursename=? AND sectionnumber=? AND studentid=?");
            prestatement.setInt(1, sectionNumber);
            prestatement.setString(2, days);
            prestatement.setString(3, sectionName);
            prestatement.setInt(4, preSectionNumber);
            prestatement.setString(5, connection.getLoggedStudent());
            prestatement.executeUpdate();
            statusMap.put("updated", true);
        }
        return statusMap;
    }

    //db courses
    public Map addSection(String sectionName, int sectionNumber) throws ClassNotFoundException, SQLException {
        statusMap = new HashMap();
        statusMap.put("isAdded", false);
        statusMap.put("isExist", false);
        statusMap.put("conflictExist", false);

        //getStatement();
        query = "SELECT * FROM regcourses WHERE coursename=? AND studentid=?";
        writeQuery(connection.getLoggedStudent(), query);
        prestatement = connection.getStatement("SELECT * FROM regcourses WHERE coursename=? AND studentid=?");
        prestatement.setString(1, sectionName);
        prestatement.setString(2, connection.getLoggedStudent());
        connection.setResultSet(prestatement.executeQuery());
        if (connection.getResultSet().next()) {
            statusMap.put("isExist", true);
        } else {
            query = "SELECT * FROM section s,instructor i "
                    + "WHERE coursename=? AND sectionnumber=? AND s.instructorid=i.id";
            writeQuery(connection.getLoggedStudent(), query);
            prestatement = connection.getStatement("SELECT * FROM section s,instructor i "
                    + "WHERE coursename=? AND sectionnumber=? AND s.instructorid=i.id");
            prestatement.setString(1, sectionName);
            prestatement.setInt(2, sectionNumber);

            connection.setResultSet(prestatement.executeQuery());
            String instructor = "";
            String starttime = "";
            String endtime = "";
            String days = "";
            String lab = "";
            while (connection.getResultSet().next()) {
                instructor = connection.getResultSet().getString("name");
                starttime = connection.getResultSet().getString("starttime");
                endtime = connection.getResultSet().getString("endtime");
                days = connection.getResultSet().getString("days");
                lab = connection.getResultSet().getString("lab");
            }
            query = "SELECT * FROM regcourses "
                    + "WHERE starttime=? AND endtime=? AND days=? AND studentid=?";
            writeQuery(connection.getLoggedStudent(), query);
            prestatement = connection.getStatement("SELECT * FROM regcourses "
                    + "WHERE starttime=? AND endtime=? AND days=? AND studentid=?");
            prestatement.setString(1, starttime);
            prestatement.setString(2, endtime);
            prestatement.setString(3, days);
            prestatement.setString(4, connection.getLoggedStudent());
            connection.setResultSet(prestatement.executeQuery());
            if (connection.getResultSet().next()) {
                statusMap.put("conflictExist", true);
            } else {
                int semesterNumber;
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int month;
                if (Calendar.getInstance().get(Calendar.MONTH) < 6) {
                    semesterNumber = 2;
                } else {
                    semesterNumber = 1;
                }
                query = "INSERT INTO regcourses "
                        + "VALUES(stdId,sectionName,starttime,endtime,sectionNumber,instructor,lab,days)";
                writeQuery(connection.getLoggedStudent(), query);
                prestatement
                        = connection.getStatement("INSERT INTO regcourses "
                                + "VALUES(?,?,?,?,?,?,?,?,?,?)");
                prestatement.setString(1, connection.getLoggedStudent());
                prestatement.setString(2, sectionName);
                prestatement.setString(3, starttime);
                prestatement.setString(4, endtime);
                prestatement.setInt(5, sectionNumber);
                prestatement.setString(6, instructor);
                prestatement.setString(7, lab);
                prestatement.setString(8, days);
                prestatement.setInt(9, semesterNumber);
                prestatement.setInt(10, year);

                int success = prestatement.executeUpdate();
                if (success != 0) {
                    statusMap.put("isAdded", true);
                }
            }
        }
        return statusMap;
    }

    public int[] getArrayofSections(String courseName) throws SQLException, ClassNotFoundException {
        int length = 0;
        query = "SELECT sectionnumber FROM section WHERE coursename=?";
        writeQuery(connection.getLoggedStudent(), query);
        prestatement = connection.getStatement("SELECT sectionnumber FROM section WHERE coursename=?");
        prestatement.setString(1, courseName);
        connection.setResultSet(prestatement.executeQuery());
        while (connection.getResultSet().next()) {
            length++;
        }
        connection.getResultSet().beforeFirst();
        int count = 0;
        int[] sections = new int[length];
        while (connection.getResultSet().next()) {
            sections[count] = connection.getResultSet().getInt("sectionnumber");
            count++;
        }
        return sections;
    }

}
