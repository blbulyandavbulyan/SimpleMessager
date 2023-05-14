package org.blbulyandavbulyan.smclient.ui.components.custom.passwordfieldwithshowpasscheckbox;

import org.blbulyandavbulyan.smclient.ui.common.WorkWithSvg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.io.InputStream;

import static org.blbulyandavbulyan.smclient.ui.common.WorkWithSvg.redrawSvgIcon;

public class ShowUnshowCheckBox extends JCheckBox {
    private static final String svgShowIcon;
    private static final String svgShowRolloverIcon;
    private static final String svgUnshowIcon;
    private static final String svgUnshowRolloverIcon;

    static {
        try {
            ClassLoader cL = Thread.currentThread().getContextClassLoader();
            try(InputStream resourceInputStream = cL.getResourceAsStream("resources/icons/show-icon.svg")){
                svgShowIcon = new String(resourceInputStream.readAllBytes());
            }
            try(InputStream resourceInputStream = cL.getResourceAsStream("resources/icons/unshow-icon.svg")) {
                svgUnshowIcon = new String(resourceInputStream.readAllBytes());
            }
            try(InputStream resourceInputStream = cL.getResourceAsStream("resources/icons/show-rollover-icon.svg")) {
                svgShowRolloverIcon = new String(resourceInputStream.readAllBytes());
            }
            try(InputStream resourceInputStream = cL.getResourceAsStream("resources/icons/unshow-rollover-icon.svg")) {
                svgUnshowRolloverIcon = new String(resourceInputStream.readAllBytes());
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void redrawSvgIcons(Dimension d){
        redrawSvgIcons(d.width, d.height);
    }
    private void redrawSvgIcons(int width, int height){
        if(width > 0 && height > 0){
            height = width = Math.min(height, width);
            this.setIcon(WorkWithSvg.redrawSvgIcon(svgShowIcon, width, height));
            this.setRolloverIcon(WorkWithSvg.redrawSvgIcon(svgShowRolloverIcon, width, height));
            this.setSelectedIcon(WorkWithSvg.redrawSvgIcon(svgUnshowIcon, width, height));
            this.setRolloverSelectedIcon(WorkWithSvg.redrawSvgIcon(svgUnshowRolloverIcon, width, height));
        }
        else if(width <= 0  ^ height <= 0){
            height = width = Math.max(height, width);
            this.setIcon(WorkWithSvg.redrawSvgIcon(svgShowIcon, width, height));
            this.setRolloverIcon(WorkWithSvg.redrawSvgIcon(svgShowRolloverIcon, width, height));
            this.setSelectedIcon(WorkWithSvg.redrawSvgIcon(svgUnshowIcon, width, height));
            this.setRolloverSelectedIcon(WorkWithSvg.redrawSvgIcon(svgUnshowRolloverIcon, width, height));
        }
    }
    public ShowUnshowCheckBox(){
        Dimension d = new Dimension(20, 20);
        redrawSvgIcons(d);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                Dimension d = e.getComponent().getSize();
                Insets insets = ((JCheckBox)e.getComponent()).getInsets();
                d.width -= insets.left + insets.right;
                d.height -= insets.top + insets.bottom;
                if (d.width > d.height) {
                    d.width = -1;
                } else {
                    d.height = -1;
                }
                redrawSvgIcons(d.width, d.height);
            }
        }
        );
    }
}
