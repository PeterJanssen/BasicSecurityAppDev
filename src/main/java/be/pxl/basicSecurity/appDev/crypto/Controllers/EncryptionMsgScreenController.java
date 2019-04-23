package be.pxl.basicSecurity.appDev.crypto.Controllers;

import be.pxl.basicSecurity.appDev.crypto.FileCollector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;

public class EncryptionMsgScreenController {
    @FXML
    private TextField textFieldFile1;
    @FXML
    private TextField textFieldFile2;
    @FXML
    private TextField textFieldFile3;
    @FXML
    private TextArea textAreaMessage;

    private FileCollector fileCollector;

    EncryptionMsgScreenController(FileCollector fileCollector) {
        this.fileCollector = fileCollector;
    }

    public void SaveFile1(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        fileCollector.setFilePrivateA(fileCollector.ShowSaveDialog(stage, textFieldFile2));
    }

    public void SaveFile2(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        fileCollector.setFilePublicA(fileCollector.ShowSaveDialog(stage, textFieldFile1));
    }

    public void SaveFile3(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        fileCollector.setFilePublicB(fileCollector.ShowSaveDialog(stage, textFieldFile3));
    }

    public void SaveMessage(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        fileCollector.setFilePrivateB(fileCollector.ShowSaveDialog(stage, textAreaMessage));
    }

    public void StartEncrypting(ActionEvent actionEvent) {

    }

    private Stage getStage(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        return (Stage) (node).getScene().getWindow();
    }
}
