package org.blbulyandavbulyan.smgeneral.message.filemessages;

import org.blbulyandavbulyan.smgeneral.message.filemessages.exceptions.FileMessageCreatingException;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.Serial;

public class ImageFileMessage extends FileMessage {
    @Serial
    private static final long serialVersionUID = -8488066645843643537L;

    ImageFileMessage(String sender, String receiver, File imageFile) throws IOException, FileMessageCreatingException {
        super(sender, receiver, imageFile);
    }
    public ImageIcon getImageIcon(){
        return new ImageIcon(fileData);
    }
}
