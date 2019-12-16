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
import Models.MySQLConnection;
import Index.Index;
import Models.MySQLFacade;
import Views.ViewFactory;

/**
 * FXML Controller class
 *
 * @author mmgunaim
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

    MySQLFacade databaseFacade;
    ViewFactory viewFactory;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseFacade = MySQLFacade.getDatabaseFacade();
        viewFactory = ViewFactory.getViewFactory();
    }

    @FXML
    private void handelbuttonSingin(ActionEvent event) throws IOException {
        MySQLConnection connection = MySQLConnection.getDbConnection();
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
            ex.printStackTrace();
        }
    }

    @FXML
    private void handelbuttonExit(ActionEvent event) {
        databaseFacade.closeConnection();
        System.out.println("Here The Connection Closed");
        System.exit(0);
    }

}
