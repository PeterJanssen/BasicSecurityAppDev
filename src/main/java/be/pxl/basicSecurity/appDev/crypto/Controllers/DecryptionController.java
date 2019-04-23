package be.pxl.basicSecurity.appDev.crypto.Controllers;

import be.pxl.basicSecurity.appDev.crypto.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class DecryptionController {
    @FXML
    private TextField textFieldFile1;
    @FXML
    private TextField textFieldFile2;
    @FXML
    private TextField textFieldFile3;
    @FXML
    private TextField textFieldPublicA;
    @FXML
    private TextField textFieldPrivateB;

    private FileCollector fileCollector;

    DecryptionController(FileCollector fileCollector) {
        this.fileCollector = fileCollector;
    }

    public void OpenFileDialogFile1(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        fileCollector.setFileOne(fileCollector.ShowOpenDialog(stage, textFieldFile1));
    }

    public void OpenFileDialogFile2(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        fileCollector.setFileTwo(fileCollector.ShowOpenDialog(stage, textFieldFile2));
    }

    public void OpenFileDialogFile3(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        fileCollector.setFileThree(fileCollector.ShowOpenDialog(stage, textFieldFile3));
    }

    public void OpenFileDialogPublicA(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        fileCollector.setFilePublicA(fileCollector.ShowOpenDialog(stage, textFieldPublicA));
    }

    public void OpenFileDialogPrivateB(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        fileCollector.setFilePrivateB(fileCollector.ShowOpenDialog(stage, textFieldPrivateB));
    }

    public void StartDecrypting(ActionEvent actionEvent) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(fileCollector.getFilePrivateB().getAbsolutePath()));

        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = kf.generatePrivate(spec);
        System.out.print(privateKey.toString());
    }

    private Stage getStage(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        return (Stage) (node).getScene().getWindow();
    }

}
