import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;


public class Gui {
    public static void main(String[] args) {
        // Fenster erstellen
        JFrame frame = new JFrame("Scrollbare Liste");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(null);

        JButton button1 = new JButton("Aufgabe löschen");

        button1.setBounds(390, 360, 150, 50);

        JButton button2 = new JButton("Aufgabe hinzufügen");

        button2.setBounds(390, 280, 150, 50);



        // Beispiel-Daten


        DefaultListModel<String> listModel = loadTasks();
        JList<String> list = new JList<>(listModel);


        // Scrollbar hinzufügen
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBounds(50, 50, 300, 400);


        // ScrollPane zum Fenster hinzufügen
        frame.add(scrollPane);
        list.addMouseListener(new java.awt.event.MouseAdapter() {	//erkennt maus click
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {						// anzahl der maus clicks
                    int index = list.locationToIndex(e.getPoint());
                    if (index != -1) {
                        String selected = list.getModel().getElementAt(index);

                        // Neues Fenster öffnen
                        JFrame infoFrame = new JFrame("Info zu: " + selected);
                        infoFrame.setSize(320, 350);
                        infoFrame.setLayout(null);

                        JTextArea info = new JTextArea(loadDescription(selected));
                        info.setLineWrap(true);
                        info.setWrapStyleWord(true);
                        info.setEditable(false);

                        JScrollPane scroll = new JScrollPane(info);
                        scroll.setBounds(10, 30, 280, 250);

                        JLabel label = new JLabel(selected);
                        label.setBounds(10, 5, 280, 20);

                        infoFrame.add(scroll);
                        infoFrame.add(label);
                        infoFrame.setLocationRelativeTo(frame);
                        infoFrame.setVisible(true);
                    }
                }
            }
        });

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame addTaskFrame = new JFrame("Add Task");
                addTaskFrame.setSize(400, 500);
                addTaskFrame.setLayout(null);

                JLabel taskLabel = new JLabel("Aufgabentitel:");
                taskLabel.setBounds(10, 25, 100, 20);
                addTaskFrame.add(taskLabel);

                JTextField taskName = new JTextField(10);
                taskName.setBounds(10, 50, 350, 20);
                addTaskFrame.add(taskName);

                JLabel taskDescriptionLabel = new JLabel("Beschreibung: ");
                taskDescriptionLabel.setBounds(10, 95, 100, 20);
                addTaskFrame.add(taskDescriptionLabel);

                JTextArea taskDescription = new JTextArea();
                taskDescription.setLineWrap(true);
                taskDescription.setWrapStyleWord(true);
                taskDescription.setEditable(true);
                addTaskFrame.add(taskDescription);

                JScrollPane scroll = new JScrollPane(taskDescription);
                scroll.setBounds(10, 120, 350, 250);
                addTaskFrame.add(scroll);

                JButton addTaskButton = new JButton("Hinzufügen");
                addTaskButton.setBounds(10, 385, 350, 50);
                addTaskFrame.add(addTaskButton);
                addTaskButton.addActionListener(a -> addTaskFrame.dispose());

                addTaskButton.addActionListener(a -> {
                    String name = taskName.getText().trim();
                    String desc = taskDescription.getText().trim();
                    if (!name.isEmpty()) {
                        listModel.addElement(name);
                        saveTask(name);
                        saveDescription(name, desc);
                        addTaskFrame.dispose();
                    }
                });




                addTaskFrame.setLocationRelativeTo(frame);
                addTaskFrame.setVisible(true);
            }
        });
        frame.add(button1);
        frame.add(button2);

        // Fenster sichtbar machen
        frame.setVisible(true);

        // Task Löschen
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = list.getSelectedIndex();
                if (selectedIndex != -1) {
                    String taskName = listModel.getElementAt(selectedIndex);

                    // Bestätigungsdialog anzeigen
                    int confirm = JOptionPane.showConfirmDialog(
                            null,
                            "Aufgabe \"" + taskName + "\" wirklich löschen?",
                            "Löschen bestätigen",
                            JOptionPane.YES_NO_OPTION
                    );

                    // Wenn bestätigt:
                    if (confirm == JOptionPane.YES_OPTION) {
                        listModel.remove(selectedIndex); // Entfernt den Task

                        // schreibt die tasks.txt neu
                        ArrayList<String> remainingTasks = new ArrayList<>();
                        for (int i = 0; i < listModel.size(); i++) {
                            remainingTasks.add(listModel.getElementAt(i));
                        }
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt"))) {
                            for (String task : remainingTasks) {
                                writer.write(task);
                                writer.newLine();
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        // löscht die beschreibung
                        File descriptionFile = new File(taskName + ".txt");
                        if (descriptionFile.exists()) {
                            descriptionFile.delete();
                        }
                    }
                }
            }
        });
    }

    private static DefaultListModel<String> loadTasks() {
        DefaultListModel<String> model = new DefaultListModel<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("tasks.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                model.addElement(line);
            }
        } catch (IOException e) {
            System.out.println("Keine Aufgaben gefunden.");
        }
        return model;
    }

    private static void saveTask(String taskName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt", true))) {
            writer.write(taskName);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveDescription(String taskName, String description) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(taskName + ".txt"))) {
            writer.write(description);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String loadDescription(String taskName) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(taskName + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            sb.append("Keine Beschreibung gefunden.");
        }
        return sb.toString();
    }
}


