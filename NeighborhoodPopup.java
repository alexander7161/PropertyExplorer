import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.util.List;
import java.util.Observable;

/**
 * Displays the selected properties
 */
public class NeighborhoodPopup extends Application{
    private List<AirbnbListing> airbnbListings;
    private String neighborhood;
    private TableView<AirbnbListing> table;
    private ComboBox comboBox;
    private TableColumn<AirbnbListing,String> hostCol;
    private TableColumn<AirbnbListing,Integer> minNightsCol;
    private TableColumn<AirbnbListing,Integer> priceCol;
    private TableColumn<AirbnbListing,Integer> numReviewsCol;

    /**
     * constructs by a catalogue
     * @param catalogue list of properties to display, program logic assumes that they are all from the same neighborhood
     */
    public NeighborhoodPopup(List<AirbnbListing> catalogue){
        this.airbnbListings=catalogue;
        neighborhood=airbnbListings.get(0).getNeighbourhood();
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Houses near "+neighborhood);
        GridPane gridPane= new GridPane();
        primaryStage.setMinWidth(400.0);
        primaryStage.setMinHeight(400.0);
        primaryStage.setMaxWidth(600.0);
        primaryStage.setMaxHeight(500.0);

        Scene scene= new Scene(gridPane);
        ObservableList<String> options= FXCollections.observableArrayList("Num. Reviews","Host Name","Price");
        comboBox=new ComboBox(options);

        Label label = new Label("Houses near "+neighborhood);
        setUpTable();

        gridPane.addRow(0,label);
        gridPane.addRow(1,comboBox);
        gridPane.addRow(2,table);
        GridPane.setHgrow(table, Priority.ALWAYS);
        GridPane.setVgrow(table,Priority.ALWAYS);
        comboBoxBehaviourSetUp();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * This method sets up the columns of the table and populate the table with currentListing
     */
    private void setUpTable(){
        table=new TableView<>();
        hostCol= new TableColumn("Host Name");
        hostCol.setCellValueFactory((f)->new ReadOnlyObjectWrapper<>(f.getValue().getHost_name()));
        minNightsCol= new TableColumn("Minimum Nights");
        minNightsCol.setCellValueFactory((f)->new ReadOnlyObjectWrapper<>(f.getValue().getMinimumNights()));
        priceCol= new TableColumn("Price");
        priceCol.setCellValueFactory((f)->new ReadOnlyObjectWrapper<>(f.getValue().getPrice()));
        numReviewsCol=new TableColumn("Number of Reviews");
        numReviewsCol.setComparator(numReviewsCol.getComparator().reversed());

        numReviewsCol.setCellValueFactory((f)->new ReadOnlyObjectWrapper<>(f.getValue().getNumberOfReviews()));
        table.getColumns().addAll(hostCol,minNightsCol,priceCol,numReviewsCol);
        for(AirbnbListing listing:airbnbListings){
            table.getItems().add(listing);
        }
        table.setRowFactory(tableView->{
            TableRow<AirbnbListing> tableRow=new TableRow<>();
            tableRow.addEventHandler(MouseEvent.MOUSE_CLICKED,(event)->{
                new PropertyPage(tableRow.getItem()).start(new Stage());
            });
            return tableRow;
        });
        table.sort();
        numReviewsCol.setCellValueFactory((f)->new ReadOnlyObjectWrapper<>(f.getValue().getNumberOfReviews()));
    }

    /**
     * This method adds the behaviour of the combo box so that it can sort the table when one of its item is click.
     */
    private void comboBoxBehaviourSetUp(){
        comboBox.valueProperty().addListener((obs,old,newP)->{
            if(newP!=null){
                if(newP.equals("Num. Reviews")){
                    table.getSortOrder().clear();
                    table.getSortOrder().add(numReviewsCol);
                    table.sort();
                }
                else if (newP.equals("Host Name")){
                    table.getSortOrder().clear();
                    table.getSortOrder().add(hostCol);
                    table.sort();
                }
                else if (newP.equals("Price")){
                    table.getSortOrder().clear();
                    table.getSortOrder().add(priceCol);
                    table.sort();
                }
            }
        });
    }
}
