import java.util.*;
import java.util.stream.Collectors;

/**
 * Class to keep track of the current listings which change based on the price range chosen.
 * Static so an instance is not needed.
 * Created by k1761218 on 13/03/18.
 */
public final class CurrentListings {
    //Get data from AirbnbDataLoader class.
    private static AirbnbDataLoader loader= new AirbnbDataLoader();
    private static List<AirbnbListing> listings;
    //Set max and min to 'null' value initially.
    private static int max = -1;
    private static int min = -1;
    //Private constructor as not intended to be created as an object.
    private CurrentListings() {}
    //Static initializer loads listings before any static methods are called.
    static {
        listings = loader.load();
    }

    /**
     * @throws PriceRangeNotSetException if panels try to use data before data is available.
     *          Listings should not be empty when this method is called.
     * @return ArrayList of current listings based on price range selected by user.
     */
    public static ArrayList<AirbnbListing> getCurrentListings()
    {
        if(max ==-1||min==-1) {
            throw new PriceRangeNotSetException();
        }
        //Filter listings by the price range chosen.
        return  listings.stream()
                .filter(p -> p.getPrice()<=max)
                .filter(p -> p.getPrice()>=min)
                .collect(Collectors.toCollection(ArrayList::new));
    }


    /**
     * @param newMin integer of new minimum price to set.
     */
    public static void setMinimumPrice(int newMin)
    {
            CurrentListings.min = newMin;
    }

    /**
     * @param newMax  integer of new maximum price to set.
     */
    public static void setMaximumPrice(int newMax)
    {
        //If user chooses 500 set the maximum to the largest price in listings.
        if(newMax==500) {
            OptionalInt value = listings.stream().map(AirbnbListing::getPrice).mapToInt(Integer::intValue).max();
            if(value.isPresent()) {
                CurrentListings.max = value.getAsInt();
            }
        } else {
            CurrentListings.max = newMax;
        }
    }

    /**
     * @return Current minimum price set.
     * @throws PriceRangeNotSetException if the price range has not yet been chosen.
     */
    public static int getMinimumPrice()
            throws PriceRangeNotSetException {
        if(min==-1) {
            throw new PriceRangeNotSetException("Min has not yet been defined.");
        }
        else {
            return min;
        }
    }

    /**
     * @return Current maximum price set.
     * @throws PriceRangeNotSetException if the price range has not yet been chosen.
     */
    public static int getMaximumPrice()
            throws PriceRangeNotSetException {
        if(max==-1) {
            throw new PriceRangeNotSetException("Max has not yet been defined.");
        }
        else {
            return max;
        }
    }
}
