import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PropertyPage extends Application {
    private AirbnbListing listing;

    public PropertyPage(AirbnbListing property){
        super();
        listing=property;
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(listing.getRoom_type()+" at "+listing.getNeighbourhood()+" for "+listing.getPrice());
        GridPane gridPane=new GridPane();
        gridPane.addRow(0,new Label("Description"),new Label(listing.getName()));

        gridPane.addRow(1,new Label("Price"),new Label(new Integer(listing.getPrice()).toString()));
        gridPane.addRow(2,new Label("Room Type"),new Label(listing.getRoom_type()));
        gridPane.addRow(3,new Label("Minimum Nights"),new Label(new Integer(listing.getMinimumNights()).toString()));
        gridPane.addRow(4,new Label("Availability over year(in days)"),new Label(new Integer(listing.getAvailability365()).toString()));
        gridPane.addRow(5,new Label("Number of Reviews"),new Label(new Integer(listing.getNumberOfReviews()).toString()));
        gridPane.addRow(6,new Label("Last Review"),new Label(listing.getLastReview()));

        Scene scene=new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
