package ui.closedjtabbedpane;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.*;
import java.io.*;

import static ui.common.WorkWithSvg.redrawSvgIcon;

public class RoundCloseButton extends JButton {
    private static String svgIcon;
    private static String svgPressIcon;
    private static String svgRollIcon;
    static{
        try {
            ClassLoader cL = Thread.currentThread().getContextClassLoader();
            svgIcon = new String(cL.getResourceAsStream("resources/icons/close-icon.svg").readAllBytes());
            svgPressIcon = new String(cL.getResourceAsStream("resources/icons/close-icon-pressed.svg").readAllBytes());
            svgRollIcon =  new String(cL.getResourceAsStream("resources/icons/close-icon-focused.svg").readAllBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public RoundCloseButton() {
        // These statements enlarge the button so that it
        // becomes a circle rather than an oval.


        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width,size.height);
        setPreferredSize(size);
        setRolloverEnabled(true);
        setIcon(redrawSvgIcon(svgIcon, size.width, size.height));
        setPressedIcon(redrawSvgIcon(svgPressIcon, size.width, size.height));
        setRolloverIcon(redrawSvgIcon(svgRollIcon, size.width, size.height));


        // This call causes the JButton not to paint
        // the background.
        // This allows us to paint a round background.
        setContentAreaFilled(false);
        setFocusPainted(false);
    }
    private void redrawSvgIcons(Dimension d){
        redrawSvgIcons(d.width, d.height);
    }
    private void redrawSvgIcons(int width, int height){
        width = height = Math.max(width, height);
        setIcon(redrawSvgIcon(svgIcon, width, height));
        setPressedIcon(redrawSvgIcon(svgPressIcon, width, height));
        setRolloverIcon(redrawSvgIcon(svgRollIcon, width, height));
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
    public static void main(String[] args){
// Create a button with the label "Jackpot".
        JButton button = new RoundCloseButton();

        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(Color.yellow);
        frame.setLayout(new FlowLayout());
        frame.add(button);
        button.setPreferredSize(new Dimension(500, 500));

        frame.setSize(150, 150);
        frame.setVisible(true);
    }
}

