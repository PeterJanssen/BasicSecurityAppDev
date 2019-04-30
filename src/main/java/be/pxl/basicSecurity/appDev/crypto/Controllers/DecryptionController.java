package be.pxl.basicSecurity.appDev.crypto.Controllers;

import be.pxl.basicSecurity.appDev.crypto.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.security.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static be.pxl.basicSecurity.appDev.crypto.FileCollector.getDirectoryThatContainsAllTheOutputFiles;

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
    private PrivateKey privateKey;
    private PublicKey publicKey;

    DecryptionController(FileCollector fileCollector) {
        this.fileCollector = fileCollector;
    }

    public void StartDecrypting(ActionEvent actionEvent) {
        try {
            //Empty textfields and textAreas and set all the paths in the textboxes
            ReadyTheWindow();

            //Get Save location of decrypted message and decrypted hash, hash is saved in the same folder
            OpenSaveDialog(actionEvent);

            //Ensure that all files have been selected.
            if (!EnsureThatEveryFileHasBeenSelected()) {
                fileCollector.SetImageView(fileCollector.getCrossRemoveSign(), imageView);
                return;
            }

            //Ensure that a valid save location has been selected
            if (fileCollector.getFileDecMessage() == null || fileCollector.getFileDecHash() == null) {
                textAreaMessage.setText("Please select a location to save your message and hash.");
                fileCollector.SetImageView(fileCollector.getCrossRemoveSign(), imageView);
                return;
            }

            //Get private key of receiver and public key of sender
            getKeys();

            //Decrypt AESKey message and write to file
            DecryptFiles();

            //Write decrypted message and hash of decrypted message to textArea
            WriteMessageAndHashOfDecodedMessageToTextArea();
            //Write hash of decrypted message to file
            RSAEncryption.decryptHashFileRSA(publicKey, fileCollector.getEncryptedHash().toPath(),
                    fileCollector.getFileDecHash().toPath());

            //Get SHA256 hash of original message
            textAreaMessage.appendText(System.lineSeparator() + "Hash of original message"
                    + System.lineSeparator() + IOCrypto.readFile(fileCollector.getFileDecHash().toPath()));


            //Check if hash of original message equals hash of received message
            if (IOCrypto.readHashFromFile(fileCollector.getFileDecHash().toPath()).equals(
                    HashSHA256.generateHash(fileCollector.getFileDecMessage().toPath()))) {
                textAreaMessage.appendText(System.lineSeparator()
                        + "Hashes of this message and original message are the same!" +
                        " This message can be trusted." + System.lineSeparator());
                //Set imageview to ticksign so the user knows everything worked
                fileCollector.SetImageView(fileCollector.getTickSign(), imageView);
                SetPathsToTextFields();
            } else {
                textAreaMessage.appendText(System.lineSeparator()
                        + "Hashes of this message and original message are NOT the same!" +
                        " This message CANNOT be trusted." + System.lineSeparator());
                fileCollector.SetImageView(fileCollector.getCrossRemoveSign(), imageView);
            }
        } catch (IOException e) {
            FileChecker.ExceptionOccurred(fileCollector, imageView, "Some of the files could not be found."
                    + System.lineSeparator(), textAreaMessage, e);
        } catch (NoSuchAlgorithmException e) {
            FileChecker.ExceptionOccurred(fileCollector, imageView, "Something went wrong when decrypting."
                    + System.lineSeparator(), textAreaMessage, e);
        } catch (InvalidKeyException e) {
            FileChecker.ExceptionOccurred(fileCollector, imageView, "An invalid key has been used for decryption."
                    + System.lineSeparator(), textAreaMessage, e);
        } catch (Exception e) {
            FileChecker.ExceptionOccurred(fileCollector, imageView, "Message was not decrypted successfully."
                    + System.lineSeparator(), textAreaMessage, e);
        }
    }

    private void DecryptFiles() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException,
            BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Key AESKey = RSAEncryption.decryptSymmetricKeyRSA(privateKey, fileCollector.getEncryptedAesKeyFile().toPath(),
                KeySize.SIZE_2048, UsableAlgorithm.AES);
        AESEncryption.decryptFileAES(AESKey, IOCrypto.readInitVectorFromFile(
                fileCollector.getFileInitVector().toPath()),
                fileCollector.getEncryptedMessageFile().toPath(),
                fileCollector.getFileDecMessage().toPath());
    }

    private void getKeys() throws Exception {
        privateKey = IOCrypto.CreatePrivateKeyFromFile(fileCollector.getFilePrivateB().toPath());
        publicKey = IOCrypto.CreatePublicKeyFromFile(fileCollector.getFilePublicA().toPath());
    }

    private void OpenSaveDialog(ActionEvent actionEvent) {
        Stage stage = getStage(actionEvent);
        List<String> extensions = new ArrayList<>();
        extensions.add("*.txt");
        fileCollector.setFileDecMessage(fileCollector.ShowSaveDialog(stage, getDirectoryThatContainsAllTheOutputFiles(),
                "TXT files (*.txt)", extensions));
        fileCollector.setFileDecHash(new File(fileCollector.getFileDecMessage().getParent()
                + "/hash created at "
                + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + ".txt"));
    }

    private void ReadyTheWindow() {
        textAreaMessage.setText("");
        textFieldFile1.setText("");
        textFieldFile2.setText("");
        textFieldFile3.setText("");
        textFieldPublicA.setText("");
        textFieldPrivateB.setText("");
        textFieldInitVector.setText("");
    }

    private void SetPathsToTextFields() {
        //Set all the paths in the textboxes
        textFieldFile1.setText(fileCollector.getEncryptedMessageFile().toPath().toString());
        textFieldFile2.setText(fileCollector.getEncryptedAesKeyFile().toPath().toString());
        textFieldFile3.setText(fileCollector.getEncryptedHash().toPath().toString());
        textFieldPublicA.setText(fileCollector.getFilePublicA().toPath().toString());
        textFieldPrivateB.setText(fileCollector.getFilePrivateB().toPath().toString());
        textFieldInitVector.setText(fileCollector.getFileInitVector().toPath().toString());
    }

    private void WriteMessageAndHashOfDecodedMessageToTextArea() throws Exception {
        Path pathToDecryptedMessage = fileCollector.getFileDecMessage().toPath();
        String decryptedMessage = IOCrypto.readFile(pathToDecryptedMessage);
        textAreaMessage.appendText(decryptedMessage);

        textAreaMessage.appendText("End of message" + System.lineSeparator() + "------------------------" + System.lineSeparator());
        //Get SHA256 hash of decoded message
        textAreaMessage.appendText("Hash of this message" + System.lineSeparator() + HashSHA256.generateHash(pathToDecryptedMessage));
    }

    private boolean EnsureThatEveryFileHasBeenSelected() {
        if (FileChecker.CheckIfFileDoesntExist(fileCollector.getFileInitVector())) {
            textAreaMessage.appendText("Please check the location for the file that contains your init vector file."
                    + System.lineSeparator());
        }

        if (FileChecker.CheckIfFileDoesntExist(fileCollector.getEncryptedMessageFile())) {
            textAreaMessage.appendText("Please check the location for the file that contains encrypted message." +
                    " This is file 1." + System.lineSeparator());
        }

        if (FileChecker.CheckIfFileDoesntExist(fileCollector.getEncryptedAesKeyFile())) {
            textAreaMessage.appendText("Please check the location for the file that contains encrypted AES key." +
                    " This is file 2." + System.lineSeparator());
        }

        if (FileChecker.CheckIfFileDoesntExist(fileCollector.getEncryptedHash())) {
            textAreaMessage.appendText("Please check the location for the file that contains encrypted hash." +
                    " This is file 3." + System.lineSeparator());
        }

        if (FileChecker.CheckIfFileDoesntExist(fileCollector.getFilePublicA())) {
            textAreaMessage.appendText("Please check the location for the file that contains public key of the sender." +
                    " This is public A." + System.lineSeparator());
        }

        if (FileChecker.CheckIfFileDoesntExist(fileCollector.getFilePrivateB())) {
            textAreaMessage.appendText("Please check the location for the file that contains the private key of the receiver." +
                    " This is private B." + System.lineSeparator());
        }

        return !textAreaMessage.getText().contains("Please");
    }

    private Stage getStage(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        return (Stage) (node).getScene().getWindow();
    }

}
