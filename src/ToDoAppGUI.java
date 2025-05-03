import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ToDoAppGUI extends JFrame {

    // GUI komponentes
    private DefaultListModel<String> taskListModel; // Modelis priekš JList
    private JList<String> taskList;                 // Vizuālais saraksts
    private JTextField taskInput;                   // Ievades lauks
    private JButton addButton;                      // Pievienošanas poga

    // DB pārvaldnieks
    private DatabaseManager dbManager;

    // Konstruktors
    public ToDoAppGUI(DatabaseManager dbManager) {
        this.dbManager = dbManager;

        // Loga iestatījumi
        setTitle("ToDo App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Logs centrā

        // Galvenā loga izkārtojums
        setLayout(new BorderLayout(5, 5)); // Atstarpes starp apgabaliem

        // Komponenšu inicializācija
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel); // *** LABOJUMS: Modelis pievienots uzreiz ***
        taskInput = new JTextField(20);        // Lauka platums apm. 20 simboliem
        addButton = new JButton("Pievienot");

        // Augšējais panelis ievadei
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Elementi no kreisās

        // Elementu pievienošana augšējam panelim
        inputPanel.add(new JLabel("Jauns uzdevums:"));
        inputPanel.add(taskInput);
        inputPanel.add(addButton);

        // Paneļa pievienošana logam (augšā)
        add(inputPanel, BorderLayout.NORTH);

        // Saraksta pievienošana ar scroll joslu (centrā)
        add(new JScrollPane(taskList), BorderLayout.CENTER);

        // Klausītājs (listener) pogai un ievades laukam (Enter taustiņam)
        ActionListener addTaskListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Izsaucam metodi uzdevuma pievienošanai
                addTask();
            }
        };

        // Pievienojam klausītāju pogai un ievades laukam
        addButton.addActionListener(addTaskListener);
        taskInput.addActionListener(addTaskListener); // Reaģē arī uz Enter nospiešanu laukā

        // Ielādējam esošos uzdevumus no DB startā
        loadTasksFromDatabase();
    }

    // Metode uzdevumu ielādei no DB un attēlošanai sarakstā
    private void loadTasksFromDatabase() {
        taskListModel.clear(); // Notīrām esošo sarakstu modelī

        // Iegūstam uzdevumus no DB pārvaldnieka (atgriež ArrayList<String>)
        List<String> tasks = dbManager.getAllTasks();

        // Pārbaudām, vai saraksts nav null (lai gan mūsu gadījumā tas vienmēr būs vismaz tukšs ArrayList)
        if (tasks != null) {
            // Iterējam cauri iegūtajiem uzdevumiem
            for (String task : tasks) {
                taskListModel.addElement(task); // Pievienojam katru uzdevumu modelim
            }
        }

        System.out.println("Ielādēti " + taskListModel.getSize() + " uzdevumi no DB.");
    }

    // Metode jauna uzdevuma pievienošanai
    private void addTask() {
        // Iegūstam tekstu no ievades lauka, noņemam liekās atstarpes sākumā/beigās
        String taskDescription = taskInput.getText().trim();

        // Pārbaudām, vai teksts nav tukšs
        if (!taskDescription.isEmpty()) {
            // Mēģinām pievienot uzdevumu datubāzei
            boolean addedToDb = dbManager.addTask(taskDescription); // Atgriež true vai false

            // Ja pievienošana DB bija veiksmīga
            if (addedToDb) {
                taskListModel.addElement(taskDescription); // Pievienojam uzdevumu arī vizuālajam sarakstam (modelim)
                taskInput.setText(""); // Notīrām ievades lauku
                System.out.println("Uzdevums: '" + taskDescription + "' pievienots.");
            } else {
                // Ja neizdevās saglabāt DB, parādām kļūdas paziņojumu
                JOptionPane.showMessageDialog(this, // Piesaistām ziņojumu šim logam
                        "Neizdevās saglabāt uzdevumu datu bāzē!", // Ziņojuma teksts
                        "Datu bāzes kļūda!",                  // Loga virsraksts
                        JOptionPane.ERROR_MESSAGE             // Ziņojuma tips (ikona)
                );
            }
        }
    }
}