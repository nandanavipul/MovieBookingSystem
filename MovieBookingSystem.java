
import javax.swing.*;
import java.awt.*;

public class MovieBookingSystem extends JFrame {

    public MovieBookingSystem() {

        setTitle("CineBook - Movie Booking System");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top heading
        JPanel header = new JPanel();
        header.setBackground(new Color(45, 15, 25));
        header.setLayout(new GridLayout(2, 1));

        JLabel title = new JLabel("CINEBOOK", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
            "Your movie night starts here", JLabel.CENTER
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(new Color(230, 190, 200));

        header.add(title);
        header.add(subtitle);
        add(header, BorderLayout.NORTH);

        // Main content
        JPanel content = new JPanel(new GridLayout(3, 1, 15, 15));
        content.setBackground(new Color(30, 25, 30));
        content.setBorder(BorderFactory.createEmptyBorder(45, 120, 45, 120));

        JButton moviesButton = new JButton("VIEW MOVIES");
        JButton bookingButton = new JButton("BOOK TICKETS");
        JButton bookingsButton = new JButton("VIEW BOOKINGS");
        
        bookingButton.addActionListener(e -> {
            new MovieCatalogue();
        });
        bookingsButton.addActionListener(e -> {
            new ViewBookings();
        });

        moviesButton.addActionListener(e -> {
            new MovieCatalogue();
        });


        JButton[] buttons = {
            moviesButton, bookingButton, bookingsButton
        };

        for (JButton button : buttons) {
            button.setFont(new Font("Arial", Font.BOLD, 16));
            button.setBackground(new Color(125, 35, 60));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            content.add(button);
        }

        add(content, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel(
            "CineBook | Simple. Fast. Cinematic.",
            JLabel.CENTER
        );
        footer.setForeground(Color.WHITE);
        footer.setBackground(new Color(45, 15, 25));
        footer.setOpaque(true);
        footer.setBorder(BorderFactory.createEmptyBorder(12, 5, 12, 5));
        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        new MovieBookingSystem();
    }
}
