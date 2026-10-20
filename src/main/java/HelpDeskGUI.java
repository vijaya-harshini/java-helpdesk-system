
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HelpDeskGUI extends JFrame {

    private final JTextField problemField = new JTextField(25);
    private final JTextField searchField = new JTextField(10);

    private final DefaultTableModel tableModel =
            new DefaultTableModel(
                    new String[]{"Ticket ID", "Problem", "Status"}, 0) {

                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    private final JTable ticketTable = new JTable(tableModel);
    private final TicketRepository repository = new TicketRepository();

    public HelpDeskGUI() {
        setTitle("HelpDesk Ticket Management");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        buildUI();
        refreshTable();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                repository.close();
                dispose();
            }
        });
    }

    private void buildUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel("HelpDesk Ticket Management");
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout(5, 10));
        topPanel.add(heading, BorderLayout.NORTH);

        JPanel createPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        createPanel.add(new JLabel("Problem:"));
        createPanel.add(problemField);

        JButton createButton = new JButton("Create Ticket");
        createPanel.add(createButton);
        topPanel.add(createPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        ticketTable.setRowHeight(28);
        ticketTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        mainPanel.add(
                new JScrollPane(ticketTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER));

        bottomPanel.add(new JLabel("Ticket ID:"));
        bottomPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        JButton resolveButton = new JButton("Resolve Ticket");
        JButton refreshButton = new JButton("Refresh");

        bottomPanel.add(searchButton);
        bottomPanel.add(resolveButton);
        bottomPanel.add(refreshButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);

        createButton.addActionListener(e -> createTicket());
        searchButton.addActionListener(e -> searchTicket());
        resolveButton.addActionListener(e -> resolveTicket());
        refreshButton.addActionListener(e -> refreshTable());
    }

    private void createTicket() {
        String problem = problemField.getText().trim();

        if (problem.isEmpty() || problem.contains("|")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter a problem without using the | symbol.");
            return;
        }

        try {
            int id = repository.createTicket(problem);

            problemField.setText("");
            refreshTable();

            JOptionPane.showMessageDialog(
                    this, "Ticket created!\nTicket ID: HD-" + id);

        } catch (Exception e) {
            showDatabaseError(e);
        }
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);

            List<Ticket> tickets = repository.getAllTickets();

            for (Ticket ticket : tickets) {
                tableModel.addRow(new Object[]{
                        "HD-" + ticket.id,
                        ticket.problem,
                        ticket.status
                });
            }
        } catch (Exception e) {
            showDatabaseError(e);
        }
    }

    private void searchTicket() {
        String input = searchField.getText().trim();

        if (input.startsWith("HD-") ||
                input.startsWith("hd-")) {
            input = input.substring(3);
        }

        try {
            int id = Integer.parseInt(input);
            Ticket ticket = repository.findTicket(id);

            if (ticket == null) {
                JOptionPane.showMessageDialog(
                        this, "Ticket not found.");
                return;
            }

            refreshTable();

            for (int row = 0; row < tableModel.getRowCount(); row++) {
                if (tableModel.getValueAt(row, 0)
                        .equals("HD-" + id)) {

                    ticketTable.setRowSelectionInterval(row, row);
                    ticketTable.scrollRectToVisible(
                            ticketTable.getCellRect(row, 0, true));
                    break;
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter a valid ID, such as 101 or HD-101.");
        } catch (Exception e) {
            showDatabaseError(e);
        }
    }

    private void resolveTicket() {
        int selectedRow = ticketTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this, "Select a ticket from the table first.");
            return;
        }

        String ticketId = tableModel
                .getValueAt(selectedRow, 0).toString();

        int id = Integer.parseInt(ticketId.substring(3));

        try {
            Ticket ticket = repository.findTicket(id);

            if (ticket == null) {
                JOptionPane.showMessageDialog(
                        this, "Ticket not found.");
                refreshTable();
                return;
            }

            if (ticket.status.equals("RESOLVED")) {
                JOptionPane.showMessageDialog(
                        this, "This ticket is already resolved.");
                return;
            }

            if (repository.resolveTicket(id)) {
                refreshTable();

                JOptionPane.showMessageDialog(
                        this, "Ticket " + ticketId + " resolved!");
            }

        } catch (Exception e) {
            showDatabaseError(e);
        }
    }

    private void showDatabaseError(Exception e) {
        JOptionPane.showMessageDialog(
                this,
                "Database operation failed:\n" + e.getMessage(),
                "MongoDB Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HelpDeskGUI app = new HelpDeskGUI();
            app.setVisible(true);
        });
    }
}
