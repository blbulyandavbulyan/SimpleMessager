package ui.closedjtabbedpane;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClosableJTabedPane {
    private static class TabCloseButton extends RoundCloseButton{
        public TabCloseButton(JTabbedPane tabPane, int tabIndex){
            super.addActionListener((a)-> tabPane.removeTabAt(tabIndex)
            );
        }

    }
    public static void addTab(JTabbedPane tabPane, String title, Component component) {
            tabPane.addTab(title, component);
            int index = tabPane.indexOfTab(title);
            JPanel pnlTab = new JPanel(new GridBagLayout());
            pnlTab.setOpaque(false);
            JLabel lblTitle = new JLabel(title);
            ClosableJTabedPane.TabCloseButton btnClose = new ClosableJTabedPane.TabCloseButton(tabPane, index);
            btnClose.setToolTipText("Закрыть диалог");
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            pnlTab.add(lblTitle, gbc);
            gbc.gridx++;
            gbc.weightx = 0;
            btnClose.setPreferredSize(new Dimension(15, 15));
            btnClose.setBorder(null);
            btnClose.setBorderPainted(false);
            pnlTab.add(btnClose, gbc);
            tabPane.setTabComponentAt(index, pnlTab);
    }
}
