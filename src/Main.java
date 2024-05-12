public class Main {
    public static void main(String[] args) {
        // uruchomienie interfejsu użytkownika (ui) w dedykowanym wątku dla swinga, wątku dystrybucji zdarzeń (edt),
        // co jest wymagane do bezpiecznego zarządzania elementami ui w środowisku wielowątkowym.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();  // wywołanie metody, która tworzy i pokazuje graficzny interfejs użytkownika
            }
        });
    }

    private static void createAndShowGUI() {
        // tworzenie instancji klasy mainframe, która jesąt głównym oknem aplikacji
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);  // ustawienie okna na widoczne, co powoduje, że okno staje się widoczne na ekranie
    }
}