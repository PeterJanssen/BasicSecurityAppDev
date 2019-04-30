package be.pxl.basicSecurity.appDev.crypto;

import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

import java.io.File;

import static be.pxl.basicSecurity.appDev.crypto.FileCollector.*;

public class FileChecker {
    public static boolean CheckIfFileDoesntExist(File file) {
        return file == null || !file.exists();
    }

    public static void ExceptionOccurred(FileCollector fileCollector, ImageView imageView, String message, TextArea textAreaOutput, Exception e) {
        fileCollector.SetImageView(fileCollector.getCrossRemoveSign(), imageView);
        if (message != null) {
            textAreaOutput.appendText(message);
        }
        if (e.getMessage() != null) {
            textAreaOutput.appendText(e.getMessage());
        }
    }

    static void CreateFoldersIfTheyAreDeleted() {
        new File(getDirectoryThatContainsAllTheNecessaryFilesForUser()).mkdirs();
        new File(getDirectoryThatContainsAllTheNecessaryFilesOnTheInternet()).mkdirs();
        new File(getDirectoryThatContainsAllTheInputFiles()).mkdirs();
        new File(getDirectoryThatContainsAllTheOutputFiles()).mkdirs();
    }
}
