package org.blbulyandavbulyan.smclient.ui.components.displayers.general.areas;

import javax.swing.*;
import java.awt.*;

public class ImageArea extends AbstractImageArea {
    public ImageArea(ImageIcon imageIcon){
        super(imageIcon);
        this.setPreferredSize(new Dimension(originalImage.getIconWidth(), originalImage.getIconHeight()));
    }
}