package be.pxl.basicSecurity.appDev.crypto;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class FileCollector {
    private static final String directoryThatContainsAllTheNecessaryFilesForUser =
            System.getProperty("user.dir") + "\\files on the user's end";
    private static final String directoryThatContainsAllTheNecessaryFilesOnTheInternet =
            System.getProperty("user.dir") + "\\files on the internet";
    private static final String directoryThatContainsAllTheInputFiles =
            System.getProperty("user.dir") + "\\Sender files";
    private static final String directoryThatContainsAllTheOutputFiles =
            System.getProperty("user.dir") + "\\Receiver files";

    private File fileDecMessage;
    private File fileDecHash;

    private File encryptedMessageFile;
    private File encryptedAesKeyFileForMessage;
    private File encryptedAesKeyFileForImage;
    private File encryptedHashMessage;
    private File fileInitVector;
    private File fileInitVectorForImage;

    private File filePublicA;
    private File filePublicB;

    private File filePrivateA;
    private File filePrivateB;
    private File aesKeyForMessage;
    private File aesKeyForImage;

    private File imageForEncryption;
    private File encryptedHashImageMessage;

    public FileCollector() {
        AllocateFileLocationsToFilesForDecryption();
        AllocateFileLocationsToFilesForEncryption();
    }

    private void AllocateFileLocationsToFilesForEncryption() {
        filePublicB = new File(directoryThatContainsAllTheNecessaryFilesOnTheInternet + "/rsaDecPub.txt");
        filePrivateA = new File(directoryThatContainsAllTheNecessaryFilesForUser + "/rsaEncPriv.txt");
        aesKeyForMessage = new File(directoryThatContainsAllTheNecessaryFilesForUser + "/AesKeyForMessage.txt");
        aesKeyForImage = new File(directoryThatContainsAllTheNecessaryFilesForUser + "/AesKeyForImage.txt");
    }

    private void AllocateFileLocationsToFilesForDecryption() {
        encryptedMessageFile = new File(directoryThatContainsAllTheNecessaryFilesOnTheInternet + "/encFile.txt");
        encryptedAesKeyFileForMessage = new File(directoryThatContainsAllTheNecessaryFilesOnTheInternet + "/encAesKeyForMessage.txt");
        encryptedAesKeyFileForImage = new File(directoryThatContainsAllTheNecessaryFilesOnTheInternet + "/encAesKeyForImage.txt");
        encryptedHashMessage = new File(directoryThatContainsAllTheNecessaryFilesOnTheInternet + "/encHashForMessage.txt");
        encryptedHashImageMessage = new File(directoryThatContainsAllTheNecessaryFilesOnTheInternet + "/encHashForMessageInImage.txt");
        imageForEncryption = new File(directoryThatContainsAllTheNecessaryFilesOnTheInternet + "/image.png");
        filePublicA = new File(directoryThatContainsAllTheNecessaryFilesOnTheInternet + "/rsaEncPub.txt");
        filePrivateB = new File(directoryThatContainsAllTheNecessaryFilesForUser + "/rsaDecPriv.txt");
        fileInitVectorForImage = new File(directoryThatContainsAllTheNecessaryFilesOnTheInternet + "/ivForImage.txt");
        fileInitVector = new File(directoryThatContainsAllTheNecessaryFilesOnTheInternet + "/iv.txt");
    }

    public File ShowSaveDialog(Stage stage, String directory, String description, List<String> extensions) {
        FileChooser fileChooser = CreateDialog(directory, "Save your message", description, extensions);
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            return file;
        }
        return null;
    }

    public File ShowSelectDialog(Stage stage, String directory, String description, List<String> extensions) {
        FileChooser fileChooser = CreateDialog(directory, "Select a file", description, extensions);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            return file;
        }
        return null;
    }

    private FileChooser CreateDialog(String initialDirectory, String title, String description, List<String> extensions) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(description, extensions);
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File(initialDirectory));
        return fileChooser;
    }

    static String getDirectoryThatContainsAllTheNecessaryFilesForUser() {
        return directoryThatContainsAllTheNecessaryFilesForUser;
    }

    public static String getDirectoryThatContainsAllTheNecessaryFilesOnTheInternet() {
        return directoryThatContainsAllTheNecessaryFilesOnTheInternet;
    }

    public static String getDirectoryThatContainsAllTheInputFiles() {
        return directoryThatContainsAllTheInputFiles;
    }

    public static String getDirectoryThatContainsAllTheOutputFiles() {
        return directoryThatContainsAllTheOutputFiles;
    }

    public File getFileDecMessage() {
        return fileDecMessage;
    }

    public File getFileDecHash() {
        return fileDecHash;
    }

    public File getFileImage() {
        return imageForEncryption;
    }

    public void setFileDecMessage(File fileDecMessage) {
        this.fileDecMessage = fileDecMessage;
    }

    public void setFileDecHash(File fileDecHash) {
        this.fileDecHash = fileDecHash;
    }

    public File getEncryptedMessageFile() {
        return encryptedMessageFile;
    }

    public File getEncryptedAesKeyFileForMessage() {
        return encryptedAesKeyFileForMessage;
    }

    public File getEncryptedHashForMessage() {
        return encryptedHashMessage;
    }

    public File getEncryptedHashForImageMessage() {
        return encryptedHashImageMessage;
    }

    public File getFilePublicA() {
        return filePublicA;
    }

    public File getFilePublicB() {
        return filePublicB;
    }

    public File getFilePrivateB() {
        return filePrivateB;
    }

    public File getFilePrivateA() {
        return filePrivateA;
    }

    public File getFileInitVectorForMessage() {
        return fileInitVector;
    }

    public File getFileInitVectorForImage() {
        return fileInitVectorForImage;
    }

    public File getAesKeyForMessage() {
        return aesKeyForMessage;
    }

    public File getEncryptedAesKeyFileForImage() {
        return encryptedAesKeyFileForImage;
    }

    public File getAesKeyForImage() {
        return aesKeyForImage;
    }

    public String getCrossRemoveSign() {
        return "@../../images/cross-remove-sign.png";
    }

    public String getTickSign() {
        return "@../../images/tick-sign.png";
    }

    public void SetImageView(String uri, ImageView imageView) {
        imageView.setImage(new Image(uri));
    }
}
