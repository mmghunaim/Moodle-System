///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package Controllers;
//
//import java.io.IOException;
//import java.net.URL;
//import java.sql.SQLException;
//import java.util.Map;
//import java.util.ResourceBundle;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextField;
//import javafx.stage.Stage;
//import Models.DBConnection;
//import Index.MainView;
//
///**
// * FXML Controller class
// *
// * @author WH1108
// */
//public class SignUpController implements Initializable {
//    Alert alert;
//    @FXML
//    private TextField textFiledStudentId;
//    @FXML
//    private TextField textFiledStudentName;
//    @FXML
//    private TextField textFiledStudentPassword;
//    @FXML
//    private TextField textFiledStudentPhone;
//    @FXML
//    private TextField textFiledStudentAddress;
//    @FXML
//    private Button buttonSignUp;
//    @FXML
//    private Button buttonSignIn;
//
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        
//    }    
//
//    @FXML
//    private void handleSingUp(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
//        DBConnection connection = DBConnection.getConnection();
//        Map returnMap = connection.addStudent(textFiledStudentId.getText(), textFiledStudentName.getText(), 
//                textFiledStudentPassword.getText(), textFiledStudentPhone.getText(), textFiledStudentAddress.getText());
//        if((boolean)returnMap.get("isAdded")){
//            textFiledStudentId.setText("");
//            textFiledStudentName.setText("");
//            textFiledStudentPassword.setText("");
//            textFiledStudentPhone.setText("");
//            textFiledStudentAddress.setText("");
//            alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("Success Message");
//                alert.setContentText("INSERTED SUCCESS");
//                alert.setHeaderText(null);
//                alert.showAndWait();
//            Parent root = FXMLLoader.load(getClass().
//                getResource("LoginView.fxml"));
//                Scene scene = new Scene(root);
//                Stage stage = MainView.getStage();
//                stage.setScene(scene);
//                stage.show();
//        }else if((boolean)returnMap.get("isExist")){
//            alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("ERROR Message");
//                alert.setContentText("Student is Already Exist!!");
//                alert.setHeaderText(null);
//                alert.showAndWait();
//                textFiledStudentId.setText("");
//        }else if((boolean)returnMap.get("enterError")){
//            alert = new Alert(Alert.AlertType.ERROR);
//                alert.setTitle("ERROR Message");
//                alert.setContentText("Some Data Are Missing");
//                alert.setHeaderText(null);
//                alert.showAndWait();
//                textFiledStudentId.setText("");
//        }
//    }
//
//    @FXML
//    private void handleSingIn(ActionEvent event) throws IOException {
//        Parent root = FXMLLoader.load(getClass().
//                getResource("LoginView.fxml"));
//                Scene scene = new Scene(root);
//                Stage stage = MainView.getStage();
//                stage.setScene(scene);
//                stage.show();
//    }
//    
//}
