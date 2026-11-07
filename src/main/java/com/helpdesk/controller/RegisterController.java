
package com.helpdesk.controller;

import com.helpdesk.service.AuthService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label messageLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void register() {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        messageLabel.setStyle("-fx-text-fill: #dc2626;");

        if (name.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {

            messageLabel.setText("Please fill in all fields.");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            messageLabel.setText("Please enter a valid email address.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }

        try {

            boolean registered = authService.register(
                    name,
                    email,
                    password
            );

            if (registered) {

                messageLabel.setStyle("-fx-text-fill: #16a34a;");
                messageLabel.setText("Account created successfully!");

                // Return to login screen
                javafx.application.Platform.runLater(() -> {
                    try {
                        Thread.sleep(800);
                        javafx.application.Platform.runLater(this::backToLogin);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            } else {
                messageLabel.setText("Email already registered.");
            }

        } catch (IllegalArgumentException e) {

            messageLabel.setText(e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();
            messageLabel.setText("Registration failed. Check MongoDB connection.");
        }
    }

    @FXML
    private void backToLogin() {

        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource("/fxml/login.fxml")
            );

            Stage stage = (Stage) messageLabel.getScene().getWindow();

            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("Smart HelpDesk - Login");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
