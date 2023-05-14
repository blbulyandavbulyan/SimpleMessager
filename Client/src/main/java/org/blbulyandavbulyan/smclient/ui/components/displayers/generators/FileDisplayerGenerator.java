package org.blbulyandavbulyan.smclient.ui.components.displayers.generators;

import org.blbulyandavbulyan.smclient.ui.components.displayers.filedisplayers.FileDisplayer;
import org.blbulyandavbulyan.smclient.ui.components.displayers.filedisplayers.ImageFileDisplayer;
import org.blbulyandavbulyan.smclient.ui.components.displayers.filedisplayers.TextFileDisplayer;
import org.blbulyandavbulyan.smclient.ui.components.displayers.filedisplayers.exceptions.InvalidMIMETypeException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class FileDisplayerGenerator {
//    private final ResourceBundle rb;
    private static final Map<String, Class<? extends FileDisplayer>> mimeToClassMap = new HashMap<>();
    static {
        mimeToClassMap.put("image/png", ImageFileDisplayer.class);
        mimeToClassMap.put("image/jpeg", ImageFileDisplayer.class);
        mimeToClassMap.put("text/plain", TextFileDisplayer.class);
    }

    public static boolean isThisFileHasValidFormat(File file) throws IOException {
        String fileTypeMIME = Files.probeContentType(file.toPath());
        return mimeToClassMap.containsKey(fileTypeMIME);
    }
    public static FileDisplayer getFileDsiplayer(File file) throws IOException, InvalidMIMETypeException {

        String fileTypeMIME = Files.probeContentType(file.toPath());
        if(mimeToClassMap.containsKey(fileTypeMIME)){
            Class<? extends FileDisplayer> fileDisplayerClass = mimeToClassMap.get(fileTypeMIME);
            try {
                Constructor<? extends FileDisplayer> fileDisplayerConstructor = fileDisplayerClass.getConstructor(File.class);
                return fileDisplayerConstructor.newInstance(file);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        else throw new InvalidMIMETypeException();
    }
}
