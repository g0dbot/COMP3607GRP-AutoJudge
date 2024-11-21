package com.hado90.ui.screen;

import javax.swing.*;

import java.awt.*;

public class JudgeButtonPanel extends JPanel {
    private JButton judgeButton;

    public JudgeButtonPanel(DashScreen dashScreen, Color bgBtn, Color fgbtn) {
        setLayout(new FlowLayout(FlowLayout.CENTER));

        judgeButton = new JButton("JUDGE");
        judgeButton.setPreferredSize(new Dimension(150, 40));
        judgeButton.setBackground(bgBtn);
        judgeButton.setFont(new Font("Monospace", Font.PLAIN, 20));
        judgeButton.setForeground(fgbtn);
        judgeButton.setEnabled(false);

        judgeButton.addActionListener(e -> {
            dashScreen.executeJudgeLogic();
        });

        add(judgeButton);
    }

    public void updateJudgeButtonState() {
        boolean allPathsSet = ((DashScreen) SwingUtilities.getWindowAncestor(this)).isStudentPathSet()
                && ((DashScreen) SwingUtilities.getWindowAncestor(this)).isTestPathSet()
                && ((DashScreen) SwingUtilities.getWindowAncestor(this)).isOutputPathSet();

        judgeButton.setEnabled(allPathsSet);

        if (allPathsSet) {
            judgeButton.setBackground(Color.GREEN);
        } else {
            judgeButton.setBackground(Color.RED);
        }
    }

}
