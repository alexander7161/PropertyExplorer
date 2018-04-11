

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class for CurrentListings.
 *
 * @author  Alexander Davis
 * @version 29.03.2018
 */
public class CurrentListingsTest
{
    /**
     * Default constructor for test class CurrentListingsTest
     */
    public CurrentListingsTest()
    {
    }

    /**
     * Tests that the minimum price is set correctly.
     */
    @Test
    public void minimumPriceIsSet()
    {
        CurrentListings.setMinimumPrice(50);
        assertEquals(50, CurrentListings.getMinimumPrice());
    }

    /**
     * Tests that the maximum price is set correctly
     * in that the max price is set to the highest price
     * in the current listings if max is set to 500.
     */
    @Test
    public void maximumPriceIsSet()
    {
        CurrentListings.setMaximumPrice(500);
        int highestPrice = 0;
        for(AirbnbListing l: CurrentListings.getCurrentListings()) {
            if(l.getPrice()>highestPrice) {
                highestPrice = l.getPrice();
            }
        }
        assertEquals(highestPrice, CurrentListings.getMaximumPrice());
    }

    /**
     * Tests that all the example listings are returned when range is at maximum.
     */
    @Test
    public void allListingsPresent()
    {
        CurrentListings.setMinimumPrice(0);
        CurrentListings.setMaximumPrice(500);
        assertEquals(53904, CurrentListings.getCurrentListings().size());
    }

    /**
     * Tests that the list filtering logic works correctly.
     * The lowest price is £8 for which there are 2 properties.
     */
    @Test
    public void lowestPriceReturnsRightValues()
    {
        CurrentListings.setMinimumPrice(0);
        CurrentListings.setMaximumPrice(8);
        assertEquals(2, CurrentListings.getCurrentListings().size());
    }
}

