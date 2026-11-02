
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BookingForm extends JFrame {

    public BookingForm(String movieTitle, double price) {

        setTitle("CineBook - Book Tickets");
        setSize(450, 430);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(30, 25, 30));

        JLabel heading = new JLabel("BOOK YOUR TICKETS");
        heading.setBounds(90, 20, 300, 35);
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setForeground(Color.WHITE);
        panel.add(heading);

        JLabel movieLabel = new JLabel("Movie: " + movieTitle);
        movieLabel.setBounds(40, 75, 350, 25);
        movieLabel.setForeground(Color.WHITE);
        movieLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(movieLabel);

        JLabel nameLabel = new JLabel("Customer Name:");
        nameLabel.setBounds(40, 125, 130, 25);
        nameLabel.setForeground(Color.WHITE);
        panel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(175, 125, 200, 28);
        panel.add(nameField);


		JLabel showLabel = new JLabel("Showtime:");
		showLabel.setBounds(40, 165, 130, 25);
		showLabel.setForeground(Color.WHITE);
		panel.add(showLabel);
		
		JComboBox<String> showCombo = new JComboBox<>();
		showCombo.setBounds(175, 165, 200, 28);
		panel.add(showCombo);

		try {
		    Connection con = DBConnection.getConnection();
		
		    String query =
		        "SELECT show_id, show_date, show_time FROM shows " +
		        "WHERE movie_id = (SELECT movie_id FROM movies " +
		        "WHERE title = ?) AND seats_available > 0";
		
		    PreparedStatement pst = con.prepareStatement(query);
		    pst.setString(1, movieTitle);
		
		    ResultSet rs = pst.executeQuery();
		
		    while (rs.next()) {
		        int showId = rs.getInt("show_id");
		        String date = rs.getString("show_date");
		        String time = rs.getString("show_time");
		
		        showCombo.addItem(
		            showId + " | " + date + " | " + time
		        );
		    }
		
		    rs.close();
		    pst.close();
		    con.close();
		
		} catch (Exception e) {
		    JOptionPane.showMessageDialog(
		        this,
		        "Unable to load showtimes: " + e.getMessage()
		    );
		}

		
		JLabel ticketsLabel = new JLabel("Number of Tickets:");
		ticketsLabel.setBounds(40, 215, 130, 25);
		ticketsLabel.setForeground(Color.WHITE);
		panel.add(ticketsLabel);
		
		JSpinner ticketSpinner = new JSpinner(
		        new SpinnerNumberModel(1, 1, 10, 1));
		ticketSpinner.setBounds(175, 215, 80, 28);
		panel.add(ticketSpinner);



        JLabel totalLabel = new JLabel("Total: Rs. " + price);
        totalLabel.setBounds(40, 265, 300, 30);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(new Color(240, 170, 185));
        panel.add(totalLabel);

        JButton confirmButton = new JButton("CONFIRM BOOKING");
        confirmButton.setBounds(100, 320, 230, 40);
        confirmButton.setBackground(new Color(125, 35, 60));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        panel.add(confirmButton);

        ticketSpinner.addChangeListener(e -> {
            int tickets = (int) ticketSpinner.getValue();
            totalLabel.setText("Total: Rs. " + (price * tickets));
        });


		confirmButton.addActionListener(e -> {
		
		    String customerName = nameField.getText().trim();
		    int tickets = (int) ticketSpinner.getValue();
		
		    if (customerName.isEmpty()) {
		        JOptionPane.showMessageDialog(
		            this, "Please enter your name.");
		        return;
		    }
		
		    if (showCombo.getSelectedItem() == null) {
		        JOptionPane.showMessageDialog(
		            this, "Please select a showtime.");
		        return;
		    }
		
		    String selectedShow = showCombo.getSelectedItem().toString();
		    int showId = Integer.parseInt(
		        selectedShow.split(" \\| ")[0]);
		
		    double totalAmount = price * tickets;
		
		    try {
		        Connection con = DBConnection.getConnection();
		
		        con.setAutoCommit(false);
		
		        String seatQuery =
		            "UPDATE shows SET seats_available = seats_available - ? " +
		            "WHERE show_id = ? AND seats_available >= ?";
		
		        PreparedStatement seatStmt =
		            con.prepareStatement(seatQuery);
		
		        seatStmt.setInt(1, tickets);
		        seatStmt.setInt(2, showId);
		        seatStmt.setInt(3, tickets);
		
		        int updated = seatStmt.executeUpdate();
		        seatStmt.close();
		
		        if (updated == 0) {
		            con.rollback();
		            JOptionPane.showMessageDialog(
		                this, "Not enough seats available.");
		            con.close();
		            return;
		        }
		
		        String bookingQuery =
		            "INSERT INTO bookings " +
		            "(customer_name, show_id, tickets, total_amount) " +
		            "VALUES (?, ?, ?, ?)";
		
		        PreparedStatement bookingStmt =
		            con.prepareStatement(bookingQuery);
		
		        bookingStmt.setString(1, customerName);
		        bookingStmt.setInt(2, showId);
		        bookingStmt.setInt(3, tickets);
		        bookingStmt.setDouble(4, totalAmount);
		
		        bookingStmt.executeUpdate();
		        bookingStmt.close();
		
		        con.commit();
		        con.close();
		
		        JOptionPane.showMessageDialog(
		            this,
		            "Booking confirmed!\n" +
		            "Movie: " + movieTitle + "\n" +
		            "Tickets: " + tickets + "\n" +
		            "Total: Rs. " + totalAmount
		        );
		
		        dispose();
		
		    } catch (SQLException ex) {
		        JOptionPane.showMessageDialog(
		            this,
		            "Booking failed: " + ex.getMessage()
		        );
		    }
		});


        add(panel);
        setVisible(true);
    }
}
