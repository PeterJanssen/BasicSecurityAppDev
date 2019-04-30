package be.pxl.basicSecurity.appDev.crypto;

import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import static be.pxl.basicSecurity.appDev.crypto.FileChecker.CreateFoldersIfTheyAreDeleted;

public class CryptoApp extends Application {
    public static void main(String[] args) {
        CreateFoldersIfTheyAreDeleted();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/StartScreen.fxml"));
        Scene scene = new Scene(root,1200,600);
        stage.setTitle("CryptoApp");
        stage.setScene(scene);
        stage.show();
    }
}
