package ui.components.displayers.general.areas;

import processings.imageprocessing.ImageProcessing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MiniatureImageArea extends ImageArea {
    protected MiniatureImageArea(ImageIcon imageIcon, Dimension miniatureSize) {
        super(ImageProcessing.scaleImage(imageIcon, miniatureSize, Image.SCALE_AREA_AVERAGING));
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                displayedImage = ImageProcessing.scaleImage(originalImage, e.getComponent().getSize(), Image.SCALE_AREA_AVERAGING);
                e.getComponent().revalidate();
                super.componentResized(e);
            }
        });
    }
    public MiniatureImageArea(ImageIcon imageIcon, int preferredHeight){
        this(imageIcon, calculatePreferredScaledSize(imageIcon, preferredHeight));
    }

    public static Dimension calculatePreferredScaledSize(ImageIcon imageIcon, int preferredHeight){
        Dimension preferredScaledSize = new Dimension();
        double imageAspectRatio = getImageAspectRatio(imageIcon);
        preferredScaledSize.height = preferredHeight;
        preferredScaledSize.width = (int)(preferredHeight * imageAspectRatio);
        return preferredScaledSize;
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.getContentPane().add(new MiniatureImageArea(new ImageIcon("/media/share/ds1/Фото и Видео/Фотографии/20211111_141124.jpg"), new Dimension(400, 300)));
        jFrame.setVisible(true);
    }
}
