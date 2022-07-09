package ui.customuicomponents.filedisplayers.imagedisplayer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ImageArea extends JPanel {
    protected ImageIcon originalImage;
    protected ImageIcon displayedImage;
    public ImageArea(ImageIcon imageIcon, Dimension scaledSize){
        this.originalImage = imageIcon;
        if(scaledSize != null){
            displayedImage = scaleImage(scaledSize, Image.SCALE_AREA_AVERAGING);
            this.setPreferredSize(scaledSize);
        }
        else {
            this.displayedImage = originalImage;
            this.setPreferredSize(new Dimension(originalImage.getIconWidth(), originalImage.getIconHeight()));
        }

        this.setBorder(new LineBorder(new Color(0, 0, 0), 2));
    }
    public ImageArea(ImageIcon image){
        this(image, null);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        displayedImage.paintIcon(this, g, 0, 0);
    }
    protected ImageIcon scaleImage(Dimension scaledSize, int hinst){
        if(scaledSize.height > originalImage.getIconHeight() && scaledSize.width > originalImage.getIconWidth())return originalImage;
        double yScale = (double) Math.max(originalImage.getIconHeight(), scaledSize.height) / Math.min(originalImage.getIconHeight(), scaledSize.height);
        double xScale = (double) Math.max(originalImage.getIconWidth(), scaledSize.width) / Math.min(originalImage.getIconWidth(), scaledSize.width);
        double scale = Math.max(yScale, xScale);
        scale = scaledSize.height < originalImage.getIconHeight() || scaledSize.width < originalImage.getIconWidth()  ? 1/scale : scale;
        return new ImageIcon(originalImage.getImage().getScaledInstance((int) (originalImage.getIconWidth()*scale), (int) (originalImage.getIconHeight()*scale), hinst));
    }
}