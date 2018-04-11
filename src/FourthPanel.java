import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.HBox;

/**
 * This panel uses sentiment analysis to analyse the reviews of a airbnbListing that the user
 * picks from the list of airbnb listings of a specific price range, and shows
 * the sentiment score in terms of the number of smiley faces and displays
 * the review and image of this item on the information panel of the right hand side.
 * @author Alexander Davis, Sarosh Habib
 * @version 29.03.2018
 */
public class FourthPanel extends PropertyPanel {
    private GridPane panel;
    private ListView<AirbnbListing> list = new ListView<>();
    private SentimentAnalysis analyser;
    // Side panel with info about selected property.
    private BorderPane informationPanel;
    // Box to hold sentiment rating.
    private HBox smilyBox;
    // Current selected property details.
    private String currentPropertyID;
    private List<String> currentReviews;

    public FourthPanel() {
        title = "Analysis";
        panel = new GridPane();
        // set listView cell factory.
        list.setCellFactory(param -> new ListingCell());
        informationPanel = new BorderPane();
        analyser = new SentimentAnalysis(this);

        panelSetUp();
    }

    @Override
    protected Pane getPanel() {
        return panel;
    }

    /**
     * This update method updates the list of current listings shown to the user according
     * to the price range specified.
     */
    public void update() {
        ObservableList<AirbnbListing> items = FXCollections.observableArrayList(CurrentListings.getCurrentListings());
        list.setItems(items);
    }


    /**
     * Add the number of smiley size according to the sentiment score analysed to a HBox, and
     * will be displayed to the user. The maximum indicating the most positive is 5 smiley faces
     * and 1 smiley face for the least positive text.
     */
    public void addSmilyFace(Integer score) {
        smilyBox.getChildren().clear();
        ArrayList<ImageView> smileList = new ArrayList<>();
        ;
        Label sentimentLabel = new Label("Sentiment score (1-5): ");
        for (int i = 0; i <= score && i <= 4; i++) { // set the max no. of smiley faces to 5
            Image image = new Image("img/Slightly-Smiling-Face-Emoji-Classic-Round-Sticker.jpg");
            ImageView imgview = new ImageView(image);
            imgview.setPreserveRatio(true);
            imgview.setFitWidth(20);
            smileList.add(imgview);
        }
        smilyBox.getChildren().add(sentimentLabel);
        smilyBox.getChildren().addAll(smileList);
    }

    /**
     * Set up the layout of the panel.
     */
    private void panelSetUp() {
        // Add listview to left column.
        panel.add(list, 0, 0);
        // Add informationPanel to right column.
        panel.add(informationPanel, 1, 0);
        // Initial instructions on right panel.
        informationPanel.setCenter(new Label("Click on one " +
                "item of the list on the left and \n see the sentiment score and details!"));
        // Limit information panel height to panel height.
        informationPanel.setMaxHeight(panel.heightProperty().get());
        // Smiley box holds sentiment analysis rating.
        smilyBox = new HBox(5);
        // Set right column and left column sizes.
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(60);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(40);
        panel.getColumnConstraints().addAll(column1, column2); // Column 1, 60%. Column 2, 40%.
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(100);
        panel.getRowConstraints().addAll(row1); // Height is 100%.

        // Add listener for listView item selected.
        list.getSelectionModel().selectedIndexProperty().addListener(
                (ov, old_val, new_val) -> {
                    // When the multirange slider is moved it returns -1 as new_val, and then returns actual value.
                    // If the same property is selected twice.
                    // Then skip.
                    if(new_val.intValue() == -1 || old_val.equals(new_val)) {
                        return;
                    }
                    // Get ID of property selected.
                    currentPropertyID = CurrentListings.getCurrentListings().get(new_val.intValue()).getId();
                    try {
                        // Get reviews for ID.
                        currentReviews = AirbnbAPI.getReviewComments(currentPropertyID);
                        List<String> reviews = currentReviews;
                        // Start analysis.
                        analyser = new SentimentAnalysis(this);
                        analyser.execute(String.join(", ", reviews));
                    } catch (Exception e) {
                        if (e.getLocalizedMessage().contains("403")) {
                            informationPanel.getChildren().clear();
                            informationPanel.setCenter(new Label("Listing does not exist on Airbnb."));
                        } else {
                            informationPanel.getChildren().clear();
                            informationPanel.setCenter(new Label("No reviews found."));

                        }
                    }
                });
    }

    /**
     * Called when a listing is clicked and sentiment analysis is calculating.
     */
    public void updateProgressLabel() {
        informationPanel.getChildren().clear();
        informationPanel.setCenter(new Label("Calculating sentiment analysis..."));
    }

    /**
     * Updates informationPanel with new information from user selection.
     * Called by sentiment analysis when complete.
     * @param score
     */
    public void updateListingPanel(int score) {
        informationPanel.getChildren().clear();
        VBox top = new VBox();
        // Sentiment score.
        addSmilyFace(score);
        top.getChildren().add(smilyBox);
        // Get listing image from Airbnb.
        ImageView listingImage;
        try {
            listingImage = new ImageView(AirbnbAPI.getListingImage(currentPropertyID));
            listingImage.setPreserveRatio(true);
            listingImage.fitWidthProperty().bind(informationPanel.widthProperty());
            listingImage.fitHeightProperty().set(400);
            top.getChildren().add(listingImage);
        } catch (Exception e) {
            System.out.println(e);
        }
        informationPanel.setTop(top);

        // Create label for each review.
        ArrayList<Label> reviewLabels = new ArrayList<>();
        for (String review : currentReviews) {
            Label label = new Label(review);
            label.setMaxWidth(300);
            label.setWrapText(true);
            reviewLabels.add(label);
        }
        // Add all reviews to VBox.
        VBox reviewLabelList = new VBox(20);
        reviewLabelList.getChildren().addAll(reviewLabels);
        // Add reviews VBox to scrollPane.
        ScrollPane reviewBox = new ScrollPane();
        reviewBox.setMaxHeight(200);
        reviewBox.setContent(reviewLabelList);
        informationPanel.setBottom(reviewBox);
    }
}

/**
 * This class is responsible for the individual cell of the list that is on the left hand side
 * of the panel to display the current listing.
 */
class ListingCell extends ListCell<AirbnbListing> {
    /**
     * This method updates the information of each cell according to the information
     * of the airbnb listing.
     * @param listing
     * @param empty
     */
    @Override
    public void updateItem(AirbnbListing listing, boolean empty) {
        super.updateItem(listing, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(null);
            BorderPane pane = new BorderPane();
            pane.setLeft(new Label(listing.getName()));
            pane.setRight(new Label(Integer.toString(listing.getNumberOfReviews())));
            setGraphic(pane);
        }
    }



}
