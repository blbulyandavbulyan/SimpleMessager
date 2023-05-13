package ui.components.displayers.general.areas;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public abstract class AbstractImageArea extends JPanel {
    protected  final ImageIcon originalImage;
    protected ImageIcon displayedImage;
    protected AbstractImageArea(ImageIcon imageIcon){
        this.originalImage = imageIcon;
        this.displayedImage = originalImage;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        displayedImage.paintIcon(this, g, 0, 0);
    }

    public static double getImageAspectRatio(ImageIcon imageIcon){
        return (double) imageIcon.getIconWidth()/imageIcon.getIconHeight();
    }
}
