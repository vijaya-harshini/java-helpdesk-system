
package com.helpdesk.controller;

import com.helpdesk.model.Ticket;
import com.helpdesk.repository.TicketRepository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label totalTicketsLabel;

    @FXML
    private Label openTicketsLabel;

    @FXML
    private Label inProgressLabel;

    @FXML
    private Label resolvedTicketsLabel;

    @FXML
    private TableView<Ticket> recentTicketsTable;

    @FXML
    private TableColumn<Ticket, Integer> ticketIdColumn;

    @FXML
    private TableColumn<Ticket, String> problemColumn;

    @FXML
    private TableColumn<Ticket, String> statusColumn;

    @FXML
    private ComboBox<String> statusComboBox;

    private final TicketRepository ticketRepository =
            new TicketRepository();

    // Initialize dashboard
    @FXML
    public void initialize() {

        configureTable();

        statusComboBox.setItems(
                FXCollections.observableArrayList(
                        "OPEN",
                        "IN_PROGRESS",
                        "RESOLVED"
                )
        );

        loadStatistics();
        loadTickets();
    }

    // Display admin name
    public void setAdminName(String name) {

        welcomeLabel.setText(
                "Welcome, " + name + "!"
        );
    }

    // Configure table columns
    private void configureTable() {

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

    // Load dashboard statistics
    private void loadStatistics() {

        try {

            long total =
                    ticketRepository.getTotalTicketCount();

            long open =
                    ticketRepository.getTicketCountByStatus("OPEN");

            long inProgress =
                    ticketRepository.getTicketCountByStatus("IN_PROGRESS");

            long resolved =
                    ticketRepository.getTicketCountByStatus("RESOLVED");

            totalTicketsLabel.setText(String.valueOf(total));
            openTicketsLabel.setText(String.valueOf(open));
            inProgressLabel.setText(String.valueOf(inProgress));
            resolvedTicketsLabel.setText(String.valueOf(resolved));

        } catch (Exception e) {

            e.printStackTrace();

            showAlert(
                    "Database Error",
                    "Unable to load dashboard statistics."
            );
        }
    }

    // Load tickets from MongoDB
    private void loadTickets() {

        try {

            ObservableList<Ticket> tickets =
                    FXCollections.observableArrayList(
                            ticketRepository.getAllTickets()
                    );

            recentTicketsTable.setItems(tickets);

        } catch (Exception e) {

            e.printStackTrace();

            showAlert(
                    "Database Error",
                    "Unable to load tickets."
            );
        }
    }

    // Update selected ticket status
    @FXML
    private void updateTicketStatus() {

        Ticket selectedTicket =
                recentTicketsTable.getSelectionModel()
                        .getSelectedItem();

        String newStatus = statusComboBox.getValue();

        if (selectedTicket == null) {

            showAlert(
                    "No Ticket Selected",
                    "Please select a ticket from the table."
            );

            return;
        }

        if (newStatus == null) {

            showAlert(
                    "No Status Selected",
                    "Please choose a status."
            );

            return;
        }

        try {

            boolean updated =
                    ticketRepository.updateTicketStatus(
                            selectedTicket.getId(),
                            newStatus
                    );

            if (updated) {

                showAlert(
                        "Success",
                        "Ticket #" + selectedTicket.getId()
                                + " status updated to "
                                + newStatus + "."
                );

                loadTickets();
                loadStatistics();

                statusComboBox.getSelectionModel().clearSelection();

            } else {

                showAlert(
                        "Update Failed",
                        "Ticket could not be found."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            showAlert(
                    "Database Error",
                    "Unable to update ticket status."
            );
        }
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
                    "Unable to return to login page."
            );
        }
    }

    // Display alert
    private void showAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
