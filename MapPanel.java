import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.collections.FXCollections;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * A Panel that displays the map
 */
public class MapPanel extends PropertyPanel {
    private ArrayList<AirbnbListing> currentListings1;
    private StackPane panel;
    private StackPane symbols;

    public MapPanel(){
        title = "Map";
    }

    public StackPane getPanel(){
        panelSetUp();
        update();
        return panel;
    }

    public void update() {
        symbols.getChildren().clear();
        currentListings1=CurrentListings.getCurrentListings();
        HashMap<String,ArrayList<AirbnbListing>> neighborhoodTable=createNeighborhoodTable();
        for(ArrayList<AirbnbListing> neighborhood:neighborhoodTable.values()){

            HouseSymbol curSymbol =new HouseSymbol(neighborhood);
            symbols.getChildren().add(curSymbol);
            double size= getSizeOfMarker(neighborhood);
            curSymbol.setFitHeight(size);
            curSymbol.setFitWidth(size);
            align(curSymbol);
        }

    }

    /**
     *
     * @return the calculated size of the marker according to the number of properties in that neighbourhood
     */
    private double getSizeOfMarker(ArrayList<AirbnbListing> neighborhood){
        return Math.min(Math.max(25.0,600.0*(double)neighborhood.size()/currentListings1.size()),50.0);
    }

    private void panelSetUp(){
        panel = new StackPane();
        symbols=new StackPane();
        Image image = new Image(new File("img/map.png").toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.fitHeightProperty().bind(panel.heightProperty());
        panel.getChildren().addAll(imageView,symbols);
        }
        private void align(HouseSymbol symbol){
            switch (symbol.getNeighborhood()){
                case "Hillingdon":
                    symbol.setTranslateX(-282.0);
                    symbol.setTranslateY(-61.0);
                    break;
                case "Harrow":
                    symbol.setTranslateX(-206.0);
                    symbol.setTranslateY(-114.0);
                    break;
                case "Barnet":
                    symbol.setTranslateX(-96.0);
                    symbol.setTranslateY(-143.0);
                    break;
                case "Haringey":
                    symbol.setTranslateX(-15.0);
                    symbol.setTranslateY(-132.0);
                    break;
                case "Enfield":
                    symbol.setTranslateX(-0.0);
                    symbol.setTranslateY(-192.0);
                    break;
                case "Waltham Forest":
                    symbol.setTranslateX(55.0);
                symbol.setTranslateY(-132.0);
                break;
            case "Redbridge":
                symbol.setTranslateX(119.0);
                symbol.setTranslateY(-123.0);
                break;
            case "Havering":
                symbol.setTranslateX(239.0);
                symbol.setTranslateY(-106.0);
                break;
            case "Ealing":
                symbol.setTranslateX(-198.0);
                symbol.setTranslateY(-50.0);
                break;
            case "Hammersmith and Fulham":
                symbol.setTranslateX(-94.0);
                symbol.setTranslateY(12.0);
                break;
            case "Kensington and Chelsea":
                symbol.setTranslateX(-88.0);
                symbol.setTranslateY(-22.0);
                break;
            case "Westminster":
                symbol.setTranslateX(-55.0);
                symbol.setTranslateY(-26.0);
                break;
            case "Brent":
                symbol.setTranslateX(-145.0);
                symbol.setTranslateY(-101.0);
                break;
            case "Camden":
                symbol.setTranslateX(-68.0);
                symbol.setTranslateY(-77.0);
                break;
            case "Islington":
                symbol.setTranslateX(-18.0);
                symbol.setTranslateY(-74.0);
                break;
            case "Hackney":
                symbol.setTranslateX(17.0);
                symbol.setTranslateY(-81.0);
                break;
            case "City of London":
                symbol.setTranslateX(-8.0);
                symbol.setTranslateY(-33);
                break;
            case "Tower Hamlets":
                symbol.setTranslateX(34.0);
                symbol.setTranslateY(-45.0);
                break;
            case "Newham":
                symbol.setTranslateX(91.0);
                symbol.setTranslateY(-60.0);
                break;
            case "Barking and Dagenham":
                symbol.setTranslateX(163.0);
                symbol.setTranslateY(-67.0);
                break;
            case "Hounslow":
                symbol.setTranslateX(-249.0);
                symbol.setTranslateY(51.0);
                break;
            case "Richmond upon Thames":
                symbol.setTranslateX(-201.0);
                symbol.setTranslateY(83.0);
                break;
            case "Kingston upon Thames":
                symbol.setTranslateX(-154.0);
                symbol.setTranslateY(146.0);
                break;
            case "Wandsworth":
                symbol.setTranslateX(-81.0);
                symbol.setTranslateY(47.0);
                break;
            case "Lambeth":
                symbol.setTranslateX(-25.0);
                symbol.setTranslateY(53.0);
                break;
            case "Southwark":
                symbol.setTranslateX(13.0);
                symbol.setTranslateY(26.0);
                break;
            case "Lewisham":
                symbol.setTranslateX(54.0);
                symbol.setTranslateY(44.0);
                break;
            case "Greenwich":
                symbol.setTranslateX(106.0);
                symbol.setTranslateY(29.0);
                break;
            case "Bexley":
                symbol.setTranslateX(161.0);
                symbol.setTranslateY(55.0);
                break;
                case "Bromley":
                    symbol.setTranslateX(116.0);
                    symbol.setTranslateY(171.0);
                    break;
                case "Croydon":
                    symbol.setTranslateX(3.0);
                    symbol.setTranslateY(143.0);
                    break;
            case "Merton":
                symbol.setTranslateX(-97.0);
                symbol.setTranslateY(88.0);
                break;
            case "Sutton":
                symbol.setTranslateX(-64.0);
                symbol.setTranslateY(172.0);
                break;


            default:
                symbol.setTranslateX(0);
                symbol.setTranslateY(0);
                break;
        }
    }

    /**
     * It returns a map to the the current listings by neighbours.
     * @return
     */
    public HashMap<String,ArrayList<AirbnbListing>> createNeighborhoodTable(){
        HashMap<String,ArrayList<AirbnbListing>> neighborhoodTable=new HashMap<>();
        for (AirbnbListing airbnbListing: currentListings1) {
            if(!neighborhoodTable.containsKey(airbnbListing.getNeighbourhood())){
                neighborhoodTable.put(airbnbListing.getNeighbourhood(),new ArrayList<AirbnbListing>());
            }
            neighborhoodTable.get(airbnbListing.getNeighbourhood()).add(airbnbListing);
        }
        return neighborhoodTable;
    }
}
