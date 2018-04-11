import javafx.scene.image.Image;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
/**
 * Where API calls are made to external services.
 * Everything is static as this is a utility class.
 *
 * @author Alexander Davis
 * @version 28.03.2018
 */
public class AirbnbAPI {
    /**
     *
     * @param id of listing to get reviews for.
     * @return List of reviews for given listing.
     * @throws Exception if no reviews are found/listing not available.
     */
    public static List<String> getReviewComments(String id) throws Exception {
        List<String> reviewComments = new ArrayList<>();
        String jsonString = getHTTP("https://api.airbnb.com/v2/reviews?client_id=d306zoyjsyarp7ifhu67rjxn52tv0t20&role=all&listing_id=" + id);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
        JSONObject metadata = (JSONObject) jsonObject.get("metadata");
        Long reviewCount = (Long) metadata.get("reviews_count");
        //If no reviews found throw exception.
        if (reviewCount == 0) {
            throw new Exception("No reviews found.");
        } else {
            //For each review found add it to return list.
            JSONArray reviews = (JSONArray) jsonObject.get("reviews");
            for (int i = 0; i < reviews.size() - 1; i++) {
                JSONObject myObj = (JSONObject) reviews.get(i);
                reviewComments.add((String) myObj.get("comments"));
            }
        }
        return reviewComments;
    }

    /**
     *
     * @param id of listing to get image for.
     * @return Image object of image of listing given
     * @throws Exception if no image found.
     */
    public static Image getListingImage(String id) throws Exception {
        String jsonString = getHTTP("https://api.airbnb.com/v2/listings/" + id + "?_format=v1_legacy_for_p3&client_id=3092nxybyb0otqw18e8nh5nty");
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
        JSONObject listing = (JSONObject) jsonObject.get("listing");
        String thumbnail = (String) listing.get("thumbnail_url");
        if (thumbnail == null) {
            throw new Exception("No image.");
        } else {
            thumbnail = thumbnail.replace("?aki_policy=small", "");
            return new Image(thumbnail);
        }
    }

    /**
     * @param listings listings to test.
     * @return average transit time in minutes for first 5 of chosen listings.
     * @throws Exception if network error occurs.
     */
    public static String getAverageTransitTime(ArrayList<AirbnbListing> listings) throws Exception {
        ArrayList<AirbnbListing> listingsToTest= new ArrayList<>();
        for(int i=0;i<listings.size();i=i+listings.size()/24) {
            listingsToTest.add(listings.get(i));
        }
        String origins = listingsToTest.stream().map(l -> l.getLatitude() + "," + l.getLongitude()).collect(Collectors.joining("|"));
        String jsonString = getHTTPS("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + origins + "&destinations=KCL&mode=transit&key=AIzaSyBucLEeuQwipnHrwPBfqRCs46wSExF91Dc");
        if (jsonString.contains("OVER_QUERY_LIMIT")) {
            return "Query limit exceeded";
        }
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(jsonString);
        JSONArray rows = (JSONArray) jsonObject.get("rows");
        int returnValues = 0;
        int totalValues = 0;
        for (Object o : rows) {
            JSONObject object = (JSONObject) o;
            JSONArray elements = (JSONArray) object.get("elements");
            for (Object element : elements) {
                JSONObject elementJson = (JSONObject) element;
                try {
                    JSONObject duration = (JSONObject) elementJson.get("duration");
                    Long time = (Long) duration.get("value");
                    //For each value add it to returnValues and increment totalValues.
                    returnValues += time;
                    totalValues++;
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }

        }
        if (totalValues == 0) {
            return "No results found.";
        }
        //Find average
        returnValues = returnValues / totalValues;
        //Convert to minutes.
        return returnValues / 60 + " mins";
    }

    /**
     *
     * @param urlToRead url to get JSON file from.
     * @return string with json.
     * @throws Exception if httpconnection error occurs.
     */
    private static String getHTTP(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }

    /**
     *
     * @param urlToRead url to get json file for.
     * @return string with json.
     * @throws Exception if https error occurs.
     */
    private static String getHTTPS(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }
}