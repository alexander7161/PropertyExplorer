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

import java.util.ArrayList;

abstract public class PropertyPanel {
    protected String title = "";

    abstract protected Pane getPanel();

    abstract public void update();

    @Override
    public String toString() {
        return title;
    }

}
