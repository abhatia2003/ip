package morpheus;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI entry point for Morpheus using FXML.
 */
public class Main extends Application {

    private static final String STORAGE_FILE_PATH = "data/morpheus.txt";
    private final Morpheus morpheus = new Morpheus(STORAGE_FILE_PATH);

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);

            // Provide controller with required dependencies (Morpheus logic and Stage)
            MainWindow controller = fxmlLoader.getController();
            controller.setMorpheus(morpheus);
            controller.setStage(stage);

            stage.show();
        } catch (IOException e) {
            // Log error and exit gracefully to avoid silent UI failure
            e.printStackTrace();
            System.err.println("Failed to load MainWindow.fxml");
        }
    }
}