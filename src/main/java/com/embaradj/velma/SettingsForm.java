package com.embaradj.velma;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class SettingsForm extends JFrame {
    private Settings settings = Settings.getInstance();
    private JPanel mainPanel;
    private JPanel jobLangPanel;
    private JPanel analyserSettingsPanel;
    private JPanel jobSsykPanel;

    public SettingsForm() {
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);    // Position the frame in the center of the screen
        setVisible(true);

        createSsykCheckboxes();
        createLangCheckboxes();
    }

    private void createSsykCheckboxes() {
        settings.getSsyk().forEach((Ssyk ssyk) -> {
            String checkText = ssyk.getCode() + "   " + ssyk.getDescription();
            JCheckBox checkBox = new JCheckBox(checkText, ssyk.isSelected());
            checkBox.addItemListener(ie -> {
                settings.selectSsyk(ssyk, (ie.getStateChange() == 1) ? true : false);
                if (settings.getSelectedSsyk().length == 0) {
                    showWarning("You must select at least one SSYK code!");
                }
            });

            jobSsykPanel.add(checkBox);
        });
    }

    private void createLangCheckboxes() {
        settings.getLang().forEach((lang, sel) -> {
            JCheckBox checkbox = new JCheckBox(lang, sel);
            checkbox.addItemListener(ie -> {
                settings.selectLang(lang, (ie.getStateChange() == 1) ? true : false);
                if (settings.getSelectedLang().length == 0) {
                    showWarning("You must select at least one language!");
                }
            });

            jobLangPanel.add(checkbox);
        });
    }

    private void showWarning(String warning) {
        JOptionPane.showMessageDialog(null, warning);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
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
        mainPanel.setPreferredSize(new Dimension(500, 500));
        mainPanel.setRequestFocusEnabled(true);
        jobLangPanel = new JPanel();
        jobLangPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(jobLangPanel, gbc);
        jobLangPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Job Ads languages", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        jobSsykPanel = new JPanel();
        jobSsykPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 10.0;
        gbc.weighty = 5.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(jobSsykPanel, gbc);
        jobSsykPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Job Ads SSYK codes", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        analyserSettingsPanel = new JPanel();
        analyserSettingsPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 10.0;
        gbc.weighty = 5.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(analyserSettingsPanel, gbc);
        analyserSettingsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Analyser Settings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
