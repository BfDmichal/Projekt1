import javax.swing.*;

/**
 * @author Julia Stefanowicz
 * @author Michał Nowicki
 * @author Michał Przyborowski
 * @author Mateusz Grabiński
 * @version 1.0
 * Klasa Projekt1 reprezentuje główną klasę aplikacji.
 */

public class Projekt1 {

    /**
     * Metoda główna uruchamiająca aplikację.
     *
     * @param args argumenty wiersza poleceń
     */
    public static void main(String[] args) {

        AppWindow window = new AppWindow();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBounds(400, 400, 560, 550);
        window.setResizable(false);
    }
}
