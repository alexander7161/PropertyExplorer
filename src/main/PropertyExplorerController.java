import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import multirange.MultiRange;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Property Explorer. Defines the logic of the GUI elements.
 *
 * @author Alex
 * @version 28.03.2018
 */
public class PropertyExplorerController implements Initializable {
    @FXML
    public BorderPane rootPane;
    @FXML
    private Button leftButton;
    @FXML
    private Button rightButton;
    @FXML
    private Label currentPaneLabel;
    @FXML
    private MultiRange priceRangeSlider;
    @FXML
    private Label fromPriceLabel;
    @FXML
    private Label toPriceLabel;
    //Holds the panels to be shown.
    private List<PropertyPanel> panels = new ArrayList<>();
    //index starts at 0.
    private int currentPanelIndex;

    //keeps track of when the price range has been set by the user.
    private boolean fromPriceSet;
    private boolean toPriceSet;

    /**
     * Called when the program starts and GUI elements are loaded.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Create instances of the panels to be shown.
        PropertyPanel welcomePanel = new WelcomePanel();
        PropertyPanel mapPanel = new MapPanel();
        PropertyPanel statisticsPanel = new StatisticsPanel();
        PropertyPanel fourthPanel = new FourthPanel();
        panels.add(welcomePanel);
        panels.add(mapPanel);
        panels.add(statisticsPanel);
        panels.add(fourthPanel);

        priceRangeSlider.setLabelFormatter(new StringConverter<Number>() {
            @Override
            public Number fromString(String string) {
                return 0;
            }

            @Override
            public String toString(Number object) {
                if(object.intValue()==500) {
                    return "£"+object.shortValue()+"+";
                }
                return "£"+object.shortValue();
            }
        });

        //Current pane set to welcome pane.
        currentPanelIndex = 0;
        //update the current pane.
        updateCurrentPanel();

        // price range is initially not set.
        this.fromPriceSet = false;
        this.toPriceSet = false;

        createPriceSliderListeners();
    }

    /**
     * Sets the current panel shown to the current panel index.
     * Updates the current panel label.
     * Calls update() on the new panel.
     */
    private void updateCurrentPanel() {
        rootPane.setCenter(panels.get(currentPanelIndex).getPanel());
        currentPaneLabel.setText(panels.get(currentPanelIndex).toString());
        panels.get(currentPanelIndex).update();
    }

    /**
     * When left button clicked decrease the currentPanelIndex.
     * disables left button if reaches the 0th element in the panel index.
     * Updates the new panel.
     */
    @FXML
    private void leftButtonClick() {
        //Ensure right button enabled when left clicked.
        rightButton.setDisable(false);
        currentPanelIndex -= 1;
        updateCurrentPanel();
        if (currentPanelIndex == 0) {
            leftButton.setDisable(true);
        }
    }

    /**
     * When right button clicked increment the currentPanelIndex.
     * disables right button if reaches the maximum element in the panel index.
     * Updates the new panel.
     */
    @FXML
    private void rightButtonClick() {
        //Ensure left button enabled when left clicked.
        leftButton.setDisable(false);
        currentPanelIndex += 1;
        updateCurrentPanel();
        if (currentPanelIndex == panels.size() - 1) {
            rightButton.setDisable(true);
        }
    }

    /**
     * Creates listeners for the price range slider knobs.
     * Enables left and right buttons when both price knobs have been chosen.
     * current panel update() method called when price range changed.
     */
    private void createPriceSliderListeners() {
        priceRangeSlider.lowValueChangingProperty().addListener(( (arg, oldVal, newVal) -> {
            // When low knob is not moving anymore.
            if(!newVal) {
                // Set the minimum price to the new value chosen.
                CurrentListings.setMinimumPrice((int) Math.round(priceRangeSlider.getLowValue()));
                //Update the from price label.
                fromPriceLabel.setText("£" + Integer.toString(CurrentListings.getMinimumPrice()));
                //Update the current panel.
                panels.get(currentPanelIndex).update();
                // If a knob has not yet been set.
                if(!fromPriceSet || !toPriceSet) {
                    fromPriceSet = true;
                    //If the toPrice has also been set enable buttons.
                    if (toPriceSet) {
                        rightButton.setDisable(false);
                    }
                }
            }
        }));
        priceRangeSlider.highValueChangingProperty().addListener(((arg, oldVal, newVal) -> {
            // When high knob is not moving anymore.
            if (!newVal) {
                // Set the maximum price to the new value chosen.
                CurrentListings.setMaximumPrice(Math.round((int) Math.round(priceRangeSlider.getHighValue())));
                //Update the to price label.
                toPriceLabel.setText("£" + Integer.toString(CurrentListings.getMaximumPrice()));
                //Update the current panel.
                panels.get(currentPanelIndex).update();
                // If a knob has not yet been set.
                if(!fromPriceSet || !toPriceSet) {
                    toPriceSet = true;
                    //If the fromPrice has also been set enable buttons.
                    if (fromPriceSet) {
                        rightButton.setDisable(false);
                    }
                }
            }
        }));
    }
}
