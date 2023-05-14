package org.blbulyandavbulyan.client.ui.components.custom.textfieldswithghosttext;

import org.blbulyandavbulyan.client.ui.ghosttextt.GhostText;
import org.blbulyandavbulyan.client.ui.ghosttextt.GhostTextInterface;

import javax.swing.*;
import java.awt.*;

public class JTextFiledWithGhostText extends JTextField implements GhostTextInterface {
    private final GhostText ghostText;
    public JTextFiledWithGhostText(String ghostTextStr){
        this.ghostText = new GhostText(this, ghostTextStr, super::setText);
    }
    @Override public String getText(){
        String text =super.getText();
        if(ghostText != null && text.equals(ghostText.getGhostText()))return "";
        else return text;
    }
    @Override public void setText(String text){
        if(ghostText != null){
            if(text == null || text.isEmpty())ghostText.clear();
            else ghostText.setText(text);
        }
        else super.setText(text);
    }
    @Override
    public boolean isEmpty(){
        return ghostText.isEmpty();
    }
    @Override
    public void setGhostText(String ghostText) {
        this.ghostText.setGhostText(ghostText);
    }

    @Override
    public String getGhostText() {
        return this.ghostText.getGhostText();
    }

    @Override
    public void setGhostTextEnabled(boolean enabled) {
        ghostText.setGhostTextEnabled(enabled);
    }

    @Override
    public boolean isGhostTextEnabled() {
        return ghostText.isGhostTextEnabled();
    }

    @Override
    public void setGhostTextColor(Color ghostTextColor) {
        ghostText.setGhostTextColor(ghostTextColor);
    }

    @Override
    public Color getGhostTextColor() {
        return ghostText.getGhostTextColor();
    }
}
