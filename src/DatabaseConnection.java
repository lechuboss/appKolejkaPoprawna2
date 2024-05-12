import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/queue_system";  // URL do bazy danych
    private static final String USER = "root";  // Nazwa użytkownika bazy danych
    private static final String PASSWORD = "12345678";  // Hasło użytkownika bazy danych

    public static Connection getConnection() throws SQLException {
        // Metoda statyczna zwracająca połączenie do bazy danych używając danych logowania
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}