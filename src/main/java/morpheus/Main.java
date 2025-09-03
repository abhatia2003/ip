package morpheus;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Duke using FXML.
 */
public class Main extends Application {

    private Morpheus morpheus = new Morpheus("data/morpheus.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);

            // Inject Morpheus instance AND Stage into the controller
            MainWindow controller = fxmlLoader.getController();
            controller.setMorpheus(morpheus);
            controller.setStage(stage);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}