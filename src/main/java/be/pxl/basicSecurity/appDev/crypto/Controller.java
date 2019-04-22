package be.pxl.basicSecurity.appDev.crypto;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Controller {

    @FXML
    public void OpenHomePage() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/StartScreen.fxml"));
        setSceneAndRoot(root, "CryptoApp");
    }

    @FXML
    public void OpenDecryptionPage() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/DecryptionScreen.fxml"));
        setSceneAndRoot(root, "Decryption Form");
    }

    @FXML
    public void OpenEncryptionKeyGenerationPage() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/EncryptionKeyGenScreen.fxml"));
        setSceneAndRoot(root, "EncryptionKey Form");
    }

    @FXML
    public void OpenEncryptionMessagePage() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/EncryptionMsgScreen.fxml"));
        setSceneAndRoot(root, "EncryptionMessage Form");
    }

    private void setSceneAndRoot(Parent parent, String title) {
        Scene scene = new Scene(parent, 1200, 600);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
