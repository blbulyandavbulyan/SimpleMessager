package org.blbulyandavbulyan.client.ui.components.custom.textfieldswithghosttext;

import org.blbulyandavbulyan.client.ui.ghosttextt.GhostText;
import org.blbulyandavbulyan.client.ui.ghosttextt.GhostTextInterface;
import org.blbulyandavbulyan.client.ui.ghosttextt.SpecificShowUnshowGhostTextActions;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;

public class JPasswordFieldWithGhostText extends JPasswordField implements GhostTextInterface {
    private final GhostText ghostText;

    private char echoChar;

    private class PasswordFieldSpeciefiecShowUnshowActions implements SpecificShowUnshowGhostTextActions {
        Callable<Boolean> isShowGetter;
        public PasswordFieldSpeciefiecShowUnshowActions(Callable<Boolean> isShowGetter){
            this.isShowGetter = isShowGetter;
        }
        @Override
        public void focusLost() {
            setEchoChar((char) 0);
        }

        @Override
        public void focusGained() {
            if(isShowGetter != null) {
                try {
                    setEchoChar(isShowGetter.call() ? (char)0 : echoChar);
                } catch (Exception e) {
                    setEchoChar(echoChar);
                    throw new RuntimeException(e);
                }
            }
            else setEchoChar(echoChar);
        }
    }
    PasswordFieldSpeciefiecShowUnshowActions passwordFieldSpeciefiecShowUnshowActions;
    public JPasswordFieldWithGhostText(String ghostText, Callable<Boolean> isShowGetter){
        this.echoChar = this.getEchoChar();
        passwordFieldSpeciefiecShowUnshowActions = new PasswordFieldSpeciefiecShowUnshowActions(isShowGetter);

        this.ghostText = new GhostText(this, ghostText,passwordFieldSpeciefiecShowUnshowActions, super::setText);

        this.setEchoChar((char)0);
    }
    public JPasswordFieldWithGhostText(String ghostText){
        this(ghostText, null);
    }
    public void clear(){
        ghostText.clear();
    }
    @Override public void setText(String text){
        if(ghostText != null){
            if(text == null || text.isEmpty())ghostText.clear();
            else ghostText.setText(text);
        }
        else super.setText(text);
    }
    @Override public void setEchoChar(char echoChar){
        if(echoChar != 0)this.echoChar = echoChar;
        super.setEchoChar(echoChar);
    }
    @Override public char getEchoChar(){
        return echoChar;
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

    public void setIsShowGetter(Callable<Boolean> isShowGetter) {
        passwordFieldSpeciefiecShowUnshowActions.isShowGetter = isShowGetter;
    }
}
