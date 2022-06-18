package ui.ghosttextt;

import javax.swing.*;
import java.awt.*;

public class JPasswordFieldWithGhostText extends JPasswordField implements GhostTextInterface{
    final GhostText ghostText;
    public JPasswordFieldWithGhostText(String ghostText){
        this.ghostText = new GhostText(this, ghostText, super::setText);
    }
    public void clear(){
        ghostText.clear();
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

    @Override
    public boolean isEmpty() {
        return ghostText.isEmpty();
    }
}
