
package com.helpdesk.controller;

import com.helpdesk.model.Ticket;
import com.helpdesk.model.User;
import com.helpdesk.repository.TicketRepository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class UserDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextArea problemField;

    @FXML
    private TableView<Ticket> myTicketsTable;

    @FXML
    private TableColumn<Ticket, Integer> ticketIdColumn;

    @FXML
    private TableColumn<Ticket, String> problemColumn;

    @FXML
    private TableColumn<Ticket, String> statusColumn;

    private final TicketRepository ticketRepository =
            new TicketRepository();

    private User loggedInUser;

    // Initialize dashboard
    @FXML
    public void initialize() {

        ticketIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        problemColumn.setCellValueFactory(
                new PropertyValueFactory<>("problem")
        );

        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("status")
        );
    }

    // Receive logged-in user
    public void setUser(User user) {

        this.loggedInUser = user;

        welcomeLabel.setText(
                "Welcome, " + user.getName() + "!"
        );

        loadMyTickets();
    }

    // Create a new ticket
    @FXML
    private void createTicket() {

        if (loggedInUser == null) {
            showAlert(
                    "Error",
                    "User information is unavailable.",
                    Alert.AlertType.ERROR
            );
            return;
        }

        String problem = problemField.getText().trim();

        if (problem.isEmpty()) {
            showAlert(
                    "Validation Error",
                    "Please describe your problem.",
                    Alert.AlertType.WARNING
            );
            return;
        }

        try {

            int ticketId = ticketRepository.createTicket(
                    problem,
                    loggedInUser.getEmail()
            );

            problemField.clear();

            loadMyTickets();

            showAlert(
                    "Ticket Created",
                    "Your ticket #" + ticketId
                            + " has been submitted successfully.",
                    Alert.AlertType.INFORMATION
            );

        } catch (Exception e) {

            e.printStackTrace();

            showAlert(
                    "Database Error",
                    "Unable to create your ticket.",
                    Alert.AlertType.ERROR
            );
        }
    }

    // Load tickets belonging to logged-in user
    private void loadMyTickets() {

        if (loggedInUser == null) {
            return;
        }

        try {

            ObservableList<Ticket> tickets =
                    FXCollections.observableArrayList(
                            ticketRepository.getTicketsByUserEmail(
                                    loggedInUser.getEmail()
                            )
                    );

            myTicketsTable.setItems(tickets);

        } catch (Exception e) {

            e.printStackTrace();

            showAlert(
                    "Database Error",
                    "Unable to load your tickets.",
                    Alert.AlertType.ERROR
            );
        }
    }

    // Refresh ticket list
    @FXML
    private void refreshTickets() {

        loadMyTickets();
    }

    // Logout
    @FXML
    private void logout() {

        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource("/fxml/login.fxml")
            );

            Stage stage = (Stage) welcomeLabel
                    .getScene().getWindow();

            stage.setScene(new Scene(root, 900, 600));

            stage.setTitle(
                    "Smart HelpDesk - Login"
            );

            stage.setMaximized(false);

            ticketRepository.close();

        } catch (IOException e) {

            e.printStackTrace();

            showAlert(
                    "Error",
                    "Unable to return to login page.",
                    Alert.AlertType.ERROR
            );
        }
    }

    // Display alert
    private void showAlert(
            String title,
            String message,
            Alert.AlertType type
    ) {

        Alert alert = new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
