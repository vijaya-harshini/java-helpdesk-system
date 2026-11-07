
package com.helpdesk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/fxml/login.fxml")
        );

        Parent root = loader.load();

        Scene scene = new Scene(root, 900, 600);

        stage.setTitle("Smart HelpDesk - Login");
        stage.setScene(scene);
        stage.setMinWidth(700);
        stage.setMinHeight(500);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
