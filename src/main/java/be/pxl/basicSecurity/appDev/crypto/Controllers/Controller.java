package be.pxl.basicSecurity.appDev.crypto.Controllers;

import be.pxl.basicSecurity.appDev.crypto.FileCollector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class Controller {
    @FXML
    private Label hyperlinkLabel;
    private FileCollector fileCollector = new FileCollector();

    @FXML
    public void OpenHomePage(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/StartScreen.fxml"));
        CloseStageOfNode((Node) mouseEvent.getSource());
        SetSceneAndRoot(root, "CryptoApp");
    }

    @FXML
    public void OpenDecryptionPage(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Decryption/DecryptionScreen.fxml"));
        loader.setController(new DecryptionController(fileCollector));
        Parent root = loader.load();
        CloseStageOfNode((Node) mouseEvent.getSource());
        SetSceneAndRoot(root, "Decryption Form");
    }

    @FXML
    public void OpenEncryptionKeyGenerationPage(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Encryption/EncryptionKeyGenScreen.fxml"));
        loader.setController(new EncryptionKeyGenController(fileCollector));
        Parent root = loader.load();
        CloseStageOfNode((Node) mouseEvent.getSource());
        SetSceneAndRoot(root, "EncryptionKey Form");
    }

    @FXML
    public void OpenEncryptionMessagePage(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Encryption/EncryptionMsgScreen.fxml"));
        loader.setController(new EncryptionMsgScreenController(fileCollector));
        Parent root = loader.load();
        CloseStageOfNode((Node) mouseEvent.getSource());
        SetSceneAndRoot(root, "EncryptionMessage Form");
    }

    private void SetSceneAndRoot(Parent parent, String title) {
        Scene scene = new Scene(parent, 1200, 600);

        Stage stage = new Stage(StageStyle.DECORATED);

        stage.setTitle(title);
        stage.setScene(scene);

        stage.show();
    }

    private void CloseStageOfNode(Node node) {
        ((Stage) (node).getScene().getWindow()).close();
    }

    public void BrowseToWebsiteAuthorSymbols() {
        try {
            Desktop.getDesktop().browse(new URI("https://www.flaticon.com/authors/freepik"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
