package com.embaradj.velma;

import com.embaradj.velma.models.Job;
import com.embaradj.velma.results.SearchHit;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Window displaying information regarding HVE, a Job
 * The help text or the topic modelling results.
 */
public class DetailsForm extends JFrame {
    private JPanel mainPanel;
    private JButton closeBtn;
    private JTextPane textPane;

    /**
     * Overloaded constructor in order to use the same form for both SearchHits and help menu
     */
    public DetailsForm() {
        $$$setupUI$$$();
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);    // Position the frame in the center of the screen
        setVisible(true);
        closeBtn.addActionListener(e -> {
            dispose();
        });
    }

    /**
     * Display search hits
     * @param searchHit hits
     */
    public DetailsForm(SearchHit searchHit) {
        this();     // Overloaded constructor
        boolean isHve = (searchHit.getType() == "hve");
        setTitle((isHve ? "HVE: " : "Job: ") + searchHit.title());
        textPane.setText(searchHit.getDescription());
        textPane.setCaretPosition(0);   // Scroll to the top

        // Just for some troubleshooting..
        if (Settings.debug() && !isHve) {
            Job job = (Job) searchHit;
            System.out.println(job.id());
        }
    }

    /**
     * Display the help file
     * @param title of the window
     * @param document to display
     */
    public DetailsForm(String title, Document document) {
        this(); // Overloaded constructor
        setTitle(title);

        try {
            textPane.setDocument(document);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an issue opening the Help file");
            dispose();
        }

        textPane.setCaretPosition(0);   // Scroll to the top
    }

    /**
     * Display the topic modelling results
     * @param topics found
     */
    public DetailsForm(HashMap<String, String> topics) {
        this();
        setTitle("LDA Results");
        StyledDocument doc = textPane.getStyledDocument();
        for (Map.Entry<String, String> entry : topics.entrySet()) {
            String topic = entry.getKey();
            String words = entry.getValue().replaceAll("[\\[\\]]", "");
            String formatted = "TOPIC " + topic + ":\t" + words + "\n";
            try {
                doc.insertString(doc.getLength(), formatted, null);
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }
        this.setSize(textPane.getWidth() + 50, textPane.getHeight());
        textPane.setCaretPosition(0);
    }

    public DetailsForm(String title, String text) {
        this();
        setTitle(title);
        StyledDocument doc = textPane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), text, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setMinimumSize(new Dimension(200, 200));
        mainPanel.setName("mainP");
        mainPanel.setPreferredSize(new Dimension(600, 400));
        final JScrollPane scrollPane1 = new JScrollPane();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10.0;
        gbc.weighty = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane1, gbc);
        textPane = new JTextPane();
        textPane.setEditable(false);
        scrollPane1.setViewportView(textPane);
        closeBtn = new JButton();
        closeBtn.setText("Close");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(closeBtn, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
