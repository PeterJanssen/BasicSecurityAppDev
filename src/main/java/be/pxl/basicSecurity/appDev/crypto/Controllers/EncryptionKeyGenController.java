package be.pxl.basicSecurity.appDev.crypto.Controllers;

import be.pxl.basicSecurity.appDev.crypto.FileCollector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EncryptionKeyGenController {
    @FXML
    private TextField textFieldPublicA;
    @FXML
    private TextField textFieldPrivateA;
    @FXML
    private TextField textFieldPublicB;
    @FXML
    private TextField textFieldPrivateB;

    private FileCollector fileCollector;

    EncryptionKeyGenController(FileCollector fileCollector) {
        this.fileCollector = fileCollector;
    }

    public void SavePrivateA(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        fileCollector.setFilePrivateA(fileCollector.ShowSaveDialog(stage, textFieldPrivateA));
    }

    public void SavePublicA(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        fileCollector.setFilePublicA(fileCollector.ShowSaveDialog(stage, textFieldPublicA));
    }

    public void SavePrivateB(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        fileCollector.setFilePrivateB(fileCollector.ShowSaveDialog(stage, textFieldPrivateB));
    }

    public void SavePublicB(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        fileCollector.setFilePublicB(fileCollector.ShowSaveDialog(stage, textFieldPublicB));
    }

    public void GenerateKeys(ActionEvent actionEvent) {

    }

    private Stage getStage(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        return (Stage) (node).getScene().getWindow();
    }
}
