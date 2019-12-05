/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Models.DBConnection;
import Index.Index;
import Models.DatabaseFacade;
import Views.ViewFactory;

/**
 * FXML Controller class
 *
 * @author WH1108
 */
public class LoginController implements Initializable {

    @FXML
    private Label labelName;
    @FXML
    private Label labelPassword;
    @FXML
    private TextField textFiledName;
    @FXML
    private TextField textFiledPassword;
    @FXML
    private Button buttonSingin;
    @FXML
    private Button buttonExit;
    DatabaseFacade databaseFacade ;
    ViewFactory viewFactory;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseFacade = DatabaseFacade.getDatabaseFacade();
        viewFactory = ViewFactory.getViewFactory();
    }

    @FXML
    private void handelbuttonSingin(ActionEvent event) throws IOException {
        DBConnection connection = DBConnection.getDbConnection();
        try {
            Map returnMap = connection.verifyLogin(textFiledName.getText(),
                    textFiledPassword.getText());
            if ((boolean) returnMap.get("isNotEmpty")) {
                String loginType = (String) returnMap.get("loginType");
                if (loginType.equalsIgnoreCase("admin")) {
                    viewFactory.getView("admin");
                } else {
                    if (!(boolean) returnMap.get("failedLogin")) {
                        viewFactory.getView("student");
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setTitle("Error occurred");
                        alert.setContentText("Wrong Name or Password!!");
                        alert.show();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error occurred");
                alert.setContentText("Some Fileds EMPTY!!");
                alert.show();
            }
        } catch (Exception ex) {
            System.out.println("Exception occurred");
        }
    }

    private void handleButtonSignUp(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().
                getResource("/Views/SignUp.fxml"));
        Scene scene = new Scene(root);
        Stage stage = Index.getStage();
        stage.setScene(scene);
        stage.show();
        databaseFacade.closeConnection();
    }

    @FXML
    private void handelbuttonExit(ActionEvent event) {
        databaseFacade.closeConnection();
        System.exit(0);
    }

}
