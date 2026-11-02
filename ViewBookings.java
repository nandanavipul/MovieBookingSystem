
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewBookings extends JFrame {

    public ViewBookings() {
        setTitle("View Bookings");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(35, 20, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel heading = new JLabel("BOOKING HISTORY", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 26));
        heading.setForeground(Color.WHITE);
        panel.add(heading, BorderLayout.NORTH);

        String[] columns = {
            "Booking ID", "Customer", "Movie",
            "Show Date", "Show Time", "Tickets", "Total (Rs.)"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };


		JTable table = new JTable(model);
		
		table.setRowHeight(35);
		table.setFont(new Font("Arial", Font.PLAIN, 14));
		table.setForeground(Color.WHITE);
		table.setBackground(new Color(45, 30, 40));
		table.setGridColor(new Color(90, 60, 75));
		table.setSelectionBackground(new Color(125, 35, 60));
		table.setSelectionForeground(Color.WHITE);
		
		table.getTableHeader().setFont(
		    new Font("Arial", Font.BOLD, 14));
		table.getTableHeader().setBackground(new Color(125, 35, 60));
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setPreferredSize(new Dimension(0, 38));
		table.getTableHeader().setReorderingAllowed(false);
		
		table.setFillsViewportHeight(true);


        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("REFRESH");
        refreshButton.addActionListener(e -> loadBookings(model));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(35, 20, 30));
        bottomPanel.add(refreshButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);

        loadBookings(model);
        setVisible(true);
    }

    private void loadBookings(DefaultTableModel model) {
        model.setRowCount(0);

        String query =
            "SELECT b.booking_id, b.customer_name, m.title, " +
            "s.show_date, s.show_time, b.tickets, b.total_amount " +
            "FROM bookings b " +
            "JOIN shows s ON b.show_id = s.show_id " +
            "JOIN movies m ON s.movie_id = m.movie_id " +
            "ORDER BY b.booking_id DESC";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("booking_id"),
                    rs.getString("customer_name"),
                    rs.getString("title"),
                    rs.getDate("show_date"),
                    rs.getTime("show_time"),
                    rs.getInt("tickets"),
                    rs.getDouble("total_amount")
                });
            }

            rs.close();
            pst.close();
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Unable to load bookings: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ViewBookings();
    }
}
