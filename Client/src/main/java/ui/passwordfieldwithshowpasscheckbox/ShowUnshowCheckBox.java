package ui.passwordfieldwithshowpasscheckbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;

import static ui.common.WorkWithSvg.redrawSvgIcon;

public class ShowUnshowCheckBox extends JCheckBox {
    private static String svgShowIcon;
    private static String svgShowRolloverIcon;
    private static String svgUnshowIcon;
    private static String svgUnshowRolloverIcon;

    static {
        try {
            ClassLoader cL = Thread.currentThread().getContextClassLoader();
            svgShowIcon = new String(cL.getResourceAsStream("resources/icons/show-icon.svg").readAllBytes());
            svgUnshowIcon = new String(cL.getResourceAsStream("resources/icons/unshow-icon.svg").readAllBytes());

            svgShowRolloverIcon = new String(cL.getResourceAsStream("resources/icons/show-rollover-icon.svg").readAllBytes());
            svgUnshowRolloverIcon = new String(cL.getResourceAsStream("resources/icons/unshow-rollover-icon.svg").readAllBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void redrawSvgIcons(Dimension d){
        redrawSvgIcons(d.width, d.height);
    }
    private void redrawSvgIcons(int width, int height){
        height = width = Math.min(height, width);
        this.setIcon(redrawSvgIcon(svgShowIcon, width, height));
        this.setRolloverIcon(redrawSvgIcon(svgShowRolloverIcon, width, height));
        this.setSelectedIcon(redrawSvgIcon(svgUnshowIcon, width, height));
        this.setRolloverSelectedIcon(redrawSvgIcon(svgUnshowRolloverIcon, width, height));
    }
    public ShowUnshowCheckBox(){
        Dimension d = new Dimension(20, 20);
        redrawSvgIcons(d);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension d = e.getComponent().getSize();
                redrawSvgIcons(d);
                super.componentResized(e);
            }
        }
    );
    }
    @Override public void setSize(Dimension d){
        super.setSize(d);
        redrawSvgIcons(d);
    }
    @Override public void setSize(int width, int height){
        super.setSize(width, height);
        if(width > 0 && height > 0)redrawSvgIcons(width, height);
    }
    @Override public void setPreferredSize(Dimension preferredSize){
        super.setPreferredSize(preferredSize);
        if(preferredSize.width > 0 && preferredSize.height > 0)redrawSvgIcons(preferredSize);
    }
}
