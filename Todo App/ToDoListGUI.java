import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

class Task {
    String description;
    boolean completed;
    int priority;
    Date deadline;

    public Task(String description, int priority, Date deadline) {
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.completed = false;
    }

    public void markAsCompleted() {
        this.completed = true;
    }

    public int getPriority() {
        return priority;
    }

    public Date getDeadline() {
        return deadline;
    }

    @Override
    public String toString() {
        return "Task: " + description + ", Priority: " + priority + ", Deadline: " + new SimpleDateFormat("yyyy-MM-dd").format(deadline) + ", Completed: " + completed;
    }

}

class ToDoList {
    DefaultListModel<Task> tasks;

    public ToDoList() {
        tasks = new DefaultListModel<>();
    }

    public void addTask(Task task) {
        tasks.addElement(task);
    }

    public void deleteTask(int index) {
        tasks.remove(index);
    }

    public void markTaskAsCompleted(int index) {
        tasks.get(index).markAsCompleted();
    }

    public void sortByPriority() {
        ArrayList<Task> taskList = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            taskList.add(tasks.get(i));
        }
        taskList.sort(Comparator.comparingInt(Task::getPriority));
        for (int i = 0; i < taskList.size(); i++) {
            tasks.set(i, taskList.get(i));
        }
    }

    public void sortByDeadline() {
        ArrayList<Task> taskList = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            taskList.add(tasks.get(i));
        }
        taskList.sort(Comparator.comparing(Task::getDeadline));
        for (int i = 0; i < taskList.size(); i++) {
            tasks.set(i, taskList.get(i));
        }
    }

}

public class ToDoListGUI {
    private JFrame frame;
    private ToDoList todoList;
    private JList<Task> taskList;
    private JTextField descriptionField;
    private JComboBox<Integer> priorityBox;
    private JTextField deadlineField;

    public ToDoListGUI() {
        todoList = new ToDoList();

        frame = new JFrame("To-Do List");
        frame.setBounds(350, 200, 600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(1, 4));

        descriptionField = new JTextField();
        priorityBox = new JComboBox<>(new Integer[] { 1, 2, 3 });
        deadlineField = new JTextField("YYYY-MM-DD");

        inputPanel.add(descriptionField);
        inputPanel.add(priorityBox);
        inputPanel.add(deadlineField);

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String description = descriptionField.getText();
                int priority = (int) priorityBox.getSelectedItem();
                Date deadline = parseDate(deadlineField.getText());
                if (description.length() > 0 && deadline != null) {
                    todoList.addTask(new Task(description, priority, deadline));
                    updateTaskList();
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid description and deadline!");
                }
            }
        });
        inputPanel.add(addButton);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        JButton deleteButton = new JButton("Delete Task");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    todoList.deleteTask(selectedIndex);
                    updateTaskList();
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a task to delete!");
                }
            }
        });
        buttonPanel.add(deleteButton);

        JButton completeButton = new JButton("Complete Task");
        completeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    todoList.markTaskAsCompleted(selectedIndex);
                    updateTaskList();
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a task to mark as completed!");
                }
            }
        });
        buttonPanel.add(completeButton);

        JButton sortByPriorityButton = new JButton("Sort by Priority");
        sortByPriorityButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                todoList.sortByPriority();
                updateTaskList();
            }
        });
        buttonPanel.add(sortByPriorityButton);

        JButton sortByDeadlineButton = new JButton("Sort by Deadline");
        sortByDeadlineButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                todoList.sortByDeadline();
                updateTaskList();
            }
        });
        buttonPanel.add(sortByDeadlineButton);

        taskList = new JList<>(todoList.tasks);
        frame.add(new JScrollPane(taskList), BorderLayout.CENTER);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void updateTaskList() {
        taskList.setModel(todoList.tasks);
    }

    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        new ToDoListGUI();
    }
}
