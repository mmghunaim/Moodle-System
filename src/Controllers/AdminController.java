package Controllers;

import Models.RegisteredCourses;
import Models.Student;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Models.MySQLConnection;
import Index.Index;
import Models.MySQLFacade;
import Models.MongoFacade;
import Models.MongoAdmin;
import Views.ViewFactory;
import java.util.ArrayList;

public class AdminController implements Initializable {

    @FXML
    private TextField textFiledStudentId;
    @FXML
    private TextField textFiledStudentName;
    @FXML
    private TextField textFiledStudentPassword;
    @FXML
    private TextField textFiledStudentPhone;
    @FXML
    private TextField textFiledStudentAddress;
    @FXML
    private Button buttonAddNewStudent;
    @FXML
    private Button buttonCancel;
    @FXML
    private TableView<Student> tableViewStudent;
    @FXML
    private TableColumn<Student, String> tableColumnStudentId;
    @FXML
    private TableColumn<Student, String> tableColumnStudentName;
    @FXML
    private TableColumn<Student, String> tableColumnStudentPhone;
    @FXML
    private TableColumn<Student, String> tableColumnStudentAddress;
    @FXML
    private Tab tabPaneStudent;
    @FXML
    private TableView<Student> tableViewDeleteStudent;
    @FXML
    private TableColumn<Student, String> tableColumnDeleteStudentName;
    @FXML
    private TableColumn<Student, String> tableColumnDeleteStudentId;
    @FXML
    private Tab tabPaneDeleteStudent;
    @FXML
    private VBox vBoxTable;
    @FXML
    private Button goBack;
    @FXML
    private TableView<RegisteredCourses> tableViewStudensCourses;
    @FXML
    private TableColumn<RegisteredCourses, String> tableColumnStudensCoursesCourseName;
    @FXML
    private TableColumn<RegisteredCourses, Integer> tableColumnStudensCoursesCourseSection;
    @FXML
    private ComboBox<String> comboBoxStudentsCourses;
    @FXML
    private Button buttonadminlogout;

    Alert alert;
    Map returnMap;
    ObservableList<String> studentList = FXCollections.observableArrayList();
    ArrayList<String> studenIds;
    Optional<ButtonType> result;

    private String connectionType = "mongo";

    MySQLFacade mysqlFacade;
    MongoFacade mongoFacade;

    ViewFactory viewFactory;

    public AdminController() {
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
            if (connectionType.equalsIgnoreCase("mysql")) {
                studenIds = mysqlFacade.getStudentIds();
            } else {
                studenIds = mongoFacade.getStudentIds();
            }

            for (int i = 0; i < studenIds.size(); i++) {
                studentList.add(studenIds.get(i));
            }
            comboBoxStudentsCourses.setItems(studentList);
            comboBoxStudentsCourses.setValue("Choose Student ID ...");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    private void handleTabPaneStudent(Event event) throws ClassNotFoundException, SQLException {
        tableColumnStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        tableColumnStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        tableColumnStudentPhone.setCellValueFactory(new PropertyValueFactory<>("studentPhone"));
        tableColumnStudentAddress.setCellValueFactory(new PropertyValueFactory<>("studentAddress"));

        if (connectionType.equalsIgnoreCase("mysql")) {
            tableViewStudent.setItems(mysqlFacade.getStudent());
        } else {
            tableViewStudent.setItems(mongoFacade.getStudent());
        }
    }

    @FXML
    private void handleTabPaneDeleteStudent(Event event) throws ClassNotFoundException, SQLException {
        tableColumnDeleteStudentName.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        tableColumnDeleteStudentId.setCellValueFactory(new PropertyValueFactory<>("studentName"));

        if (connectionType.equalsIgnoreCase("mysql")) {
            tableViewDeleteStudent.setItems(mysqlFacade.getStudent());
        } else {
            tableViewDeleteStudent.setItems(mongoFacade.getStudent());
        }
    }

    @FXML
    private void handleButtonAddNewStudent(ActionEvent event) throws ClassNotFoundException, SQLException {
        if (connectionType.equalsIgnoreCase("mysql")) {
            returnMap = mysqlFacade.addStudent(textFiledStudentId.getText(), textFiledStudentName.getText(),
                    textFiledStudentPassword.getText(), textFiledStudentPhone.getText(), textFiledStudentAddress.getText());
        } else {
            returnMap = mongoFacade.addStudent(textFiledStudentId.getText(), textFiledStudentName.getText(),
                    textFiledStudentPassword.getText(), textFiledStudentPhone.getText(), textFiledStudentAddress.getText());
        }
        if ((boolean) returnMap.get("isAdded")) {
            textFiledStudentId.setText("");
            textFiledStudentName.setText("");
            textFiledStudentPassword.setText("");
            textFiledStudentPhone.setText("");
            textFiledStudentAddress.setText("");

            studenIds = mongoFacade.getStudentIds();
            studentList = FXCollections.observableArrayList();
            for (int i = 0; i < studenIds.size(); i++) {
                studentList.add(studenIds.get(i));
            }
            comboBoxStudentsCourses.setItems(studentList);
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success Message");
            alert.setContentText("Student Added Successfully!");
            alert.setHeaderText(null);
            alert.showAndWait();
        } else if ((boolean) returnMap.get("isExist")) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR Message");
            alert.setContentText("Student is Already Exist!!");
            alert.setHeaderText(null);
            alert.showAndWait();
            textFiledStudentId.setText("");
        } else if ((boolean) returnMap.get("enterError")) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR Message");
            alert.setContentText("Some Data Are Missing");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    @FXML
    private void handleButtonCancel(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleTableColumn(MouseEvent event) throws SQLException, ClassNotFoundException {
        Student dbstudent = new Student();
        if (event.getClickCount() == 2) {
            if (tableViewDeleteStudent.getSelectionModel().getSelectedItem() != null) {
                alert = new Alert(AlertType.WARNING);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Do You Want Remove This Student??!");
                ButtonType okButton = new ButtonType("Yes", ButtonData.YES);
                ButtonType cancelButton = new ButtonType("No", ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(okButton, cancelButton);
                result = alert.showAndWait();
                if (result.get() == okButton) {
                    try {
                        Student student = tableViewDeleteStudent.getSelectionModel().getSelectedItem();
                        boolean deleted = false;
                        if (connectionType.equalsIgnoreCase("mysql")) {
                            deleted = mysqlFacade.deleteStudent(student.getStudentId());
                        } else {
                            deleted = mongoFacade.deleteStudent(student.getStudentId());
                        }

                        if (deleted) {

                            if (connectionType.equalsIgnoreCase("mysql")) {
                                studenIds = mysqlFacade.getStudentIds();
                            } else {
                                studenIds = mongoFacade.getStudentIds();
                            }

                            studentList = FXCollections.observableArrayList();
                            for (int i = 0; i < studenIds.size(); i++) {
                                studentList.add(studenIds.get(i));
                            }
                            comboBoxStudentsCourses.setItems(studentList);
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Success Message");
                            alert.setContentText("Student Removed Successfully!");
                            alert.setHeaderText(null);
                            alert.showAndWait();
                            tableViewDeleteStudent.setItems(mongoFacade.getStudent());
                        }
                    } catch (Exception ex) {
                        System.out.println("Cannot Delete this student because it has reference to athoer records in precourse table");
                    }
                }
            }
        }
    }

    @FXML
    private void handleTableColumnUpdate(MouseEvent event) throws SQLException, ClassNotFoundException {
        if (event.getClickCount() == 2) {
            if (tableViewStudent.getSelectionModel().getSelectedItem() != null) {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Do You Want Update This Student??!");
                result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Student student = tableViewStudent.getSelectionModel().getSelectedItem();
                    alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Chooseing Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("What do you want to update ??!");
//                    ButtonType id = new ButtonType("ID");
                    ButtonType name = new ButtonType("Name");
                    ButtonType phone = new ButtonType("Phone");
                    ButtonType address = new ButtonType("Address");
                    ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
                    alert.getButtonTypes().setAll(name, phone, address, cancel);

                    result = alert.showAndWait();
                    String enteredText = "";
                    String updateType = "";
//                    if (result.get()==id) {
//                        TextInputDialog dialog = new TextInputDialog();
//                        dialog.setTitle("Text ID Dialog");
//                        dialog.setHeaderText(null);
//                        dialog.setContentText("Please New Student ID:");
//                        Optional<String> text = dialog.showAndWait();
//                        if (text.isPresent()) {
//                            enteredText = text.get();
//                            updateType="id";
//                        }
//                    }
                    if (result.get() == name) {
                        TextInputDialog dialog = new TextInputDialog();
                        dialog.setTitle("Text ID Dialog");
                        dialog.setHeaderText(null);
                        dialog.setContentText("Please New Student Name:");
                        Optional<String> text = dialog.showAndWait();
                        if (text.isPresent()) {
                            enteredText = text.get();
                            updateType = "name";
                        }
                    } else if (result.get() == phone) {
                        TextInputDialog dialog = new TextInputDialog();
                        dialog.setTitle("Text ID Dialog");
                        dialog.setHeaderText(null);
                        dialog.setContentText("Please New Student Phone:");
                        Optional<String> text = dialog.showAndWait();
                        if (text.isPresent()) {
                            enteredText = text.get();
                            updateType = "phone";
                        }
                    } else if (result.get() == address) {
                        TextInputDialog dialog = new TextInputDialog();
                        dialog.setTitle("Text ID Dialog");
                        dialog.setHeaderText(null);
                        dialog.setContentText("Please New Student Address:");
                        Optional<String> text = dialog.showAndWait();
                        if (text.isPresent()) {
                            enteredText = text.get();
                            updateType = "address";
                        }
                    }
                    if (!enteredText.equalsIgnoreCase("")) {

                        if (connectionType.equalsIgnoreCase("mysql")) {
                            returnMap = mongoFacade.updateStudent(student.getStudentId(), enteredText, updateType);
                        } else {
                            returnMap = mysqlFacade.updateStudent(student.getStudentId(), enteredText, updateType);
                        }

                        if (!(boolean) returnMap.get("duplicatedOccurred")) {
                            if ((boolean) returnMap.get("isUpdated")) {
                                if (connectionType.equalsIgnoreCase("mysql")) {
                                    tableViewStudent.setItems(mysqlFacade.getStudent());
                                } else {
                                    tableViewStudent.setItems(mongoFacade.getStudent());
                                }

                                alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Success Message");
                                alert.setContentText("Student Updated Successfully!");
                                alert.setHeaderText(null);
                                alert.show();
                                tableViewStudent.setItems(mongoFacade.getStudent());
                            }
                        } else {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setContentText("Duplicated Student ID !!");
                            alert.setHeaderText(null);
                            alert.show();
                        }
                    }
                }
            }
        }

    }

    @FXML
    private void handleComboBoxStudentsCourses(ActionEvent event) throws SQLException, ClassNotFoundException {
        String selectedStudent = comboBoxStudentsCourses.getValue();

        if (connectionType.equalsIgnoreCase("mysql")) {
            mysqlFacade.getRegisteredCourses(selectedStudent);
        } else {
//            mongoFacade.getRegisteredCourses(selectedStudent);
        }
        tableColumnStudensCoursesCourseName.setCellValueFactory(new PropertyValueFactory<>("coursename"));
        tableColumnStudensCoursesCourseSection.setCellValueFactory(new PropertyValueFactory<>("sectionnumber"));

        if (connectionType.equalsIgnoreCase("mysql")) {
            tableViewStudensCourses.setItems(mysqlFacade.getRegisteredCourses(selectedStudent));
        } else {
            tableViewStudent.setItems(mongoFacade.getStudent());
        }

        vBoxTable.setVisible(true);
        tableViewStudensCourses.setVisible(true);
    }

    @FXML
    private void handleButtonGoBack(ActionEvent event) {
        vBoxTable.setVisible(false);
        tableViewStudensCourses.setVisible(false);
    }

    @FXML
    private void handleButtonAdminLogout(ActionEvent event) throws IOException {

        viewFactory.getView("login");
    }

}
