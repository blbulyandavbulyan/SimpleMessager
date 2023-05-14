package org.blbulyandavbulyan.smgeneral.message.filemessages;

import org.blbulyandavbulyan.smgeneral.message.Message;
import org.blbulyandavbulyan.smgeneral.message.filemessages.exceptions.FileMessageCreatingException;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.nio.file.Files;

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
