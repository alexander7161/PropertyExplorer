import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Displays statistics
 * @author Alexander, Sarosh, Ans
 * @version 28.03.2018
 */
public class StatisticsPanel extends PropertyPanel{
    private GridPane panel;
    private List<StatisticBox> statisticBoxesDisplayed = new ArrayList<>();
    public StatisticsPanel(){
        title = "Statistics";
        panel = new GridPane();
        //Create 4 statistics boxes and add them to the list of boxes.
        for (int i=1;i<=4;i++){
            statisticBoxesDisplayed.add(new StatisticBox());
        }
        panelSetUp();
    }


    private void panelSetUp(){
        //Add statistic Boxes in corners.
        panel.add(statisticBoxesDisplayed.get(0).getPanel(), 0, 0);
        panel.add(statisticBoxesDisplayed.get(1).getPanel(), 0, 1);
        panel.add(statisticBoxesDisplayed.get(2).getPanel(), 1, 0);
        panel.add(statisticBoxesDisplayed.get(3).getPanel(), 1, 1);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(50);
        panel.getColumnConstraints().addAll(column1, column2); // each get 50% of width
     
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(50);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(50);
        panel.getRowConstraints().addAll(row1, row2); // each get 50% of width
        panel.setGridLinesVisible(true);
    }

    @Override
    protected Pane getPanel() {
        return panel;
    }

    /**
     *  When the price range is changed updateStatistics() method
     *  is called on the statistics boxes.
     *  This means only the displayed statistics are updated which is all that is necessary.
     */
    public void update() {
        statisticBoxesDisplayed.forEach(StatisticBox::updateStatistics);
    }
}
