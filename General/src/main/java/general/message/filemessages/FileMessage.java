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
    private static final java.util.Map<String, Class<? extends FileMessage>> mimeToClassMap;
    static {
        mimeToClassMap = new HashMap<>();
        mimeToClassMap.put("image/jpeg", ImageFileMessage.class);
        mimeToClassMap.put("image/png", ImageFileMessage.class);

    }
    protected final String fileName;
    protected final String MIMEType;
    protected final byte[] fileData;
    protected FileMessage(String sender, String receiver, File file, Predicate<String> isThisFileValidFormat) throws IOException, FileMessageCreatingException {
        super(sender, receiver);;
        if(!isThisFileValidFormat.test(MIMEType = Files.probeContentType(file.toPath())))throw new FileHasInvalidFormatException();
        this.fileName = file.getName();
        this.fileData = Files.readAllBytes(file.toPath());
    }
    protected FileMessage(String sender, String receiver, String fileName, String MIMEType, byte[] fileData){
        super(sender, receiver);
        this.fileName = fileName;
        this.MIMEType = MIMEType;
        this.fileData = fileData;
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
    public static FileMessage create(String sender, String receiver, File file) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String fileMIMEType = Files.probeContentType(file.toPath());
        Class<? extends FileMessage> fileMessageClass = mimeToClassMap.get(fileMIMEType);
        if(fileMessageClass != null){
            return fileMessageClass.getConstructor(String.class, String.class, File.class)
                    .newInstance(sender, receiver, file);
        }
        else throw new FileHasInvalidFormatException();
    }
}
