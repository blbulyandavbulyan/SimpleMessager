package org.blbulyandavbulyan.client.ui.common;

import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class WorkWithSvg {
    public static Icon redrawSvgIcon(String svg, int width, int height) {
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
    public static Icon redrawSvgIcon(String svg, Dimension d){
        return redrawSvgIcon(svg, d.width, d.height);
    }
}
