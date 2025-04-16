import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui {
    public static void main(String[] args) {
        // Fenster erstellen
        JFrame frame = new JFrame("Scrollbare Liste");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(null);

        JButton button = new JButton("Aufgabe löschen");

        button.setBounds(390, 360, 150, 50);

        JButton button2 = new JButton("Aufgabe hinzufügen");

        button2.setBounds(390, 280, 150, 50);



        // Beispiel-Daten


        String[] items = {
                "Eintrag 1", "Eintrag 2", "Eintrag 3",
                "Eintrag 4", "Eintrag 5", "Eintrag 6",
                "Eintrag 7", "Eintrag 8", "Eintrag 9",
                "Eintrag 10", "Eintrag 11", "Eintrag 12"
        };


        // Liste erstellen
        JList<String> list = new JList<>(items);

        // Scrollbar hinzufügen
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBounds(50, 50, 300, 400);


        // ScrollPane zum Fenster hinzufügen
        frame.add(scrollPane);
        list.addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                String selected = list.getSelectedValue();

                // Neues Fenster öffnen
                JFrame infoFrame = new JFrame("Info zu: " + selected);
                infoFrame.setSize(320, 350);
                infoFrame.setLayout(null);

                // JTextArea mit Text erstellen
                JTextArea info = new JTextArea("Du hast ausgewählt: " + selected);
                info.setLineWrap(true);
                info.setWrapStyleWord(true);
                info.setEditable(false);

                // JScrollPane erstellen und JTextArea hinzufügen
                JScrollPane scroll = new JScrollPane(info);
                scroll.setBounds(10, 30, 280, 250); // Position und Größe des Scrollpanes festlegen

                // JLabel hinzufügen
                JLabel label = new JLabel(selected);
                label.setBounds(10, 5, 280, 20);

                // Komponenten zum Fenster hinzufügen
                infoFrame.add(scroll);
                infoFrame.add(label);

                // Fenster positionieren und sichtbar machen
                infoFrame.setLocationRelativeTo(frame);
                infoFrame.setVisible(true);
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

                addTaskFrame.setLocationRelativeTo(frame);
                addTaskFrame.setVisible(true);
            }
        });
        frame.add(button);
        frame.add(button2);

        // Fenster sichtbar machen
        frame.setVisible(true);


    }
}