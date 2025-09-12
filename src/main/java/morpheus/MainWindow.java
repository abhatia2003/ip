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

    private Morpheus morpheus;
    private Stage stage;

    private static final String USER_IMAGE_PATH = "/images/neo.png";
    private static final String MORPHEUS_IMAGE_PATH = "/images/morpheus.png";
    private static final String EXIT_COMMAND = "END PROGRAM";

    private final Image userImage = new Image(this.getClass().getResourceAsStream(USER_IMAGE_PATH));
    private final Image morpheusImage = new Image(this.getClass().getResourceAsStream(MORPHEUS_IMAGE_PATH));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Supplies the logic handler (Morpheus) to this controller.
     * Also initializes the UI with the welcome message.
     */
    public void setMorpheus(Morpheus m) {
        morpheus = m;
        String welcome = morpheus.getWelcomeMessage();
        dialogContainer.getChildren().add(
                DialogBox.getMorpheusDialog(welcome, morpheusImage)
        );
    }

    /**
     * Supplies the Stage reference to this controller for window management.
     */
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
        //Guard Clause
        if (EXIT_COMMAND.equals(response)) {
            stage.close();
            return;
        }

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getMorpheusDialog(response, morpheusImage)
        );
        userInput.clear();
    }
}