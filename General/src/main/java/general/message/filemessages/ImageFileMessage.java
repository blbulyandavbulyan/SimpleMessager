package general.message.filemessages;

import general.message.filemessages.FileMessage;
import general.message.filemessages.exceptions.FileMessageCreatingException;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.Serial;

public class ImageFileMessage extends FileMessage {
    @Serial
    private static final long serialVersionUID = -8488066645843643537L;

    public ImageFileMessage(String sender, String receiver, File imageFile) throws IOException, FileMessageCreatingException {
        super(sender, receiver, imageFile, (mimeType)-> mimeType.equals("image/png") || mimeType.equals("image/jpeg"));
    }
    public ImageIcon getImageIcon(){
        return new ImageIcon(fileData);
    }
}
