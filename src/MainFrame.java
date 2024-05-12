import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainFrame extends JFrame {
    // definicja komponentów interfejsu użytkownika
    private JComboBox<String> issueComboBox;  // combobox służący do wyboru sprawy
    private JButton generateButton;  // przycisk służący do generowania biletu
    private JLabel ticketInfo;  // etykieta wyświetlająca informacje o bilecie

    public MainFrame() {
        setTitle("System Kolejkowy");  // ustawienie tytułu okna
        setSize(600, 400); // ustalenie rozmiarów okna
        setLocationRelativeTo(null);   // wyśrodkowanie okna na ekranie
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ustawienie domyślnej operacji zamknięcia okna na zakończenie aplikacji
        initComponents();  // wywołanie metody inicjalizującej komponenty gui
        loadIssues();  // załadowanie dostępnych spraw z bazy danych do comboboxa
    }

    private void initComponents() {
        issueComboBox = new JComboBox<>(); // inicjalizacja comboboxa
        generateButton = new JButton("Generuj bilet"); // inicjalizacja przycisku
        ticketInfo = new JLabel("Numer biletu: brak"); // inicjalizacja etykiety z domyślnym tekstem

        setLayout(new FlowLayout()); // ustawienie menedżera układu dla okna
        add(issueComboBox);          // dodanie comboboxa do okna
        add(generateButton);         // dodanie przycisku do okna
        add(ticketInfo);             // dodanie etykiety do okna

        generateButton.addActionListener(e -> generateTicket((String) issueComboBox.getSelectedItem())); // dodanie słuchacza zdarzeń do przycisku, który wywołuje metodę generateticket
        pack(); // dostosowanie rozmiaru okna do zawartości
    }

    private void loadIssues() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        String query = "SELECT description FROM issues"; // zapytanie sql do pobrania opisów spraw
        try (Connection conn = DatabaseConnection.getConnection(); // otwarcie połączenia z bazą danych
             PreparedStatement stmt = conn.prepareStatement(query); // przygotowanie zapytania
             ResultSet rs = stmt.executeQuery()) { // wykonanie zapytania

            while (rs.next()) { // przeglądanie wyników zapytania
                model.addElement(rs.getString("description")); // dodawanie opisu sprawy do modelu comboboxa
            }
            issueComboBox.setModel(model); // ustawienie modelu dla comboboxa
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania spraw: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateTicket(String issueDescription) {
        String insertQuery = "INSERT INTO tickets (issue_id, ticket_number, station_assigned) VALUES (?, ?, ?)"; // zapytanie sql do wstawienia nowego biletu
        String selectQuery = "SELECT issue_id FROM issues WHERE description = ?"; // zapytanie sql do pobrania id sprawy na podstawie opisu

        try (Connection conn = DatabaseConnection.getConnection(); // otwarcie połączenia z bazą danych
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery); // przygotowanie zapytania do pobrania id
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) { // przygotowanie zapytania do wstawienia biletu

            selectStmt.setString(1, issueDescription); // ustawienie parametru zapytania
            ResultSet rs = selectStmt.executeQuery(); // wykonanie zapytania
            if (rs.next()) {
                int issueId = rs.getInt("issue_id"); // pobranie id sprawy
                int ticketNumber = (int) (Math.random() * 10000 + 1); // generowanie losowego numeru biletu
                int stationAssigned = (int) (Math.random() * 10 + 1); // przypisanie losowego numeru stanowiska

                insertStmt.setInt(1, issueId);
                insertStmt.setInt(2, ticketNumber);
                insertStmt.setInt(3, stationAssigned);
                insertStmt.executeUpdate(); // wykonanie zapytania do wstawienia biletu

                ticketInfo.setText("Bilet numer: " + ticketNumber + ", Stanowisko: " + stationAssigned); // aktualizacja etykiety z informacjami o bilecie
            } else {
                JOptionPane.showMessageDialog(this, "Nie znaleziono takiej sprawy", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        } catch ( SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd podczas generowania biletu: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}