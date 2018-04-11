

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The test class for Statistics
 *
 * @author  Alexander Davis
 * @version 29.03.2018
 */
public class StatisticsTest
{
    ArrayList<Statistic> statistics;
    /**
     * Default constructor for test class StatisticsTest
     */
    public StatisticsTest()
    {
        CurrentListings.setMinimumPrice(0);
        CurrentListings.setMaximumPrice(500);
        statistics = Statistic.getStatistics();
    }

    /**
     * Tests Average number of reviews per property.
     */
    @Test
    public void testStatistic1() {
        //Get first test.
        String stat = statistics.get(0).updateStatistic();
        int totalProperties = CurrentListings.getCurrentListings().size();
        int totalReviews = CurrentListings.getCurrentListings().stream()
                                            .map(AirbnbListing::getNumberOfReviews)
                                            .mapToInt(Integer::intValue)
                                            .sum();
        assertEquals(totalReviews/totalProperties,Integer.parseInt(stat));
    }

    /**
     * Tests Total number of available properties.
     */
    @Test
    public void testStatistic2() {
         String stat = statistics.get(1).updateStatistic();
        assertEquals(CurrentListings.getCurrentListings().size(),Integer.parseInt(stat));
    }

    /**
     * Tests total number of available entire homes and apartments.
     */
    @Test
    public void testStatistic3() {
        String stat = statistics.get(2).updateStatistic();
        Long numberEntireHomeApartments = CurrentListings.getCurrentListings().stream()
                          .filter(e->e.getRoom_type().contains("Entire home/apt"))
                          .count();
        assertEquals(Math.toIntExact(numberEntireHomeApartments),Integer.parseInt(stat));
    }

    /**
     * Tests the priciest neighbourhood.
     */
    @Test
    public void testStatistic4() {
        String stat = statistics.get(3).updateStatistic();
        ArrayList<AirbnbListing> listings = CurrentListings.getCurrentListings();

        //Get set with neighbourhood names.
        Set<String> neighbourhoods =
                CurrentListings.getCurrentListings().stream()
                        .map(AirbnbListing::getNeighbourhood)
                        .collect(Collectors.toSet());
        //Stores price as key and neighbourhood as value.
        HashMap<Integer, String> neighbourhoodPriciest = new HashMap<>();
        //For each neighbourhood calculate the sum of the price*MinimumNights
        //for each property. Find the average sum.
        //Add this to the hashMap.
        for(String neighbourhood: neighbourhoods) {
            int totalPrice = listings.stream()
                    .filter(e -> e.getNeighbourhood().equals(neighbourhood))
                    .mapToInt(e -> (e.getPrice() * e.getMinimumNights()))
                    .sum();
            int totalProperties = (int) listings.stream()
                    .filter(e -> e.getNeighbourhood().equals(neighbourhood))
                    .count();

            int total = totalPrice/totalProperties;
            neighbourhoodPriciest.put(total, neighbourhood);
        }
        //Find this highest price.
        int mostExpensive = neighbourhoodPriciest.keySet().stream().max(Comparator.comparing(i -> i)).get();
        String mostExpensiveNeighbourhood = neighbourhoodPriciest.get(mostExpensive);
        assertEquals(mostExpensiveNeighbourhood.toLowerCase(), stat.toLowerCase());
    }
}
