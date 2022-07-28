package general.message.filemessages.imagefilesmessages;

import general.message.filemessages.FileMessage;
import general.message.filemessages.exceptions.FileMessageCreatingException;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.Serial;

public class ImageFileMessage extends FileMessage {
    @Serial
    private static final long serialVersionUID = -8488066645843643537L;

    protected ImageFileMessage(String sender, String receiver, File file) throws IOException, FileMessageCreatingException {
        super(sender, receiver, file, (mimeType)-> mimeType.equals("image/png") || mimeType.equals("image/jpeg"));
    }
    public ImageIcon getImageIcon(){
        return new ImageIcon(fileData);
    }
}
