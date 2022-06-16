package ui.common;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class DisplayErrors {
    public static void showErrorMessage(Component parent, String messageKey, String captionKey, ResourceBundle rb){
        JOptionPane.showMessageDialog(parent, rb.getString(messageKey), rb.getString(captionKey), JOptionPane.ERROR_MESSAGE);
    }
}
