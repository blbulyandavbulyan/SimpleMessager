package ui.ghosttextt;

import ui.ghosttextt.exceptions.GhostTextIsNullException;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.Consumer;

public class GhostText implements FocusListener, DocumentListener, PropertyChangeListener, GhostTextInterface
{
    private final JTextComponent textComp;
    private boolean isEmpty;
    private boolean enabled = true;
    private Color ghostColor;
    private final Color foregroundColor;
    private String ghostText;
    private SpecificShowUnshowGhostTextActions specificShowUnshowGhostTextActions;
    private final Consumer<String> specialSetText;
    public GhostText(final JTextComponent textComp, String ghostText, SpecificShowUnshowGhostTextActions specificShowUnshowGhostTextActions, Consumer<String> specialSetText)
    {
        super();
        this.textComp = textComp;
        this.ghostText = ghostText;
        this.ghostColor = Color.LIGHT_GRAY;
        this.foregroundColor = textComp.getForeground();
        this.specificShowUnshowGhostTextActions = specificShowUnshowGhostTextActions;
        this.specialSetText = specialSetText;
        if(ghostText == null)enabled = false;

        else{
            textComp.addFocusListener(this);
            registerListeners();
            updateState();
            if (!this.textComp.hasFocus())
            {
                focusLost(null);
            }
        }

    }

    public GhostText(final JTextComponent textComp, String ghostText, Consumer<String> specialSetText)
    {
        this(textComp, ghostText, null, specialSetText);

    }
    public void delete()
    {
        enabled = true;
        unregisterListeners();
        textComp.removeFocusListener(this);
    }
    public void setText(String text){
        //if(text == null || text.isEmpty())text = ghostText;
        if(text != null && text.equals(ghostText))textComp.setForeground(ghostColor);
        else textComp.setForeground(foregroundColor);
        if(specialSetText != null)specialSetText.accept(text);
        else textComp.setText(text);
    }
    private void registerListeners()
    {
        textComp.getDocument().addDocumentListener(this);
        textComp.addPropertyChangeListener("foreground", this);

    }

    private void unregisterListeners()
    {
        textComp.getDocument().removeDocumentListener(this);
        textComp.removePropertyChangeListener("foreground", this);
    }
    public void clear(){
        if(enabled){
            isEmpty = true;
            if(!textComp.hasFocus())showGhostText();
            else setText("");
        }
        else setText("");
    }
    public void updateState()
    {
        isEmpty = textComp.getText().length() == 0;


    }
    private void showGhostText(){
        if(isEmpty){
            unregisterListeners();
            try {
                setText(ghostText);
                if (specificShowUnshowGhostTextActions != null) specificShowUnshowGhostTextActions.focusLost();
            } finally {
                registerListeners();
            }
        }
    }
    private void unshowGhostText(){
        if(isEmpty){
            unregisterListeners();
            try {
                setText("");
                if (specificShowUnshowGhostTextActions != null) specificShowUnshowGhostTextActions.focusGained();
            } finally {
                registerListeners();
            }
        }
    }
    @Override
    public void focusGained(FocusEvent e)
    {
        unshowGhostText();
    }

    @Override
    public void focusLost(FocusEvent e)
    {
        showGhostText();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        updateState();
    }

    @Override
    public void changedUpdate(DocumentEvent e)
    {
        updateState();
    }

    @Override
    public void insertUpdate(DocumentEvent e)
    {
        updateState();
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
        updateState();
    }


    @Override
    public void setGhostText(String ghostText) {
        this.ghostText = ghostText;
        showGhostText();
    }

    @Override
    public String getGhostText() {
        return ghostText;
    }

    @Override
    public void setGhostTextEnabled(boolean enabled) throws GhostTextIsNullException{

        if(enabled && ghostText == null)throw new GhostTextIsNullException();

        if(enabled){
            if(!this.enabled)textComp.addFocusListener(this);
            showGhostText();
        }
        else{
            unregisterListeners();
            if(isEmpty)setText("");
            textComp.removeFocusListener(this);
        }
        this.enabled = enabled;
    }

    @Override
    public boolean isGhostTextEnabled() {
        return enabled;
    }

    @Override
    public void setGhostTextColor(Color ghostTextColor) {
        this.ghostColor = ghostTextColor;
    }

    @Override
    public Color getGhostTextColor() {
        return ghostColor;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

    public void setSpecificShowUnshowGhostTextActions(SpecificShowUnshowGhostTextActions specificShowUnshowGhostTextActions) {
        this.specificShowUnshowGhostTextActions = specificShowUnshowGhostTextActions;
    }
}
