package be.pxl.basicSecurity.appDev.crypto.Controllers;

import be.pxl.basicSecurity.appDev.crypto.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class EncryptionKeyGenController {
    @FXML
    private TextField textFieldPublicA;
    @FXML
    private TextField textFieldPrivateA;
    @FXML
    private TextField textFieldPublicB;
    @FXML
    private TextField textFieldPrivateB;
    @FXML
    private TextArea textAreaMessage;
    @FXML
    private ImageView imageView;
    private FileCollector fileCollector;

    EncryptionKeyGenController(FileCollector fileCollector) {
        this.fileCollector = fileCollector;
    }

    public void GenerateKeys(ActionEvent actionEvent) {
        try {
            //Empty textfields and textAreas and set all the paths in the textboxes
            ReadyTheWindow();
            //Creating keys
            KeyPair kpEnc = RSAEncryption.getKeyPair(KeySize.SIZE_2048);
            KeyPair kpDec = RSAEncryption.getKeyPair(KeySize.SIZE_2048);
            PublicKey publicKeyEnc = kpEnc.getPublic();
            PrivateKey privateKeyEnc = kpEnc.getPrivate();
            PublicKey publicKeyDec = kpDec.getPublic();
            PrivateKey privateKeyDec = kpDec.getPrivate();

            IOCrypto.writeKeyToFile(privateKeyEnc, fileCollector.getFilePrivateA().toPath());
            IOCrypto.writeKeyToFile(publicKeyEnc, fileCollector.getFilePublicA().toPath());
            IOCrypto.writeKeyToFile(publicKeyDec, fileCollector.getFilePublicB().toPath());
            IOCrypto.writeKeyToFile(privateKeyDec, fileCollector.getFilePrivateB().toPath());

            //Ensure that all files have been created and saved.
            if (!EnsureThatEveryFileHasBeenSaved()) {
                fileCollector.SetImageView(fileCollector.getCrossRemoveSign(), imageView);
                return;
            }
            //Set imageview to ticksign so the user knows everything worked
            textAreaMessage.setText("Keys generated and saved successfully.");
            SetPathsToTextFields();
            fileCollector.SetImageView(fileCollector.getTickSign(), imageView);
        } catch (IOException e) {
            FileChecker.ExceptionOccurred(fileCollector,imageView,"Keys not saved successfully."
                    + System.lineSeparator(),textAreaMessage,e);
        } catch (Exception e) {
            FileChecker.ExceptionOccurred(fileCollector,imageView,"Keys not generated successfully."
                    + System.lineSeparator(),textAreaMessage,e);
        }
    }

    private boolean EnsureThatEveryFileHasBeenSaved() {
        if (FileChecker.CheckIfFileDoesntExist(fileCollector.getFilePublicA())) {
            textAreaMessage.appendText("Please check this location for the file that contains public key of the sender." +
                    " This is public A." + System.lineSeparator());
        }

        if (FileChecker.CheckIfFileDoesntExist(fileCollector.getFilePrivateA())) {
            textAreaMessage.appendText("Please check this location for the file that contains private key of the sender." +
                    " This is private A." + System.lineSeparator());
        }

        if (FileChecker.CheckIfFileDoesntExist(fileCollector.getFilePublicB())) {
            textAreaMessage.appendText("Please check this location for the file that contains public key of the receiver." +
                    " This is public B." + System.lineSeparator());
        }

        if (FileChecker.CheckIfFileDoesntExist(fileCollector.getFilePrivateB())) {
            textAreaMessage.appendText("Please check this location for the file that contains the private key of the receiver." +
                    " This is private B." + System.lineSeparator());
        }

        return !textAreaMessage.getText().contains("Please");
    }

    private void ReadyTheWindow() {
        //Empty textArea and textFields
        textAreaMessage.setText("");
        textFieldPublicA.setText("");
        textFieldPrivateA.setText("");
        textFieldPublicB.setText("");
    }

    private void SetPathsToTextFields() {
        //Set all the paths in the textboxes
        textFieldPublicA.setText(fileCollector.getFilePublicA().toPath().toString());
        textFieldPrivateA.setText(fileCollector.getFilePrivateA().toPath().toString());
        textFieldPublicB.setText(fileCollector.getFilePublicB().toPath().toString());
        textFieldPrivateB.setText(fileCollector.getFilePrivateB().toPath().toString());
    }
}
