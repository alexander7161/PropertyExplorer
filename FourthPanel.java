import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;

import java.awt.*;
import java.io.File;
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
    private ObservableList<AirbnbListing> items;
    private SentimentAnalysis analyser;
    private BorderPane informationPanel;
    private HBox smilyBox;
    private String currentPropertyID;
    private List<String> currentReviews;

    public FourthPanel() {
        title = "Analysis";
        panel = new GridPane();
        list.setMinWidth(400);
        list.setCellFactory(param -> new ListingCell());
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
        items = FXCollections.observableArrayList(CurrentListings.getCurrentListings());
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
            Image image = new Image(new File("img/Slightly-Smiling-Face-Emoji-Classic-Round-Sticker.jpg").toURI().toString());
            ImageView imgview = new ImageView(image);
            imgview.setPreserveRatio(true);
            imgview.setFitWidth(20);
            smileList.add(imgview);
        }
        smilyBox.getChildren().add(sentimentLabel);
        smilyBox.getChildren().addAll(smileList);
    }

    /**
     * Set up the lay out of the panel.
     */
    private void panelSetUp() {
        panel.add(list, 0, 0);
        informationPanel = new BorderPane();
        informationPanel.setMinWidth(200);
        informationPanel.setCenter(new Label("Click on one " +
                "item of the list on the left and \n see the sentiment score and details!"));
        smilyBox = new HBox(5);
        panel.add(informationPanel, 1, 0);
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(60);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(40);
        panel.getColumnConstraints().addAll(column1, column2); // each get 50% of width
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(100);
        panel.getRowConstraints().addAll(row1); // each get 50% of width

        list.getSelectionModel().selectedIndexProperty().addListener(
                (ov, old_val, new_val) -> {
                    currentPropertyID = CurrentListings.getCurrentListings().get(new_val.intValue()).getId();
                    try {
                        currentReviews = AirbnbAPI.getReviewComments(currentPropertyID);
                        List<String> reviews = currentReviews;
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

    public void updateProgressLabel() {
        informationPanel.getChildren().clear();
        informationPanel.setCenter(new Label("Calculating sentiment analysis..."));
    }

    public void updateListingPanel(int score) {
        informationPanel.getChildren().clear();
        VBox top = new VBox();
        addSmilyFace(score);
        top.getChildren().add(smilyBox);
        ImageView listingImage;
        try {
            listingImage = new ImageView(AirbnbAPI.getListingImage(currentPropertyID));
            listingImage.setPreserveRatio(true);
            listingImage.fitWidthProperty().bind(informationPanel.widthProperty());
            listingImage.fitHeightProperty().set(400);
            top.getChildren().add(listingImage);
        } catch (Exception e) {

        }
        informationPanel.setTop(top);

            ArrayList<Label> reviewLabels = new ArrayList<>();
            for (String review : currentReviews) {
                Label label = new Label(review);
                label.setMaxWidth(300);
                label.setWrapText(true);
                reviewLabels.add(label);
            }
            VBox reviewLabelList = new VBox(20);
            reviewLabelList.getChildren().addAll(reviewLabels);
            ScrollPane reviewBox = new ScrollPane();
            reviewBox.setMaxHeight(300);
            reviewBox.setContent(reviewLabelList);
            informationPanel.setBottom(reviewBox);


//            dialogVbox.getChildren().addAll(reviewLabels);
//            dialogVbox.setFillWidth(true);
//            informationPanel.setCenter(listingImage);
//            informationPanel.setTop(smilyBox);
//            ScrollPane bottom = new ScrollPane();
//            bottom.setMaxHeight(300);
//            bottom.setContent(dialogVbox);
//            informationPanel.setBottom(bottom);
//        }
    }
}

/**
 * This class is responsible for the individual cell of the list that is on the right hand side
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
