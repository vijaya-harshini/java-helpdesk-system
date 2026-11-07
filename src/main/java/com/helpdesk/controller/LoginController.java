
package com.helpdesk.controller;

import com.helpdesk.model.User;
import com.helpdesk.service.AuthService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button userButton;

    @FXML
    private Button adminButton;

    @FXML
    private Label roleLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private String selectedRole = "USER";

    private final AuthService authService = new AuthService();

    // Select USER
    @FXML
    private void selectUser() {

        selectedRole = "USER";

        userButton.setStyle(
                "-fx-background-color: #2563eb; -fx-text-fill: white;"
        );

        adminButton.setStyle(
                "-fx-background-color: #e5e7eb; -fx-text-fill: #172554;"
        );

        roleLabel.setText("USER LOGIN");
        messageLabel.setText("");
    }

    // Select ADMIN
    @FXML
    private void selectAdmin() {

        selectedRole = "ADMIN";

        adminButton.setStyle(
                "-fx-background-color: #2563eb; -fx-text-fill: white;"
        );

        userButton.setStyle(
                "-fx-background-color: #e5e7eb; -fx-text-fill: #172554;"
        );

        roleLabel.setText("ADMIN LOGIN");
        messageLabel.setText("");
    }

    // Login
    @FXML
    private void login() {

        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {

            messageLabel.setText(
                    "Please enter email and password."
            );

            return;
        }

        User user = authService.login(
                email,
                password,
                selectedRole
        );

        if (user == null) {

            messageLabel.setText(
                    "Invalid email, password, or account role."
            );

            return;
        }

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {

            openAdminDashboard(user);

        } else {

            openUserDashboard(user);
        }
    }

    // Open Admin Dashboard
    private void openAdminDashboard(User user) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/fxml/admin-dashboard.fxml"
                    )
            );

            Parent root = loader.load();

            AdminDashboardController controller =
                    loader.getController();

            controller.setAdminName(user.getName());

            Stage stage = (Stage) emailField
                    .getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setTitle(
                    "Smart HelpDesk - Admin Dashboard"
            );

            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {

            e.printStackTrace();

            showAlert(
                    "Dashboard Error",
                    "Unable to open the Admin Dashboard."
            );
        }
    }

    // Open User Dashboard
    private void openUserDashboard(User user) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/fxml/user-dashboard.fxml"
                    )
            );

            Parent root = loader.load();

            UserDashboardController controller =
                    loader.getController();

            controller.setUser(user);

            Stage stage = (Stage) emailField
                    .getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setTitle(
                    "Smart HelpDesk - User Dashboard"
            );

            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {

            e.printStackTrace();

            showAlert(
                    "Dashboard Error",
                    "Unable to open the User Dashboard."
            );
        }
    }

    // Open Registration Page
    @FXML
    private void openRegister() {

        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource(
                            "/fxml/register.fxml"
                    )
            );

            Stage stage = (Stage) emailField
                    .getScene().getWindow();

            stage.setScene(new Scene(root, 900, 600));

            stage.setTitle(
                    "Smart HelpDesk - Registration"
            );

        } catch (IOException e) {

            e.printStackTrace();

            showAlert(
                    "Error",
                    "Unable to open registration page."
            );
        }
    }

    // Alert
    private void showAlert(String title, String message) {

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION
        );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
