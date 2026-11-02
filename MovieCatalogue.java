import java.sql.PreparedStatement;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MovieCatalogue extends JFrame {

	private JPanel moviesPanel;
	private JComboBox<String> genreBox;
    public MovieCatalogue() {

        setTitle("CineBook - Available Movies");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(30, 25, 30));
        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel heading = new JLabel("EXPLORE MOVIES");
        heading.setFont(new Font("Arial", Font.BOLD, 26));
        heading.setForeground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(new Color(30, 25, 30));

        String[] genres = {
        	    "All", "Sci-Fi", "Thriller", "Romance",
        	    "Action", "Comedy", "Horror"
        };

        genreBox = new JComboBox<>(genres);

        JLabel genreLabel = new JLabel("Select Genre:");
        genreLabel.setForeground(Color.WHITE);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(new Color(30, 25, 30));
        filterPanel.add(genreLabel);
        filterPanel.add(genreBox);

        topPanel.add(heading, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        moviesPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        moviesPanel.setBackground(new Color(30, 25, 30));

        filterMovies();

        genreBox.addActionListener(e -> filterMovies());

        JScrollPane scrollPane = new JScrollPane(moviesPanel);

        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(30, 25, 30));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        setVisible(true);
    }

    private void filterMovies() {

        moviesPanel.removeAll();

        String selectedGenre =
            genreBox.getSelectedItem().toString();

        try {
            Connection con = DBConnection.getConnection();

            String query;

            if (selectedGenre.equals("All")) {
                query = "SELECT * FROM movies";
            } else {
                query = "SELECT * FROM movies WHERE genre = ?";
            }

            PreparedStatement pst = con.prepareStatement(query);

            if (!selectedGenre.equals("All")) {
                pst.setString(1, selectedGenre);
            }

            ResultSet rs = pst.executeQuery();

            int count = 0;

            while (rs.next()) {

                String title = rs.getString("title");
                String genre = rs.getString("genre");
                String language = rs.getString("language");
                int duration = rs.getInt("duration_minutes");
                double price = rs.getDouble("price");

                String imageName = "";

                if (title.equals("Interstellar")) {
                    imageName = "interstellar.jpeg";
                } else if (title.equals("Inception")) {
                    imageName = "inception.jpeg";
                } else if (title.equals("Premam")) {
                    imageName = "premam.jpeg";
                } else if (title.equals("The Martian")) {
                    imageName = "the_martian.png";
                } else if (title.equals("Dune")) {
                    imageName = "dune.png";
                } else if (title.equals("Shutter Island")) {
                    imageName = "shutter_island.png";
                } else if (title.equals("Gone Girl")) {
                    imageName = "gone_girl.png";
                } else if (title.equals("La La Land")) {
                    imageName = "la_la_land.png";
                } else if (title.equals("Me Before You")) {
                    imageName = "me_before_you.png";
                } else if (title.equals("John Wick")) {
                    imageName = "john_wick.png";
                } else if (title.equals("Top Gun: Maverick")) {
                    imageName = "top_gun_maverick.png";
                } else if (title.equals("3 Idiots")) {
                    imageName = "3_idiots.png";
                } else if (title.equals("The Intern")) {
                    imageName = "the_intern.png";
                } else if (title.equals("The Conjuring")) {
                    imageName = "the_conjuring.png";
                } else if (title.equals("A Quiet Place")) {
                    imageName = "a_quiet_place.png";
                }

                addMovieCard(
                    moviesPanel,
                    title,
                    genre + " | " + language,
                    duration + " min",
                    "Rs. " + price,
                    imageName
                );

                count++;
            }

            if (count == 0) {
                JLabel message = new JLabel("No movies found!");
                message.setForeground(Color.WHITE);
                moviesPanel.add(message);
            }

            rs.close();
            pst.close();
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Unable to load movies: " + e.getMessage()
            );
        }

        moviesPanel.revalidate();
        moviesPanel.repaint();
    }


    private void addMovieCard(JPanel panel, String title,
                              String genre, String duration,
                              String price, String imageName) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(50, 40, 48));
        card.setBorder(
            BorderFactory.createEmptyBorder(12, 10, 12, 10)
        );

        JLabel poster = new JLabel();
        poster.setAlignmentX(Component.CENTER_ALIGNMENT);

        File imageFile = new File("images/" + imageName);

        if (imageFile.exists()) {
            ImageIcon icon = new ImageIcon(imageFile.getPath());
            Image image = icon.getImage().getScaledInstance(
                180, 250, Image.SCALE_SMOOTH
            );
            poster.setIcon(new ImageIcon(image));
        } else {
            poster.setText("Poster not found");
            poster.setForeground(Color.WHITE);
        }

        JLabel name = new JLabel(title);
        name.setFont(new Font("Arial", Font.BOLD, 18));
        name.setForeground(Color.WHITE);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel details = new JLabel(genre + " | " + duration);
        details.setForeground(Color.LIGHT_GRAY);
        details.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel ticketPrice = new JLabel(price);
        ticketPrice.setFont(new Font("Arial", Font.BOLD, 16));
        ticketPrice.setForeground(new Color(240, 170, 185));
        ticketPrice.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton bookButton = new JButton("BOOK NOW");
        bookButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookButton.setBackground(new Color(125, 35, 60));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFocusPainted(false);
        bookButton.addActionListener(e -> {
            new BookingForm(title, Double.parseDouble(
                price.replace("Rs. ", "")));
        });

        card.add(poster);
        card.add(Box.createVerticalStrut(10));
        card.add(name);
        card.add(Box.createVerticalStrut(5));
        card.add(details);
        card.add(Box.createVerticalStrut(8));
        card.add(ticketPrice);
        card.add(Box.createVerticalStrut(10));
        card.add(bookButton);

        panel.add(card);
    }

    public static void main(String[] args) {
        new MovieCatalogue();
    }
}
