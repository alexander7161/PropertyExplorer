
import javafx.scene.layout.*;

abstract public class PropertyPanel {
    protected String title = "";

    abstract protected Pane getPanel();

    abstract public void update();

    @Override
    public String toString() {
        return title;
    }

}
