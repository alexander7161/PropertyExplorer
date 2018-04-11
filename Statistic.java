import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alex, Ans, Sarosh
 */
public abstract class Statistic {
    String title = "";

    /**
     * Only called once in Statistic box.
     * @return list of statistics objects.
     */
    public static ArrayList<Statistic> getStatistics() {
        ArrayList<Statistic> statistics = new ArrayList<>();
        statistics.add(new Stat1());
        statistics.add(new Stat2());
        statistics.add(new Stat3());
        statistics.add(new Stat4());
        statistics.add(new StatAlex());
        statistics.add(new StatAns());
        statistics.add(new StatFirat());
        statistics.add(new StatSarosh());
        return statistics;
    }

    /**
     * @return Statistic title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * updates the statistic and returns the statistic value.
     * @return String with current statistic value.
     */
    public String getCurrentStatistic() {
        return updateStatistic();
    }

    /**
     * Where statistic logic occurs.
     */
    protected abstract String updateStatistic();
}

/**
 * Average number of reviews per property
 */
class Stat1 extends Statistic {
    Stat1() {
        title = "Average number of reviews per property";
    }
    public String updateStatistic() { return averageNumberOfReviewsPerProperty();}
        private String averageNumberOfReviewsPerProperty() {
            int totalNumberOfReviews = 0;
            for(AirbnbListing listing :CurrentListings.getCurrentListings()){
                totalNumberOfReviews += listing.getNumberOfReviews();
            }
            return Integer.toString(totalNumberOfReviews / CurrentListings.getCurrentListings().size());
        }
}
/**
 * Total number of available properties.
 */
class Stat2 extends Statistic {
    Stat2() {
        title = "Total number of available properties:";
    }
    public String updateStatistic() {
        return Integer.toString(CurrentListings.getCurrentListings().size());
    }
}
/**
 * Total number of entire homes and apartments.
 */
class Stat3 extends Statistic {
    Stat3() {
        title = "the number of entire homes and apartments";
    }
    public String updateStatistic() {
        return numberOfEntireHomes();
    }
    private String numberOfEntireHomes() {
        int numberOfEntireHomes = 0;
        for(AirbnbListing listing :CurrentListings.getCurrentListings()){
            if (listing.getRoom_type().equals("Entire home/apt")){
                numberOfEntireHomes++;
            }
        }
        return Integer.toString(numberOfEntireHomes);
    }
}
/**
 * The priciest neighbourhood in the database. taking into account the minimum number of nights per property
 */
class Stat4 extends Statistic {
    Stat4() {
        title = "the priciest neighbourhood:";
    }
    public String updateStatistic() {
        return priciestNeighbourhood();
    }
    private String priciestNeighbourhood() {
        Set<String> neighbourhoods = CurrentListings.getCurrentListings().stream().map(AirbnbListing::getNeighbourhood).collect(Collectors.toSet()); //creates a Set of all neighbourhoods in the database.
        String priciestNeighbourhood = "";
        for(String neighbourhood :neighbourhoods) {
            if((neighbourhoodPrice(neighbourhood) > neighbourhoodPrice(priciestNeighbourhood))) {
                priciestNeighbourhood = neighbourhood;
            }
        }
        return priciestNeighbourhood;
    }

    /**
     * *
     * @param neighbourhood a neighbourhood string form the database
     * @return average price of a property in the neighbourhood accounting for minimum number of nights per property.
     */
    private int neighbourhoodPrice(String neighbourhood) {
        int totalPriceOfAllProperties = 0;
        int numberOfPropertiesInNeighbourhood =0;
        for(AirbnbListing listing :CurrentListings.getCurrentListings()) {
            if (listing.getNeighbourhood() .contains(neighbourhood)) {
                totalPriceOfAllProperties += (listing.getPrice() * listing.getMinimumNights());
                numberOfPropertiesInNeighbourhood++;
            }
        }
        return totalPriceOfAllProperties /numberOfPropertiesInNeighbourhood;
    }

}

/**
 * The cheapest neighbourhood in the database. taking into account the minimum number of nights per property
 */
class StatAns extends Statistic {
    StatAns() {
        title = "The Cheapest neighbourhood:";
    }
    public String updateStatistic() {
        return cheapestNeighbourhood();
    }
    private String cheapestNeighbourhood() {
        Set<String> neighbourhoods = CurrentListings.getCurrentListings().stream().map(AirbnbListing::getNeighbourhood).collect(Collectors.toSet());
        String cheapestNeighbourhood = "";
        for(String neighbourhood :neighbourhoods) {
            if((neighbourhoodPrice(neighbourhood) < neighbourhoodPrice(cheapestNeighbourhood))) {
                cheapestNeighbourhood = neighbourhood;
            }
        }
        return cheapestNeighbourhood;
    }
    /**
     * *
     * @param neighbourhood a neighbourhood string form the database
     * @return average price of a property in the neighbourhood accounting for minimum number of nights per property.
     */
    private int neighbourhoodPrice(String neighbourhood) {
        int totalPriceOfAllProperties = 0;
        int numberOfPropertiesInNeighbourhood =0;
        for(AirbnbListing listing :CurrentListings.getCurrentListings()) {
            if (listing.getNeighbourhood() .contains(neighbourhood)) {
                totalPriceOfAllProperties += (listing.getPrice() * listing.getMinimumNights());
                numberOfPropertiesInNeighbourhood++;
            }
        }
        return totalPriceOfAllProperties /numberOfPropertiesInNeighbourhood;
    }
}
/**
 * Calculates average transit time to Strand campus using the Google maps API.
 */
class StatAlex extends Statistic {
    StatAlex() {
        title = "Average transit time to KCL Strand Campus:";
    }
    public String updateStatistic() {
        try {
            return AirbnbAPI.getAverageTransitTime(CurrentListings.getCurrentListings());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return "Error";
        }
    }
}
/**
 *  The Host with the most amount of reviews;
 */
class StatSarosh extends Statistic {
    StatSarosh() {
        title = "Most popular host:";
    }
    public String updateStatistic() { return mostPopularHost(); }
    private String mostPopularHost(){
        String mostPopularHost = null;
        int highestReviewPerMonth = -1;
        for(AirbnbListing listing :CurrentListings.getCurrentListings()){
            if(listing.getNumberOfReviews() > highestReviewPerMonth){
                mostPopularHost = listing.getHost_name();
                highestReviewPerMonth = listing.getNumberOfReviews();
            }
        }
        return mostPopularHost;
    }
}
/**
 * @return most popular host name in the priciest neighborhood
 */
class StatFirat extends Statistic {
    StatFirat() {
        title = "The most popular host in the priciest neighborhood:";
    }
    public String updateStatistic() {
        String priciestNeighborhood= new Stat4().updateStatistic();
        AirbnbListing listing= CurrentListings.getCurrentListings().stream().filter((l)->l.getNeighbourhood().equals(priciestNeighborhood)).reduce((acc,nl)->{
            if (acc.getNumberOfReviews()<nl.getNumberOfReviews()){
                acc=nl;
            }
            return acc;

        }).orElse(null);
        try{
            return listing.getHost_name();
        }
        catch (NullPointerException e){
            return "None";
        }
    }
}




