import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        DatabaseManager dbManager = new DatabaseManager();

        try {
            dbManager.connect();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Kritiska kļūda: Nevar izveidot savienojumu ar datubāzi\n" +
                            "Pārbaudiet MYSQL servera statusu un DatabaseManager konfigurāciju\n" +
                            "Kļūdas ziņojums: " + e.getMessage(),
                            "Datubāzes savienojuma kļūda",
                    JOptionPane.ERROR_MESSAGE
                    );

            System.exit(1);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            System.out.println("Programma beidz darbu, aizveram DB savienojumu...");
            dbManager.closeConnection();
        }));

        SwingUtilities.invokeLater(() -> {
            ToDoAppGUI appwindow = new ToDoAppGUI(dbManager);

            appwindow.setVisible(true);
        });

    }
}