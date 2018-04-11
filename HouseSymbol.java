import javafx.event.EventType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

public class HouseSymbol extends ImageView{
    String neighborhood;
    public HouseSymbol(ArrayList<AirbnbListing> standsFor){
        super("img/HouseSymbolImage.png");
        neighborhood=standsFor.get(0).getNeighbourhood();
        addEventHandler(MouseEvent.MOUSE_CLICKED,(event)->new NeighborhoodPopup(standsFor).start(new Stage()));
        addEventFilter(MouseEvent.MOUSE_ENTERED_TARGET, (event) -> this.setOpacity(0.5));
        addEventFilter(MouseEvent.MOUSE_EXITED_TARGET, (event) -> this.setOpacity(1));

    }

    public String getNeighborhood() {
        return neighborhood;
    }
}
