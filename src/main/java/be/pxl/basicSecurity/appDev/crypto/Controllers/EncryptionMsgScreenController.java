package be.pxl.basicSecurity.appDev.crypto.Controllers;

import be.pxl.basicSecurity.appDev.crypto.*;
import be.pxl.basicSecurity.appDev.steganography.LSB_encode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.crypto.spec.IvParameterSpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static be.pxl.basicSecurity.appDev.crypto.FileCollector.*;

public class EncryptionMsgScreenController {
    @FXML
    private TextField textFieldFile1;
    @FXML
    private TextField textFieldFile2;
    @FXML
    private TextField textFieldFile3;
    @FXML
    private TextArea textAreaMessage;
    @FXML
    private TextArea textAreaOutput;
    @FXML
    private TextArea textAreaOutputSteganography;
    @FXML
    private ImageView imageView;
    @FXML
    private ImageView imageForSteganography;
    @FXML
    private Button encryptionButton;
    @FXML
    private Button SelectMessage;
    @FXML
    private Button encryptionButtonSteganography;
    @FXML
    private Button  SelectImageButton;

    private FileCollector fileCollector;
    private PrivateKey privateKeyEnc;
    private PublicKey publicKeyDec;
    private BufferedImage imageForEncryption = null;

    EncryptionMsgScreenController(FileCollector fileCollector) {
        this.fileCollector = fileCollector;
    }

    public void SaveMessage(ActionEvent actionEvent) {
        try {
            Stage stage = getStage(actionEvent);
            //Empty textFields and TextAreas
            EmptyTextFieldsAndAreas();
            if (textAreaMessage.getText().isEmpty()) {
                throw new Exception("Please fill in a message.");
            }
            //Open savedialog and save message and hash of message in same folder
            List<String> extensions = new ArrayList<>();
            extensions.add("*.txt");
            fileCollector.setFileDecMessage(fileCollector.ShowSaveDialog(stage, getDirectoryThatContainsAllTheInputFiles(),
                    "TXT files (*.txt)", extensions));
            fileCollector.setFileDecHash(new File(fileCollector.getFileDecMessage().getParent() +
                    "/hash created at " +
                    new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + ".txt"));
            HandleFileSaving();
        } catch (IOException e) {
            FileChecker.ExceptionOccurred(fileCollector, imageView, "Message was not saved successfully"
                    + System.lineSeparator(), textAreaOutput, e);

        } catch (NoSuchAlgorithmException e) {
            FileChecker.ExceptionOccurred(fileCollector, imageView, "Message not hashed successfully."
                    + System.lineSeparator(), textAreaOutput, e);

        } catch (Exception e) {
            FileChecker.ExceptionOccurred(fileCollector, imageView, "Oops, something went wrong."
                    + System.lineSeparator(), textAreaOutput, e);
        }
    }

    public void SelectMessage(ActionEvent actionEvent) {
        try {
            Stage stage = getStage(actionEvent);
            EmptyTextFieldsAndAreas();
            //Open selectDialog and save hash of message in same folder
            List<String> extensions = new ArrayList<>();
            extensions.add("*.txt");
            fileCollector.setFileDecMessage(fileCollector.ShowSelectDialog(stage, getDirectoryThatContainsAllTheInputFiles(),
                    "TXT files (*.txt)", extensions));
            fileCollector.setFileDecHash(new File(getDirectoryThatContainsAllTheInputFiles() +
                    "/hash created at " +
                    new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + ".txt"));
            textAreaMessage.setText(IOCrypto.readFile(fileCollector.getFileDecMessage().toPath()));
            HandleFileSaving();
        } catch (IOException e) {
            FileChecker.ExceptionOccurred(fileCollector, imageView, "Message was not saved successfully"
                    + System.lineSeparator(), textAreaOutput, e);

        } catch (NoSuchAlgorithmException e) {
            FileChecker.ExceptionOccurred(fileCollector, imageView, "Message not hashed successfully."
                    + System.lineSeparator(), textAreaOutput, e);

        } catch (Exception e) {
            FileChecker.ExceptionOccurred(fileCollector, imageView, "Oops, something went wrong please try again or write a message instead of selecting a text file."
                    + System.lineSeparator(), textAreaOutput, e);
        }
    }

    public void StartEncrypting(ActionEvent actionEvent) {
        try {
            //empty textFields and textAreas and set all the paths in the textboxes
            EmptyTextFieldsAndAreas();

            //Create keys from files
            createKeysFromFiles();

            //Create AES key and write to file
            Key AESKey = AESEncryption.generateRandomAESKey(KeySize.SIZE_128);
            IOCrypto.writeKeyToFile(AESKey, fileCollector.getAesKeyForMessage().toPath());
            //Create init vector and write to file
            IvParameterSpec iv = AESEncryption.generateInitVector();
            IOCrypto.writeInitVectorToFile(iv, fileCollector.getFileInitVectorForMessage().toPath());

            //Create encrypted AesKey, Write encoded keys, message and encoded hash of message to files
            AESEncryption.encryptFileAES(IOCrypto.readKeyFromFile(fileCollector.getAesKeyForMessage().toPath(),
                    UsableAlgorithm.AES, KeySize.SIZE_128), iv, fileCollector.getFileDecMessage().toPath(),
                    fileCollector.getEncryptedMessageFile().toPath());
            byte[] encryptedAESKey = RSAEncryption.encryptSymmetricKeyRSA(
                    publicKeyDec, AESKey, fileCollector.getEncryptedAesKeyFileForMessage().toPath());
            IOCrypto.writeEncryptedKeyToFile(encryptedAESKey, fileCollector.getEncryptedAesKeyFileForMessage().toPath());
            RSAEncryption.encryptHashFileRSA(
                    privateKeyEnc, fileCollector.getFileDecHash().toPath(), fileCollector.getEncryptedHashForMessage().toPath());

            //Ensure that all files have been created and saved.
            if (!EnsureThatEveryFileHasBeenSaved()) {
                fileCollector.SetImageView(fileCollector.getCrossRemoveSign(), imageView);
                return;
            }
            //Set imageview to ticksign so the user knows everything worked
            textAreaOutput.setText("Message, key and hash is encrypted and saved successfully.");
            SetPathsToTextFields(false);
            fileCollector.SetImageView(fileCollector.getTickSign(), imageView);
        } catch (IOException e) {
            FileChecker.ExceptionOccurred(fileCollector, imageView, "Message, key or hash was not saved successfully."
                    + System.lineSeparator(), textAreaOutput, e);
        } catch (NoSuchAlgorithmException e) {
            FileChecker.ExceptionOccurred(fileCollector, imageView, "Message, key or hash was not encrypted successfully."
                    + System.lineSeparator(), textAreaOutput, e);
        } catch (Exception e) {
            FileChecker.ExceptionOccurred(fileCollector, imageView, "Message, key or hash was not found."
                    + System.lineSeparator(), textAreaOutput, e);
        }

    }

    public void StartEncryptingMessageInImage() {
        textAreaOutputSteganography.setText("");
        try {
            //Create keys from files
            createKeysFromFiles();

            //Create AES key and write to file
            Key AESKey = AESEncryption.generateRandomAESKey(KeySize.SIZE_128);
            IOCrypto.writeKeyToFile(AESKey, fileCollector.getAesKeyForImage().toPath());

            //Create init vector and write to file
            IvParameterSpec iv = AESEncryption.generateInitVector();
            IOCrypto.writeInitVectorToFile(iv, fileCollector.getFileInitVectorForImage().toPath());

            //Encrypt the message
            String encText = AESEncryption.encryptStringAES(textAreaMessage.getText(), AESKey, iv);
            //Hide message in the image and put it on the "internet"
            int[] bits = LSB_encode.getMessageBits(encText);
            LSB_encode.hideTheMessage(bits, imageForEncryption, fileCollector.getFileImage().toPath());

            //Create encrypted AesKey, Write encoded keys and encoded hash of message to files
            byte[] encryptedAESKey = RSAEncryption.encryptSymmetricKeyRSA(
                    publicKeyDec, AESKey, fileCollector.getEncryptedAesKeyFileForImage().toPath());
            IOCrypto.writeEncryptedKeyToFile(encryptedAESKey, fileCollector.getEncryptedAesKeyFileForImage().toPath());
            RSAEncryption.encryptHashFileRSA(
                    privateKeyEnc, fileCollector.getFileDecHash().toPath(), fileCollector.getEncryptedHashForImageMessage().toPath());

            //Set imageview to ticksign so the user knows everything worked
            textAreaOutputSteganography.setText("Message has been successfully encrypted and encoded into the image.");
            SetPathsToTextFields(true);
            fileCollector.SetImageView(fileCollector.getTickSign(), imageView);

        } catch (Exception e) {
            FileChecker.ExceptionOccurred(fileCollector, imageView, "Could not put encrypted message into image please try again."
                    + System.lineSeparator(), textAreaOutputSteganography, e);
        }
    }

    public void SelectImage(ActionEvent actionEvent) throws IOException {
        Stage stage = getStage(actionEvent);
        textAreaOutputSteganography.setText("");
        List<String> extensions = new ArrayList<>();
        extensions.add("*.jpg");
        extensions.add("*.png");
        File imageFile = fileCollector.ShowSelectDialog(stage, System.getProperty("user.home") + "\\pictures", "Image Files (*.jpg and *.png)", extensions);
        if (imageFile == null) {
            textAreaOutputSteganography.appendText("Please select an image.");
            encryptionButtonSteganography.disableProperty().setValue(true);
            return;
        }
        if (textAreaMessage.getText().isEmpty()) {
            textAreaOutputSteganography.appendText("Please fill in or select a message in the previous tab.");
            encryptionButtonSteganography.disableProperty().setValue(true);
            return;
        }
        //Set image in imageview
        Image image = new Image(imageFile.toURI().toString());
        imageForSteganography.setImage(image);
        //Set image in an bufferedimage to ready it for the encryption process
        imageForEncryption = ImageIO.read(imageFile);
        encryptionButtonSteganography.disableProperty().setValue(false);
    }

    private void HandleFileSaving() throws Exception {
        //Ensure that a valid save location has been selected
        if (fileCollector.getFileDecMessage() == null || fileCollector.getFileDecHash() == null) {
            textAreaOutput.appendText("Please select a location to save your message and hash.");
            fileCollector.SetImageView(fileCollector.getCrossRemoveSign(), imageView);
            throw new Exception();
        }

        WriteMessageAndHashOfDecodedMessageToFile();
        textAreaOutput.appendText("Message saved/selected successfully.");
        encryptionButton.disableProperty().setValue(false);
        SelectImageButton.disableProperty().setValue(false);
        fileCollector.SetImageView(fileCollector.getTickSign(), imageView);
    }

    private void createKeysFromFiles() throws Exception {
        publicKeyDec = IOCrypto.CreatePublicKeyFromFile(fileCollector.getFilePublicB().toPath());
        privateKeyEnc = IOCrypto.CreatePrivateKeyFromFile(fileCollector.getFilePrivateA().toPath());
    }

    private boolean EnsureThatEveryFileHasBeenSaved() {
        if (FileChecker.CheckIfFileDoesntExist(fileCollector.getEncryptedMessageFile())) {
            textAreaOutput.appendText("Please check this location for the file that contains the encrypted message." + System.lineSeparator());
        }

        if (FileChecker.CheckIfFileDoesntExist(fileCollector.getEncryptedAesKeyFileForMessage())) {
            textAreaOutput.appendText("Please check this location for the file that contains the encrypted AESkey." + System.lineSeparator());
        }

        if (FileChecker.CheckIfFileDoesntExist(fileCollector.getEncryptedHashForMessage())) {
            textAreaOutput.appendText("Please check this location for the file that contains the encrypted hash. " + System.lineSeparator());
        }

        return !textAreaOutput.getText().contains("Please");
    }

    private Stage getStage(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        return (Stage) (node).getScene().getWindow();
    }

    private void WriteMessageAndHashOfDecodedMessageToFile() throws Exception {
        IOCrypto.writeToFile(textAreaMessage.getText(), fileCollector.getFileDecMessage().toPath());
        IOCrypto.writeToFile(HashSHA256.generateHash(fileCollector.getFileDecMessage().toPath()),
                fileCollector.getFileDecHash().toPath());
    }

    private void EmptyTextFieldsAndAreas() {
        textFieldFile1.setText("");
        textFieldFile2.setText("");
        textFieldFile3.setText("");
        textAreaOutput.setText("");
    }

    private void SetPathsToTextFields(boolean isImageEncryption) {
        if (isImageEncryption){
            textFieldFile1.setText(fileCollector.getFileImage().toPath().toString());
            textFieldFile2.setText(fileCollector.getEncryptedAesKeyFileForImage().toPath().toString());
            textFieldFile3.setText(fileCollector.getEncryptedHashForImageMessage().toPath().toString());
        }else {
            textFieldFile1.setText(fileCollector.getEncryptedMessageFile().toPath().toString());
            textFieldFile2.setText(fileCollector.getEncryptedAesKeyFileForMessage().toPath().toString());
            textFieldFile3.setText(fileCollector.getEncryptedHashForMessage().toPath().toString());
        }

    }
}
