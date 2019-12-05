/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Index.Index;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author hp
 */
public class ViewFactory {

    private static ViewFactory viewFactory;

    private ViewFactory() {

    }
    
    public static ViewFactory getViewFactory(){
        if (viewFactory == null) {
            viewFactory = new ViewFactory();
        }
        return viewFactory;
    }

    public void getView(String view) throws IOException {
        if (view.equalsIgnoreCase("admin")) {
            view = "AdminView";
        } else if (view.equalsIgnoreCase("student")) {
            view = "StudentView";
        } else {
            view = "LoginView";
        }
        Parent root = FXMLLoader.load(getClass().
                getResource("/Views/"+view+".fxml"));
        Scene scene = new Scene(root);
        Stage stage = Index.getStage();
        stage.setScene(scene);
        stage.show();
    }
}
