package ui.customuicomponents.closedjtabbedpane;


import javax.swing.*;
import java.awt.*;

public class JTabbedPaneWithCloseableTabs extends javax.swing.JTabbedPane {
    private final CloseTabAction defaultCloseTabAction;
    private final String defaultCloseButtonToolTipText;
    private static class TabCloseButton extends RoundCloseButton{
        public TabCloseButton(javax.swing.JTabbedPane tabPane, String titleRemovedTab, CloseTabAction closeTabAction){
            super.addActionListener(
                    (a)-> {
                        if(closeTabAction != null){
                            closeTabAction.closeTab(tabPane, titleRemovedTab);

                            closeTabAction.afterRemoveTab(titleRemovedTab);
                        }
                        else tabPane.removeTabAt(tabPane.indexOfTab(titleRemovedTab));
                    }

            );
        }

    }
    public JTabbedPaneWithCloseableTabs(CloseTabAction defaultCloseTabAction, String defaultCloseButtonToolTipText){
        this.defaultCloseTabAction = defaultCloseTabAction;
        this.defaultCloseButtonToolTipText = defaultCloseButtonToolTipText;
    }
    public void addCloseableTab(String title, Component component, CloseTabAction closeTabAction, String closeButtonToolTipText) {
        this.addTab(title, component);
        int index = this.indexOfTab(title);
        JPanel pnlTab = new JPanel(new GridBagLayout());
        pnlTab.setOpaque(false);
        JLabel lblTitle = new JLabel(title);
        JTabbedPaneWithCloseableTabs.TabCloseButton btnClose = new JTabbedPaneWithCloseableTabs.TabCloseButton(this, title, closeTabAction);
        btnClose.setToolTipText(closeButtonToolTipText);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        pnlTab.add(lblTitle, gbc);
        gbc.gridx++;
        gbc.weightx = 0;
        gbc.insets = new Insets(2, 2, 2, 0);
        btnClose.setPreferredSize(new Dimension(15, 15));
        btnClose.setBorder(null);
        btnClose.setBorderPainted(false);
        pnlTab.add(btnClose, gbc);
        this.setTabComponentAt(index, pnlTab);
    }
    public void addCloseableTab(String title, Component component){
        addCloseableTab(title, component, defaultCloseTabAction, defaultCloseButtonToolTipText);
    }
}
