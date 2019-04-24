package be.pxl.basicSecurity.appDev.crypto;

import javafx.scene.control.TextInputControl;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileCollector {
    private File fileDecMessage;
    private File fileDecHash;
    private File fileOne;
    private File fileTwo;
    private File fileThree;
    private File filePublicA;
    private File filePublicB;
    private File filePrivateB;
    private File filePrivateA;
    private File fileInitVector;

    public File ShowOpenDialog(Stage stage, TextInputControl textInputControl) {
        FileChooser fileChooser = CreateDialog();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            textInputControl.setText(file.getAbsolutePath());
            return file;
        }
        return null;
    }

    public File ShowSaveDialog(Stage stage, TextInputControl textInputControl) {
        FileChooser fileChooser = CreateDialog();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            textInputControl.setText(file.getAbsolutePath());
            return file;
        }
        return null;
    }

    public File ShowSaveDialog(Stage stage) {
        FileChooser fileChooser = CreateDialog();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            return file;
        }
        return null;
    }

    private FileChooser CreateDialog() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extensionFilter);
        return fileChooser;
    }

    public File getFileDecMessage() {
        return fileDecMessage;
    }

    public void setFileDecMessage(File fileDecMessage) {
        this.fileDecMessage = fileDecMessage;
    }

    public File getFileDecHash() {
        return fileDecHash;
    }

    public void setFileDecHash(File fileDecHash) {
        this.fileDecHash = fileDecHash;
    }

    public File getFileOne() {
        return fileOne;
    }

    public void setFileOne(File fileOne) {
        this.fileOne = fileOne;
    }

    public File getFileTwo() {
        return fileTwo;
    }

    public void setFileTwo(File fileTwo) {
        this.fileTwo = fileTwo;
    }

    public File getFileThree() {
        return fileThree;
    }

    public void setFileThree(File fileThree) {
        this.fileThree = fileThree;
    }

    public File getFilePublicA() {
        return filePublicA;
    }

    public void setFilePublicA(File filePublicA) {
        this.filePublicA = filePublicA;
    }

    public File getFilePublicB() {
        return filePublicB;
    }

    public void setFilePublicB(File filePublicB) {
        this.filePublicB = filePublicB;
    }

    public File getFilePrivateB() {
        return filePrivateB;
    }

    public void setFilePrivateB(File filePrivateB) {
        this.filePrivateB = filePrivateB;
    }

    public File getFilePrivateA() {
        return filePrivateA;
    }

    public void setFilePrivateA(File filePrivateA) {
        this.filePrivateA = filePrivateA;
    }

    public File getFileInitVector() {
        return fileInitVector;
    }

    public void setFileInitVector(File fileInitVector) {
        this.fileInitVector = fileInitVector;
    }
}
