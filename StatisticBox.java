import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.util.ArrayDeque;

/**
 * Individual statistics box which displays a statistic.
 *
 * @author Alex, Ans, Sarosh
 * @version 28.03.2018
 */
public class StatisticBox extends Pane {
    private BorderPane panel;
    //Statistics are stored in a queue so there is an order kept when the left button is pressed.
    //This is shared between all Statistics boxes.
    private static ArrayDeque<Statistic> currentStatistics = new ArrayDeque<>();
    //Stores the current statistic being shown by the box.
    private Statistic statistic;

    private Label statisticTitle = new Label();
    private Label statisticData = new Label();

    //When the boxes are created this is called once.
    static {
        currentStatistics.addAll(Statistic.getStatistics());
    }
    /**
     * Constructor for objects of class StatisticBox
     */
    public StatisticBox() {
        //Gives this box the first statistic in the queue.
        statistic = currentStatistics.removeFirst();
        panel = new BorderPane();
        VBox statBox = new VBox();
        statBox.setAlignment(Pos.CENTER);
        statBox.getChildren().add(statisticTitle);
        statBox.getChildren().add(statisticData);
        panel.setCenter(statBox);

        Button leftButton = new Button("<");
        panel.setLeft(leftButton);
        leftButton.setOnAction(e -> leftButtonClick());
        leftButton.minHeightProperty().bind(panel.heightProperty());

        Button rightButton = new Button(">");
        panel.setRight(rightButton);
        rightButton.setOnAction(e -> rightButtonClick());
        rightButton.minHeightProperty().bind(panel.heightProperty());

    }

    public BorderPane getPanel() {
        return panel;
    }

    /**
     * When right button is clicked
     * current statistic is added to front of queue.
     * statistic is taken from the back of queue.
     * Statistic is updated.
     */
    private void rightButtonClick() {
        currentStatistics.addFirst(statistic);
        statistic = currentStatistics.removeLast();
        updateStatistics();
    }

    /**
     * When left button is clicked
     * current statistic is added to back of queue.
     * statistic is taken from the front of queue.
     * Statistic is updated.
     */
    private void leftButtonClick() {
        currentStatistics.addLast(statistic);
        statistic = currentStatistics.removeFirst();
        updateStatistics();

    }

    /**
     * statistic is updated.
     */
    public void updateStatistics() {
        statisticTitle.setText(statistic.getTitle());
        statisticData.setText(statistic.getCurrentStatistic());
    }
}
