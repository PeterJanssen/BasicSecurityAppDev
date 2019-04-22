package be.pxl.basicSecurity.appDev.crypto;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Controller {


    @FXML
    public void OpenHomePage(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/StartScreen.fxml"));
        closeStageOfNode((Node) mouseEvent.getSource());
        setSceneAndRoot(root, "CryptoApp");
    }

    @FXML
    public void OpenDecryptionPage(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/DecryptionScreen.fxml"));
        closeStageOfNode((Node) mouseEvent.getSource());
        setSceneAndRoot(root, "Decryption Form");
    }

    @FXML
    public void OpenEncryptionKeyGenerationPage(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/EncryptionKeyGenScreen.fxml"));
        closeStageOfNode((Node) mouseEvent.getSource());
        setSceneAndRoot(root, "EncryptionKey Form");
    }

    @FXML
    public void OpenEncryptionMessagePage(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/EncryptionMsgScreen.fxml"));
        closeStageOfNode((Node) mouseEvent.getSource());
        setSceneAndRoot(root, "EncryptionMessage Form");
    }

    private void setSceneAndRoot(Parent parent, String title) {
        Scene scene = new Scene(parent, 1200, 600);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle(title);
        stage.setScene(scene);

        stage.show();
    }

    private void closeStageOfNode(Node node) {
        ((Stage) (node).getScene().getWindow()).close();
    }

}
