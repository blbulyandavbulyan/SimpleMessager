package ui.components.displayers.general.areas;

import processings.imageprocessing.ImageProcessing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MiniatureImageArea extends AbstractImageArea {
    //fixme MiniatureImageArea is larger than imageIcon, it shows in the main window in messages

    protected int preferredHeight;
    protected MiniatureImageArea(ImageIcon imageIcon, Dimension miniatureSize, boolean enableImageAutoScale) {
        super(imageIcon);
        displayedImage = ImageProcessing.scaleImage(originalImage, miniatureSize, Image.SCALE_AREA_AVERAGING);
        if(enableImageAutoScale)this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                displayedImage = ImageProcessing.scaleImage(originalImage, calculatePreferredScaledSize(originalImage, e.getComponent().getHeight()), Image.SCALE_AREA_AVERAGING);
                e.getComponent().revalidate();
                e.getComponent().invalidate();
                //System.out.printf("Component resized:\n\tParam String: %s\n\tHeight: %d\n\tWidth: %d\n", e.paramString(), e.getComponent().getHeight(), e.getComponent().getWidth());
            }
        });
    }
    public MiniatureImageArea(ImageIcon imageIcon, int preferredHeight, boolean enableImageAutoScale){
        this(imageIcon, calculatePreferredScaledSize(imageIcon, preferredHeight), enableImageAutoScale);
    }

    public static Dimension calculatePreferredScaledSize(ImageIcon imageIcon, int preferredHeight){
        Dimension preferredScaledSize = new Dimension();
        double imageAspectRatio = getImageAspectRatio(imageIcon);
        preferredScaledSize.height = preferredHeight;
        preferredScaledSize.width = (int)(preferredHeight * imageAspectRatio);
        return preferredScaledSize;
    }


    public void setPreferredHeight(int preferredHeight) {
        Dimension preferredScaledSize = calculatePreferredScaledSize(originalImage, preferredHeight);
        setPreferredSize(preferredScaledSize);
        displayedImage = ImageProcessing.scaleImage(originalImage, preferredScaledSize, Image.SCALE_AREA_AVERAGING);
        this.revalidate();
    }
}
