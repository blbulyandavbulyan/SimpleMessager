package general.message.filemessages;

import general.message.filemessages.exceptions.FileHasInvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.HashMap;

public class FileMessageFactory {
    private static final java.util.Map<String, Class<? extends FileMessage>> mimeToClassMap;
    static {
        mimeToClassMap = new HashMap<>();
        mimeToClassMap.put("image/jpeg", ImageFileMessage.class);
        mimeToClassMap.put("image/png", ImageFileMessage.class);
    }
    public static FileMessage create(String sender, String receiver, File file) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String fileMIMEType = Files.probeContentType(file.toPath());
        Class<? extends FileMessage> fileMessageClass = mimeToClassMap.get(fileMIMEType);
        if(fileMessageClass != null){
            return fileMessageClass.getDeclaredConstructor(String.class, String.class, File.class)
                    .newInstance(sender, receiver, file);
        }
        else throw new FileHasInvalidFormatException();
    }
}
