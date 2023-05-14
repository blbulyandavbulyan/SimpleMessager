package org.blbulyandavbulyan.smclient.ui.components.custom.buttons;

import org.blbulyandavbulyan.smclient.ui.common.WorkWithSvg;

import javax.swing.*;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;

import static org.blbulyandavbulyan.smclient.ui.common.WorkWithSvg.redrawSvgIcon;

public class RoundCloseButton extends JButton {
    private static final String svgIcon;
    private static final String svgPressIcon;
    private static final String svgRollIcon;
    static{
        try {
            ClassLoader cL = Thread.currentThread().getContextClassLoader();
            try(InputStream resourceInputStream = cL.getResourceAsStream("resources/icons/close-icon.svg")){
                svgIcon = new String(resourceInputStream.readAllBytes());
            }
            try(InputStream resourceInputStream = cL.getResourceAsStream("resources/icons/close-icon-pressed.svg")){
                svgPressIcon = new String(resourceInputStream.readAllBytes());
            }
            try(InputStream resourceInputStream = cL.getResourceAsStream("resources/icons/close-icon-focused.svg")){
                svgRollIcon = new String(resourceInputStream.readAllBytes());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public RoundCloseButton() {
        // These statements enlarge the button so that it
        // becomes a circle rather than an oval.


        Dimension size = getPreferredSize();
        if(size.width != 0 && size.height != 0){
            size.width = size.height = Math.max(size.width,size.height);
            setPreferredSize(size);
            setIcon(WorkWithSvg.redrawSvgIcon(svgIcon, size.width, size.height));
            setPressedIcon(WorkWithSvg.redrawSvgIcon(svgPressIcon, size.width, size.height));
            setRolloverIcon(WorkWithSvg.redrawSvgIcon(svgRollIcon, size.width, size.height));
        }



        // This call causes the JButton not to paint
        // the background.
        // This allows us to paint a round background.
        setRolloverEnabled(true);
        setContentAreaFilled(false);
        setFocusPainted(false);
    }
    private void redrawSvgIcons(Dimension d){
        redrawSvgIcons(d.width, d.height);
    }
    private void redrawSvgIcons(int width, int height){
        width = height = Math.max(width, height);
        if(width > 0){
            setIcon(WorkWithSvg.redrawSvgIcon(svgIcon, width, height));
            setPressedIcon(WorkWithSvg.redrawSvgIcon(svgPressIcon, width, height));
            setRolloverIcon(WorkWithSvg.redrawSvgIcon(svgRollIcon, width, height));
        }

    }
    public void setPreferredSize(Dimension d){
        super.setPreferredSize(d);
        redrawSvgIcons(d);
    }
    public void setSize(int width, int height){
        width = height = Math.max(width, height);
        super.setSize(width, height);
        redrawSvgIcons(width, height);
    }
    public void setSize(Dimension d){
        this.setSize(d.width, d.height);
    }
        // Paint the round background and label.
    protected void paintComponent(Graphics g) {
        g.fillOval(0, 0, getSize().width-1,
                getSize().height-1);

        // This call will paint the label and the
        // focus rectangle.
        super.paintComponent(g);
    }

    // Paint the border of the button using a simple stroke.
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawOval(0, 0, getSize().width-1,
                getSize().height-1);
    }

    // Hit detection.
    private Shape shape;
    public boolean contains(int x, int y) {
// If the button has changed size,
        // make a new shape object.
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        }
        return shape.contains(x, y);
    }

    // Test routine.
}

