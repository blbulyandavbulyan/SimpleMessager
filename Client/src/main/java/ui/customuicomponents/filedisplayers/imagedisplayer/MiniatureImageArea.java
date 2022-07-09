package ui.customuicomponents.filedisplayers.imagedisplayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MiniatureImageArea extends ImageArea{
    public MiniatureImageArea(ImageIcon imageIcon, Dimension miniatureSize) {
        super(imageIcon, miniatureSize);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                displayedImage = ((ImageArea)e.getComponent()).scaleImage(e.getComponent().getSize(), Image.SCALE_AREA_AVERAGING);
                e.getComponent().revalidate();
                super.componentResized(e);
            }
        });
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.getContentPane().add(new MiniatureImageArea(new ImageIcon("/media/share/ds1/Фото и Видео/Фотографии/20211111_141124.jpg"), new Dimension(400, 300)));
        jFrame.setVisible(true);
    }
}
