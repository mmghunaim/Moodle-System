package Controllers;

import Models.Course;
import Models.RegisteredCourses;
import Models.Section;
import Models.Student;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SortEvent;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import Models.MySQLConnection;
import Index.Index;
import Models.MongoFacade;
import Models.MySQLFacade;
import Views.ViewFactory;

/**
 * FXML Controller class
 *
 * @author mmgunaim
 */
public class StudentController implements Initializable {

    @FXML
    private Tab tabPaneCurrentCourses;
    private TextArea textAreaCurrent;
    @FXML
    private TableColumn<Course, String> tableColumnCourseName;
    @FXML
    private TableColumn<Course, String> tableColumnCourseId;
    @FXML
    private TableView<Course> tableViewCourse;
    @FXML
    private TableView<Section> tableViewSection;
    @FXML
    private TableColumn<Section, Integer> tableColumnSectionNo;
    @FXML
    private TableColumn<Section, String> tableColumnSectionLab;
    @FXML
    private TableColumn<Section, String> tableColumnSectionInstructor;
    @FXML
    private TableColumn<Section, String> tableColumnSectionStartTime;
    @FXML
    private TableColumn<Section, String> tableColumnSectionEndTime;
    @FXML
    private TableColumn<Section, String> tableColumnDays;
    @FXML
    private TableView<RegisteredCourses> tableViewRegisteredCourses;
    @FXML
    private TableColumn<RegisteredCourses, String> tableColumnCourseNameReg;
    @FXML
    private TableColumn<RegisteredCourses, String> tableColumnCourseStartTime;
    @FXML
    private TableColumn<RegisteredCourses, String> tableColumnCourseEndTime;
    @FXML
    private TableColumn<RegisteredCourses, Integer> tableColumnCourseSecionNumber;
    @FXML
    private TableColumn<RegisteredCourses, String> tableColumnCourseLab;
    @FXML
    private TableColumn<RegisteredCourses, String> tableColumnCourseInstructorName;
    @FXML
    private TableColumn<RegisteredCourses, String> tableColumnCourseDays;
    @FXML
    private TableView<RegisteredCourses> tableViewUpdateCourse;
    @FXML
    private TableColumn<RegisteredCourses, String> tableColumnDeleteCourseName;
    @FXML
    private TableColumn<RegisteredCourses, Integer> tableColumnDeleteCourseSectionNumber;
    @FXML
    private ComboBox<String> comboBoxSemesters;
    @FXML
    private TableView<Course> tableViewGrades;
    @FXML
    private TableColumn<Course, String> tableColumnGradesCourseName;
    @FXML
    private TableColumn<Course, Double> tableColumnGradesCourseGrade;
    @FXML
    private Button goBack;
    @FXML
    private VBox vBoxTable;
    @FXML
    private Button buttonStudentLogout;
    @FXML
    private TextField textFiledStudentId;
    @FXML
    private TextField textFiledStudentName;
    @FXML
    private TextField textFiledStudentLastName;
    @FXML
    private TextField textFiledStudentAddress;
    @FXML
    private TextField textFiledStudentEmail;
    @FXML
    private Tab tabStudentInfo;

    Course course;
    Section section;
    String userName;
    MySQLConnection connection;
    Optional<ButtonType> result;
    private Alert alert;
    Map returnMap;

    ObservableList<String> semesterList = FXCollections.observableArrayList("First Semester of 2017/2018", "Second Semester of 2017/2018",
            "First Semester of 2018/2019", "Second Semester of 2018/2019");

    private String connectionType = "mysql";

    MySQLFacade mysqlFacade;
    MongoFacade mongoFacade;

    ViewFactory viewFactory;

    public StudentController() {
        if (connectionType.equalsIgnoreCase("mysql")) {
                mysqlFacade = MySQLFacade.getDatabaseFacade();
            } else {
                mongoFacade = MongoFacade.getMongoFacade();
            }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("initialize");
        try {
            viewFactory = ViewFactory.getViewFactory();
            comboBoxSemesters.setItems(semesterList);
            comboBoxSemesters.setValue("Choose Semester...");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleStudentBasicInfo(Event event) throws SQLException, ClassNotFoundException {
        System.out.println("What the hell?!");
        System.out.println("What return " + mysqlFacade);
        Student returnedStudent = mysqlFacade.showStudentBasicInfo();
        System.out.println(returnedStudent);
        System.out.println(returnedStudent.getStudentEmail());
        textFiledStudentName.setText(returnedStudent.getStudentName());
        textFiledStudentAddress.setText(returnedStudent.getStudentAddress());
        textFiledStudentLastName.setText(returnedStudent.getStudentLastName());
        textFiledStudentEmail.setText(returnedStudent.getStudentEmail());
        textFiledStudentId.setText(returnedStudent.getStudentId());
    }

    @FXML
    private void handelCurrentCourses(Event event) throws ClassNotFoundException, SQLException {
        tableColumnCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        tableColumnCourseId.setCellValueFactory(new PropertyValueFactory<>("courseId"));

        tableViewCourse.setItems(mysqlFacade.getCurrentCourses());

    }

    @FXML
    private void handleRegisteredCourses(Event event) throws ClassNotFoundException, SQLException {

        tableColumnCourseNameReg.setCellValueFactory(new PropertyValueFactory<>("coursename"));
        tableColumnCourseStartTime.setCellValueFactory(new PropertyValueFactory<>("starttime"));
        tableColumnCourseEndTime.setCellValueFactory(new PropertyValueFactory<>("endtime"));
        tableColumnCourseSecionNumber.setCellValueFactory(new PropertyValueFactory<>("sectionnumber"));
        tableColumnCourseLab.setCellValueFactory(new PropertyValueFactory<>("lab"));
        tableColumnCourseInstructorName.setCellValueFactory(new PropertyValueFactory<>("instructor"));
        tableColumnCourseDays.setCellValueFactory(new PropertyValueFactory<>("days"));

        tableViewRegisteredCourses.setItems(mysqlFacade.getRegisteredCourses(""));
    }

    @FXML
    private void handleUpdateCourses(Event event) throws ClassNotFoundException, SQLException {
        tableColumnDeleteCourseName.setCellValueFactory(new PropertyValueFactory<>("coursename"));
        tableColumnDeleteCourseSectionNumber.setCellValueFactory(new PropertyValueFactory<>("sectionnumber"));
        tableViewUpdateCourse.setItems(mysqlFacade.getRegisteredCourses(""));
    }

    @FXML
    private void clickedCourse(MouseEvent event) throws ClassNotFoundException, SQLException {
        if (event.getClickCount() == 2) {
            if (tableViewCourse.getSelectionModel().getSelectedItem() != null) {
                tableViewCourse.setVisible(false);
                tableViewSection.setVisible(true);

                course = tableViewCourse.getSelectionModel().getSelectedItem();
                String clickedCourse = course.getCourseName();

                tableColumnSectionNo.setCellValueFactory(new PropertyValueFactory<>("sectionNumber"));
                tableColumnSectionLab.setCellValueFactory(new PropertyValueFactory<>("sectionLab"));
                tableColumnSectionInstructor.setCellValueFactory(new PropertyValueFactory<>("sectionInstructor"));
                tableColumnSectionStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
                tableColumnSectionStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
                tableColumnSectionEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
                tableColumnDays.setCellValueFactory(new PropertyValueFactory<>("days"));

                tableViewSection.setItems(mysqlFacade.getSections(clickedCourse));
            }
        }
    }

    @FXML
    private void clickedSection(MouseEvent event) throws ClassNotFoundException, SQLException {
        if (event.getClickCount() == 2) {
            if (tableViewSection.getSelectionModel().getSelectedItem() != null) {
                section = tableViewSection.getSelectionModel().getSelectedItem();
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText(null);
                alert.setContentText("ADD THIS SECTION ?!");
                ButtonType okButton = new ButtonType("Yes", ButtonData.YES);
                ButtonType cancelButton = new ButtonType("No", ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(okButton, cancelButton);
                result = alert.showAndWait();
                if (result.get() == okButton) {
                    returnMap = mysqlFacade.addSection(course.getCourseName(), section.getSectionNumber());
                    if ((boolean) returnMap.get("isAdded")) {
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success Message");
                        alert.setContentText("INSERTED COMPLETE");
                        alert.setHeaderText(null);
                        alert.showAndWait();
                        tableViewRegisteredCourses.setItems(mysqlFacade.getRegisteredCourses(""));
                        tableViewCourse.setVisible(true);
                        tableViewSection.setVisible(false);
                    } else if ((boolean) returnMap.get("isExist")) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setContentText("Course Already Exist");
                        alert.setHeaderText(null);
                        alert.showAndWait();
                        tableViewCourse.setVisible(true);
                        tableViewSection.setVisible(false);
                    } else if ((boolean) returnMap.get("conflictExist")) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setContentText("Conflict With Other Course Exist");
                        alert.setHeaderText(null);
                        alert.showAndWait();
                        tableViewCourse.setVisible(true);
                        tableViewSection.setVisible(false);
                    }
                } else {
                    tableViewCourse.setVisible(true);
                    tableViewSection.setVisible(false);
                }
            }

        }
    }

    @FXML
    private void clickedDeleteSection(MouseEvent event) throws ClassNotFoundException, SQLException {
        if (event.getClickCount() == 2) {
            if (tableViewRegisteredCourses.getSelectionModel().getSelectedItem() != null) {
                RegisteredCourses registeredCourses = tableViewRegisteredCourses.getSelectionModel().getSelectedItem();
                System.out.println(registeredCourses.getCoursename());
                String courseName = registeredCourses.getCoursename();
                int sectionNumber = registeredCourses.getSectionnumber();
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText(null);
                alert.setContentText("DELETE THIS SECTION ?!");
                ButtonType okButton = new ButtonType("Yes", ButtonData.YES);
                ButtonType cancelButton = new ButtonType("No", ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(okButton, cancelButton);
                result = alert.showAndWait();
                if (result.get() == okButton) {
                    int deleted = mysqlFacade.deleteSection(courseName, sectionNumber);
                    if (deleted != 0) {
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success Message");
                        alert.setContentText("REMOVED COMPLETE");
                        alert.setHeaderText(null);
                        alert.showAndWait();
                        tableViewRegisteredCourses.setItems(mysqlFacade.getRegisteredCourses(""));
                    }
                }
            }
        }
    }

    @FXML
    private void clickedUpdateSection(MouseEvent event) throws ClassNotFoundException, SQLException {
        if (event.getClickCount() == 2) {
            if (tableViewUpdateCourse.getSelectionModel().getSelectedItem() != null) {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText(null);
                alert.setContentText("DO YOU WANT UPDATE THIS COURSE ?!");
                ButtonType okButton = new ButtonType("Yes", ButtonData.YES);
                ButtonType cancelButton = new ButtonType("No", ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(okButton, cancelButton);
                result = alert.showAndWait();
                if (result.get() == okButton) {
                    RegisteredCourses registeredCourses = tableViewUpdateCourse.getSelectionModel().getSelectedItem();
                    String rcName = registeredCourses.getCoursename();
                    int preCourseSectionNumber = registeredCourses.getSectionnumber();
                    int[] sections = mysqlFacade.getArrayofSections(rcName);
                    List<Integer> choices = new ArrayList<>();
                    for (int i = 0; i < sections.length; i++) {
                        choices.add(sections[i]);
                    }
                    ChoiceDialog<Integer> dialog = new ChoiceDialog<>(101, choices);
                    dialog.setTitle("Choice Dialog");
                    dialog.setHeaderText(null);
                    dialog.setContentText("Choose your new Sections :");
                    Optional<Integer> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        int choosedSections = result.get();
                        returnMap = mysqlFacade.updateSection(rcName, choosedSections, preCourseSectionNumber);
                        if ((boolean) returnMap.get("conflict")) {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setContentText("Conflict With Other Course Exist");
                            alert.setHeaderText(null);
                            alert.showAndWait();
                            tableViewUpdateCourse.setItems(mysqlFacade.getRegisteredCourses(""));
                        } else if ((boolean) returnMap.get("updated")) {
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Success Message");
                            alert.setContentText("Updated Success");
                            alert.setHeaderText(null);
                            alert.showAndWait();
                            tableViewUpdateCourse.setItems(mysqlFacade.getRegisteredCourses(""));
                        }
                    } else {
                        System.out.println("nothing");
                    }
                }
            }
        }
    }

    @FXML
    private void handleComboBoxSemesters(ActionEvent event) throws SQLException, ClassNotFoundException {
        int semesterNumber = 0;
        if (comboBoxSemesters.getValue().equalsIgnoreCase("First Semester of 2017/2018")) {
            semesterNumber = 1;
        } else if (comboBoxSemesters.getValue().equalsIgnoreCase("Second Semester of 2017/2018")) {
            semesterNumber = 2;
        } else if (comboBoxSemesters.getValue().equalsIgnoreCase("First Semester of 2018/2019")) {
            semesterNumber = 3;
        } else {
            semesterNumber = 4;
        }

        tableColumnGradesCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        tableColumnGradesCourseGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));

        tableViewGrades.setItems(mysqlFacade.grades(semesterNumber));
        vBoxTable.setVisible(true);
        tableViewGrades.setVisible(true);
    }

    @FXML
    private void handleButtonGoBack(ActionEvent event) {
        vBoxTable.setVisible(false);
        tableViewGrades.setVisible(false);
    }

    @FXML
    private void handleButtonStudentLogout(ActionEvent event) throws IOException {
        textFiledStudentAddress.setText("");
        textFiledStudentLastName.setText("");
        textFiledStudentName.setText("");
        //labelStudentId.setText("");

        viewFactory.getView("login");
    }

}
