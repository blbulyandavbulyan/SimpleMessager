package org.blbulyandavbulyan.client.processings.imageprocessing;

import javax.swing.*;
import java.awt.*;

public class ImageProcessing {

    public static ImageIcon scaleImage(ImageIcon imageIcon, Dimension scaledSize, int hinst) {
        if (scaledSize.height > imageIcon.getIconHeight() && scaledSize.width > imageIcon.getIconWidth())
            return imageIcon;
        double yScale = (double) Math.max(imageIcon.getIconHeight(), scaledSize.height) / Math.min(imageIcon.getIconHeight(), scaledSize.height);
        double xScale = (double) Math.max(imageIcon.getIconWidth(), scaledSize.width) / Math.min(imageIcon.getIconWidth(), scaledSize.width);
        double scale = Math.max(yScale, xScale);
        scale = scaledSize.height < imageIcon.getIconHeight() || scaledSize.width < imageIcon.getIconWidth() ? 1 / scale : scale;
        return new ImageIcon(imageIcon.getImage().getScaledInstance((int) (imageIcon.getIconWidth() * scale), (int) (imageIcon.getIconHeight() * scale), hinst));
    }
}