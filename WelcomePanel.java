import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.collections.FXCollections;

public class WelcomePanel extends PropertyPanel{

    private BorderPane panel;
    private Label fromPriceLabel;
    private Label toPriceLabel;


    public WelcomePanel() {
        title = "Welcome";
        panel = new BorderPane();
        panel.setId("welcomePanel");
        panelSetUp();
    }

    public Pane getPanel(){
        return panel;
    }
    
    private void panelSetUp(){
        GridPane root = new GridPane();

        Label instructionsLabel = new Label(getInstructionString());
        instructionsLabel.setId("instructionsLabel");
        instructionsLabel.setWrapText(true);
        fromPriceLabel = new Label();
        fromPriceLabel.setId("WelcomeFromPrice");
        toPriceLabel = new Label();
        toPriceLabel.setId("WelcomeToPrice");
        Label fromPriceText = new Label("Price range selected: £");
        fromPriceText.setId("WelcomeFromPriceText");
        Label toPriceText = new Label(" - £");
        toPriceText.setId("WelcomeToPriceText");

        HBox priceRangeHBox = new HBox();
        priceRangeHBox.getChildren().addAll(fromPriceText,fromPriceLabel,toPriceText,toPriceLabel);
        priceRangeHBox.setId("priceRangeHBox");

        root.add(instructionsLabel,1,1);
        root.add(priceRangeHBox,1,2);
        panel.setCenter(root);
    }

    public void update() {
        try {
            fromPriceLabel.setText(Integer.toString(CurrentListings.getMinimumPrice()));
            toPriceLabel.setText(Integer.toString(CurrentListings.getMaximumPrice()));
        }
        catch (Exception e) {
            // Don't update text if min or max price hasn't been chosen yet.
        }
    }

    private String getInstructionString(){
        String instructionString = "";
        String welcomeString = "Welcome to the Airbnb property explorer! \n\n";
        String firstInstruction = "1. Select the appropriate price range of properties you wish to see. \n\n";
        String secondInstruction = "2. Click on the 'back' and 'forward' buttons to see different information about these properties. \n\n";
        String thirdInstruction = " On the second page, click on the 'properties' \n marker on the map to see more details. \n\n";
        String forthInstruction = " On the third page, click on the 'next' buttons \n to see other statistics. \n\n";
        instructionString += welcomeString + firstInstruction + secondInstruction + thirdInstruction + forthInstruction;
        return instructionString;
    }

}
