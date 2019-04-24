package be.pxl.basicSecurity.appDev.crypto.Controllers;

import be.pxl.basicSecurity.appDev.crypto.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

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
    @FXML
    private TextField textFieldInitVector;
    @FXML
    private TextArea textAreaMessage;
    @FXML
    private ImageView imageView;

    private FileCollector fileCollector;

    DecryptionController(FileCollector fileCollector) {
        this.fileCollector = fileCollector;
    }

    public void StartDecrypting(ActionEvent actionEvent) throws Exception {
        try {
            //Empty textArea and textFields
            textAreaMessage.setText("");
            textFieldFile1.setText("");
            textFieldFile2.setText("");
            textFieldFile3.setText("");
            textFieldPublicA.setText("");
            textFieldPrivateB.setText("");
            textFieldInitVector.setText("");

            Stage stage = getStage(actionEvent);

            //Get Save location of decrypted message and decrypted hash
            fileCollector.setFileDecMessage(fileCollector.ShowSaveDialog(stage));

            fileCollector.setFileDecHash(new File(fileCollector.getFileDecMessage().getParent() + "/decHash.txt"));
            String directoryThatContainsAllTheNecessaryFilesForDecrypting = System.getProperty("user.dir") + "\\files for decryption";

            //Get all the necessary files for decrypting
            fileCollector.setFileOne(new File(directoryThatContainsAllTheNecessaryFilesForDecrypting + "/encFile.txt"));
            fileCollector.setFileTwo(new File(directoryThatContainsAllTheNecessaryFilesForDecrypting + "/encAesKey.txt"));
            fileCollector.setFileThree(new File(directoryThatContainsAllTheNecessaryFilesForDecrypting + "/encHash.txt"));
            fileCollector.setFilePublicA(new File(directoryThatContainsAllTheNecessaryFilesForDecrypting + "/rsaEncPub.txt"));
            fileCollector.setFilePrivateB(new File(directoryThatContainsAllTheNecessaryFilesForDecrypting + "/rsaDecPriv.txt"));
            fileCollector.setFileInitVector(new File(directoryThatContainsAllTheNecessaryFilesForDecrypting + "/iv.txt"));

            //Set all the paths in the textboxes
            textFieldFile1.setText(fileCollector.getFileOne().toPath().toString());
            textFieldFile2.setText(fileCollector.getFileTwo().toPath().toString());
            textFieldFile3.setText(fileCollector.getFileThree().toPath().toString());
            textFieldPublicA.setText(fileCollector.getFilePublicA().toPath().toString());
            textFieldPrivateB.setText(fileCollector.getFilePrivateB().toPath().toString());
            textFieldInitVector.setText(fileCollector.getFileInitVector().toPath().toString());

            //Ensure that all files have been selected.
            if (!EnsureThatEveryFileHasBeenSelected()) {
                SetImageView("@../../images/cross-remove-sign.png");
                return;
            }

            if (fileCollector.getFileDecMessage() == null || fileCollector.getFileDecHash() == null) {
                textAreaMessage.setText("Please select a location to save your message and hash.");
                SetImageView("@../../images/cross-remove-sign.png");
                return;
            }

            //Get keys
            PrivateKey privateKey = CreatePrivateKeyFromFile();
            PublicKey publicKey = CreatePublicKeyFromFile();

            //Decrypt AESKey
            Key AESKey = RSAEncryption.decryptSymmetricKeyRSA(privateKey, fileCollector.getFileTwo().toPath(), KeySize.SIZE_2048, UsableAlgorithm.AES);

            //Decrypt message and write to file
            AESEncryption.decryptAES(AESKey, IOCrypto.readInitVectorFromFile(
                    fileCollector.getFileInitVector().toPath()),
                    fileCollector.getFileOne().toPath(),
                    fileCollector.getFileDecMessage().toPath());

            //Write decrypted message to textArea
            WriteMessageAndHashOfDecodedMessageToTextArea();
            //Write hash to file
            Path pathToDecryptedHash = fileCollector.getFileDecHash().toPath();
            RSAEncryption.decryptHashFileRSA(publicKey, fileCollector.getFileThree().toPath(), pathToDecryptedHash);

            //Get SHA256 hash of original message
            StringBuilder hashOfOriginalMessage = WriteMessageFromFileToTextArea(pathToDecryptedHash);
            textAreaMessage.appendText(System.lineSeparator() + "Hash of original message" + System.lineSeparator() + hashOfOriginalMessage);
            SetImageView("@../../images/tick-sign.png");
        } catch (Exception e) {
            textAreaMessage.setText("Please ensure that all the right files have been selected" + System.lineSeparator());

            if (e.getMessage() != null) {
                textAreaMessage.appendText(e.getMessage());
            }
            SetImageView("@../../images/cross-remove-sign.png");
        }
    }

    private void WriteMessageAndHashOfDecodedMessageToTextArea() throws Exception {
        Path pathToDecryptedMessage = fileCollector.getFileDecMessage().toPath();
        textAreaMessage.appendText(String.valueOf(WriteMessageFromFileToTextArea(pathToDecryptedMessage)));

        textAreaMessage.appendText("End of message" + System.lineSeparator() + "------------------------" + System.lineSeparator());
        //Get SHA256 hash of decoded message
        textAreaMessage.appendText("Hash of this message" + System.lineSeparator() + HashSHA256.generateHash(pathToDecryptedMessage));

    }

    private PrivateKey CreatePrivateKeyFromFile() throws Exception {
        byte[] keyBytes = Files.readAllBytes(fileCollector.getFilePrivateB().toPath());
        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    private PublicKey CreatePublicKeyFromFile() throws Exception {
        byte[] keyBytes = Files.readAllBytes(fileCollector.getFilePublicA().toPath());
        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    private StringBuilder WriteMessageFromFileToTextArea(Path fileLocation) throws Exception {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(fileLocation)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
            return sb;
        }
    }

    private void SetImageView(String uri) {
        imageView.setImage(new Image(uri));
    }

    private boolean EnsureThatEveryFileHasBeenSelected() {
        if (fileCollector.getFileInitVector() == null || !fileCollector.getFileInitVector().exists()) {
            textAreaMessage.appendText("Please select a location for the file that contains your init vector file." + System.lineSeparator());
        }

        if (fileCollector.getFileOne() == null || !fileCollector.getFileInitVector().exists()) {
            textAreaMessage.appendText("Please select a location for the file that contains encrypted message. This is file 1." + System.lineSeparator());
        }

        if (fileCollector.getFileTwo() == null || !fileCollector.getFileTwo().exists()) {
            textAreaMessage.appendText("Please select a location for the file that contains encrypted AES key. This is file 2." + System.lineSeparator());
        }

        if (fileCollector.getFileThree() == null || !fileCollector.getFileThree().exists()) {
            textAreaMessage.appendText("Please select a location for the file that contains encrypted hash. This is file 3." + System.lineSeparator());
        }

        if (fileCollector.getFilePublicA() == null || !fileCollector.getFilePublicA().exists()) {
            textAreaMessage.appendText("Please select a location for the file that contains public key of the sender. This is public A." + System.lineSeparator());
        }

        if (fileCollector.getFilePrivateB() == null || !fileCollector.getFilePrivateB().exists()) {
            textAreaMessage.appendText("Please select a location for the file that contains the private key of the receiver. This is private B." + System.lineSeparator());
        }

        return !textAreaMessage.getText().contains("Please");
    }

    private Stage getStage(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        return (Stage) (node).getScene().getWindow();
    }

}
