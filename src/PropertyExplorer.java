import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;


/**
 * Main window which holds the different panels.
 *
 * @author Alexander Davis
 * @version 28.03.2018
 */
public class PropertyExplorer extends Application {
    @Override
    public void start(Stage stage) throws Exception {
//        File f = new File("+libs/stanford-corenlp-3.9.1-models.jar");
//        if(!f.exists()) {
//            System.out.println("Please download model https://stanfordnlp.github.io/CoreNLP/index.html#download");
//            System.out.println("Copy contents to +libs folder.");
//            return;
//        }
        //Get the layout FXML file.
        BorderPane root = FXMLLoader.load(
                new URL(PropertyExplorer.class.getResource("PropertyExplorerLayout.fxml").toExternalForm())
        );
        Scene scene = new Scene(root);
        stage.setTitle("Property Explorer");
        //Add an icon to the window.
        stage.getIcons().add(new Image("img/logo.png"));
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}

