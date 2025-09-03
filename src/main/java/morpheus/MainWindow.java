package morpheus;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Morpheus morpheus;
    private Stage stage; // <- keep a reference to the stage

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/neo.png"));
    private Image morpheusImage = new Image(this.getClass().getResourceAsStream("/images/morpheus.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Morpheus instance */
    public void setMorpheus(Morpheus m) {
        morpheus = m;
        // Display welcome message when program starts
        String welcome = morpheus.getWelcomeMessage();
        dialogContainer.getChildren().add(
                DialogBox.getMorpheusDialog(welcome, morpheusImage)
        );
    }

    /** Injects the Stage instance */
    public void setStage(Stage s) {
        stage = s;
    }

    /**
     * Handles user input and appends responses to the dialog container.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = morpheus.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getMorpheusDialog(response, morpheusImage)
        );
        userInput.clear();

        // Close program when response signals termination
        if ("END PROGRAM".equals(response)) {
            stage.close();
        }
    }
}