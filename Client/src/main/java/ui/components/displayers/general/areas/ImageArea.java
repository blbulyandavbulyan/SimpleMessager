package ui.components.displayers.general.areas;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ImageArea extends JPanel {
    protected  final ImageIcon originalImage;
    protected ImageIcon displayedImage;
    public ImageArea(ImageIcon imageIcon){
        this.originalImage = imageIcon;
        this.displayedImage = originalImage;
        this.setPreferredSize(new Dimension(originalImage.getIconWidth(), originalImage.getIconHeight()));
        this.setBorder(new LineBorder(new Color(0, 0, 0), 2));
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        displayedImage.paintIcon(this, g, 0, 0);
    }
    public double getImageAspectRatio(){
        return getImageAspectRatio(originalImage);
    }
    public static double getImageAspectRatio(ImageIcon imageIcon){
        return (double) imageIcon.getIconWidth()/imageIcon.getIconHeight();
    }
}