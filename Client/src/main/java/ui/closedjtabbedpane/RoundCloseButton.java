package ui.closedjtabbedpane;

import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class RoundCloseButton extends JButton {
    private static String svgIcon;
    private static String svgPressIcon;
    private static String svgRollIcon;
    static{
        try {
            svgIcon = new String(Files.readAllBytes(Path.of(ClassLoader.getSystemResource("icons/close-icon.svg").getPath())));
            svgPressIcon =  new String(Files.readAllBytes(Path.of(ClassLoader.getSystemResource("icons/close-icon-pressed.svg").getPath())));
            svgRollIcon =  new String(Files.readAllBytes(Path.of(ClassLoader.getSystemResource("icons/close-icon-focused.svg").getPath())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Icon redrawSvgIcon(String svg, int width, int height) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        Reader reader = new BufferedReader(new StringReader(svg));
        TranscoderInput svgImage = new TranscoderInput(reader);
        TranscoderOutput pngImage = new TranscoderOutput(result);
        ImageTranscoder transcoder = new PNGTranscoder();
        transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, (float)height);
        transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, (float)width);
        try {
            transcoder.transcode(svgImage, pngImage);
        } catch (TranscoderException e) {
            throw new RuntimeException(e);
        }
        try{
            return new ImageIcon(ImageIO.read(new ByteArrayInputStream(result.toByteArray())));
        }
        catch (IOException e){
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
    public void setPreferredSize(Dimension d){
        super.setPreferredSize(d);
        setIcon(redrawSvgIcon(svgIcon, d.width, d.height));
        setPressedIcon(redrawSvgIcon(svgPressIcon, d.width, d.height));
        setRolloverIcon(redrawSvgIcon(svgRollIcon, d.width, d.height));
    }
    public void setSize(int width, int height){
        super.setSize(width, height);
        setIcon(redrawSvgIcon(svgIcon, width, height));
        setPressedIcon(redrawSvgIcon(svgPressIcon, width, height));
        setRolloverIcon(redrawSvgIcon(svgRollIcon, width, height));
    }
    public void setSize(Dimension d){
        super.setSize(d);
        setPressedIcon(redrawSvgIcon(svgPressIcon, d.width, d.height));
        setRolloverIcon(redrawSvgIcon(svgRollIcon, d.width, d.height));
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
        frame.getContentPane().add(button);
        button.setPreferredSize(new Dimension(500, 500));
        frame.getContentPane().setLayout(new FlowLayout());
        frame.setSize(150, 150);
        frame.setVisible(true);
    }
}

