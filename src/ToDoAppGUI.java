import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToDoAppGUI extends JFrame {

    // components
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private JTextField taskInput;
    private JButton addButton;

    // db manager
    private DatabaseManager dbManager;

    // constructor
    public ToDoAppGUI(DatabaseManager dbManager) {
        this.dbManager = dbManager;

        // window
        setTitle("Todo App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(5, 5));

        // init components
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>();
        taskInput = new JTextField(20);
        addButton = new JButton("Pievienot");

        // top panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // add elements to panel
        inputPanel.add(new JLabel("Jauns uzdevums:"));
        inputPanel.add(taskInput);
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.NORTH);

        // add list to scroll
        add(new JScrollPane(taskList), BorderLayout.CENTER);

        // listeners
        ActionListener addTaskListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // need to implement
                addTask();
            }
        };

        addButton.addActionListener(addTaskListener);
        taskInput.addActionListener(addTaskListener);

        // need to implement
        loadTasksFromDatabase();
    }

    // load tasks
    private void loadTasksFromDatabase() {
        taskListModel.clear();

        List<String> tasks = dbManager.getAllTasks();

        if (tasks != null) {
            for (String task : tasks) {
                taskListModel.addElement(task);
            }
        }

        System.out.println("Ielādēti " + taskListModel.getSize() + " uzdevumi no DB.");
    }

    // add tasks
    private void addTask() {
        String taskDescription = taskInput.getText().trim();

        if (!taskDescription.isEmpty()) {
            boolean addedToDb = dbManager.addTask(taskDescription); // true vai false

            if (addedToDb) {
                taskListModel.addElement(taskDescription);
                taskInput.setText("");
                System.out.println("Uzdevums: '" + taskDescription + "' pievienots.");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Neizdevās saglabāt uzdevumu datu bāzē!",
                        "Datu bāzes kļūda!",
                        JOptionPane.ERROR_MESSAGE
                        );
            }
        }
    }

}
