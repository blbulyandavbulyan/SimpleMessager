package org.blbulyandavbulyan.client.ui.components.displayers.general.viewwindows;

import javax.swing.*;

public class ScrollableViewWindow extends JFrame {
    protected final JScrollPane mainScrollPane;
    public ScrollableViewWindow() {
        mainScrollPane = new JScrollPane();
        this.getContentPane().add(mainScrollPane);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
    }
}
