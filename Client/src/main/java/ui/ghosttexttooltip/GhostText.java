package ui.ghosttexttooltip;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GhostText implements FocusListener, DocumentListener, PropertyChangeListener
{
    private final JTextComponent textComp;
    private boolean isEmpty;
    private boolean deleted= false;
    private Color ghostColor;
    private Color foregroundColor;
    private final String ghostText;
    private final SpecificShowUnshowGhostTextActions specificShowUnshowGhostTextActions;
    public GhostText(final JTextComponent textComp, String ghostText)
    {
        super();
        this.textComp = textComp;
        this.ghostText = ghostText;
        this.ghostColor = Color.LIGHT_GRAY;
        textComp.addFocusListener(this);
        registerListeners();
        updateState();
        if (!this.textComp.hasFocus())
        {
            focusLost(null);
        }
        specificShowUnshowGhostTextActions = null;
    }
    public GhostText(final  JTextComponent textComp, String ghostText, SpecificShowUnshowGhostTextActions specificShowUnshowGhostTextActions){
        super();
        this.textComp = textComp;
        this.ghostText = ghostText;
        this.ghostColor = Color.LIGHT_GRAY;
        textComp.addFocusListener(this);
        registerListeners();
        updateState();
        if (!this.textComp.hasFocus())
        {
            focusLost(null);
        }
        this.specificShowUnshowGhostTextActions = specificShowUnshowGhostTextActions;
    }
    public void delete()
    {
        deleted = true;
        unregisterListeners();
        textComp.removeFocusListener(this);
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
        if(!deleted){
            showGhostText();
            isEmpty = true;
        }
        else textComp.setText("");
    }
    public Color getGhostColor()
    {
        return ghostColor;
    }

    public void setGhostColor(Color ghostColor)
    {
        this.ghostColor = ghostColor;
    }
    private void updateState()
    {
        isEmpty = textComp.getText().length() == 0;
        foregroundColor = textComp.getForeground();

    }
    private void showGhostText(){
        unregisterListeners();
        try
        {
            textComp.setText(ghostText);
            if(specificShowUnshowGhostTextActions != null) specificShowUnshowGhostTextActions.focusLost();
            textComp.setForeground(ghostColor);
        }
        finally
        {
            registerListeners();
        }
    }
    private void unshowGhostText(){
        unregisterListeners();
        try
        {
            textComp.setText("");
            if(specificShowUnshowGhostTextActions != null) specificShowUnshowGhostTextActions.focusGained();
            textComp.setForeground(foregroundColor);
        }
        finally
        {
            registerListeners();
        }
    }
    @Override
    public void focusGained(FocusEvent e)
    {
        if(isEmpty)unshowGhostText();
    }

    @Override
    public void focusLost(FocusEvent e)
    {
        if(isEmpty)showGhostText();
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

}
