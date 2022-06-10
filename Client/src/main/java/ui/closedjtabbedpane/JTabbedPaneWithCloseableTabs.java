package ui.closedjtabbedpane;


import javax.swing.*;
import java.awt.*;

public class JTabbedPaneWithCloseableTabs extends javax.swing.JTabbedPane {
    private CloseTabAction defaultCloseTabAction;
    private static class TabCloseButton extends RoundCloseButton{
        public TabCloseButton(javax.swing.JTabbedPane tabPane, int tabIndex, CloseTabAction closeTabAction){
            super.addActionListener(
                    (a)-> {
                        if(closeTabAction != null){
                            String titleRemovedTab = tabPane.getTitleAt(tabIndex);
                            closeTabAction.closeTab(tabPane, tabIndex);

                            closeTabAction.afterRemoveTab(titleRemovedTab);
                        }
                        else tabPane.removeTabAt(tabIndex);
                    }

            );
        }

    }
    public JTabbedPaneWithCloseableTabs(CloseTabAction defaultCloseTabAction){
        this.defaultCloseTabAction = defaultCloseTabAction;
    }
    public void addCloseableTab(String title, Component component, CloseTabAction closeTabAction) {
        this.addTab(title, component);
        int index = this.indexOfTab(title);
        JPanel pnlTab = new JPanel(new GridBagLayout());
        pnlTab.setOpaque(false);
        JLabel lblTitle = new JLabel(title);
        JTabbedPaneWithCloseableTabs.TabCloseButton btnClose = new JTabbedPaneWithCloseableTabs.TabCloseButton(this, index, closeTabAction);
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
        this.setTabComponentAt(index, pnlTab);
    }
    public void addCloseableTab(String title, Component component){
        addCloseableTab(title, component, defaultCloseTabAction);
    }
}
