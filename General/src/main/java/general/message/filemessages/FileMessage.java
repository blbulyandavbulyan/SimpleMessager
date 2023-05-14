package general.message.filemessages;

import general.message.Message;
import general.message.filemessages.exceptions.FileHasInvalidFormatException;
import general.message.filemessages.exceptions.FileMessageCreatingException;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.function.Predicate;

public abstract class FileMessage extends Message {
    @Serial
    private static final long serialVersionUID = -4876801809226719046L;
    protected final String fileName;
    protected final String MIMEType;
    protected final byte[] fileData;
    protected FileMessage(String sender, String receiver, File file) throws IOException, FileMessageCreatingException {
        super(sender, receiver);
        this.fileName = file.getName();
        this.MIMEType = Files.probeContentType(file.toPath());
        this.fileData = Files.readAllBytes(file.toPath());
    }

    public String getFileName() {
        return fileName;
    }
    public String getMIMEType() {
        return MIMEType;
    }
    public byte[] getFileData() {
        return fileData;
    }
}
