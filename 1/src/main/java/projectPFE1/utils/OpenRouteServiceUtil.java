package projectPFE1.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class OpenRouteServiceUtil {

    @Value("${ors.api.key}") // Inject the API key from application.properties
    private String orsApiKey;

    private static final String ORS_GEOCODE_URL = "https://api.openrouteservice.org/geocode/autocomplete";
    private static final String ORS_GEOCODE_SEARCH_URL = "https://api.openrouteservice.org/geocode/search";


    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String[] getLocationSuggestions(String query) {
        try {
            // Build the API URL
            String url = ORS_GEOCODE_URL + "?api_key=" + orsApiKey + "&text=" + query;
            System.out.println("Calling ORS API: " + url); // Log the URL

            // Call the ORS API
            String jsonResponse = restTemplate.getForObject(url, String.class);
            System.out.println("ORS API Response: " + jsonResponse); // Log the response

            // Parse the JSON response
            Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, Map.class);

            // Extract the "features" array
            List<Map<String, Object>> features = (List<Map<String, Object>>) responseMap.get("features");

            // Extract the "label" field from each feature and convert to a String array
            return features.stream()
                    .map(feature -> (String) ((Map<String, Object>) feature.get("properties")).get("label"))
                    .toArray(String[]::new); // Convert List<String> to String[]
        } catch (Exception e) {
            e.printStackTrace();
            return new String[0]; // Return an empty array if there's an error
        }
    }




    private String getIso2CountryCode(String countryName) {
        try {
            // Build the API URL to search for the country
            String url = ORS_GEOCODE_SEARCH_URL + "?api_key=" + orsApiKey + "&text=" + countryName + "&layers=country";
            System.out.println("Calling ORS API to fetch ISO2 code for country: " + url); // Log the URL

            // Call the ORS API
            String jsonResponse = restTemplate.getForObject(url, String.class);
            System.out.println("ORS API Response for ISO2 code: " + jsonResponse); // Log the response

            // Parse the JSON response
            Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, Map.class);

            // Extract the "features" array
            List<Map<String, Object>> features = (List<Map<String, Object>>) responseMap.get("features");

            // Extract the ISO2 code from the first feature (if available)
            if (!features.isEmpty()) {
                Map<String, Object> properties = (Map<String, Object>) features.get(0).get("properties");
                return (String) properties.get("country_a"); // "country_a" contains the ISO2 code
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if the ISO2 code is not found
    }

    /**
     * Get country suggestions based on a query.
     *
     * @param query The search term for the country (e.g., "Fra" for France).
     * @return Array of country suggestions.
     */
    public String[] getCountrySuggestions(String query) {
        try {
            // Build the API URL for country suggestions
            String url = ORS_GEOCODE_URL + "?api_key=" + orsApiKey + "&text=" + query + "&layers=country";
            System.out.println("Calling ORS API for country suggestions: " + url); // Log the URL

            // Call the ORS API
            String jsonResponse = restTemplate.getForObject(url, String.class);
            System.out.println("ORS API Response for country suggestions: " + jsonResponse); // Log the response

            // Parse the JSON response
            Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, Map.class);

            // Extract the "features" array
            List<Map<String, Object>> features = (List<Map<String, Object>>) responseMap.get("features");

            // Extract the "label" field from each feature and convert to a String array
            return features.stream()
                    .map(feature -> (String) ((Map<String, Object>) feature.get("properties")).get("label"))
                    .toArray(String[]::new); // Convert List<String> to String[]
        } catch (Exception e) {
            e.printStackTrace();
            return new String[0]; // Return an empty array if there's an error
        }
    }

    /**
     * Get city suggestions based on a query and country.
     *
     * @param query       The search term for the city (e.g., "Par" for Paris).
     * @param countryName The selected country (e.g., "France").
     * @return Array of city suggestions.
     */
    public String[] getCitySuggestions(String query, String countryName) {
        try {
            // Fetch the ISO2 code for the country
            String iso2CountryCode = getIso2CountryCode(countryName);
            if (iso2CountryCode == null) {
                throw new IllegalArgumentException("Invalid country name: " + countryName);
            }

            // Build the API URL for city suggestions, filtered by country
            String url = ORS_GEOCODE_SEARCH_URL + "?api_key=" + orsApiKey + "&text=" + query + "&boundary.country=" + iso2CountryCode + "&layers=locality";
            System.out.println("Calling ORS API for city suggestions: " + url); // Log the URL

            // Call the ORS API
            String jsonResponse = restTemplate.getForObject(url, String.class);
            System.out.println("ORS API Response for city suggestions: " + jsonResponse); // Log the response

            // Parse the JSON response
            Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, Map.class);

            // Extract the "features" array
            List<Map<String, Object>> features = (List<Map<String, Object>>) responseMap.get("features");

            // Extract the "label" field from each feature and convert to a String array
            return features.stream()
                    .map(feature -> (String) ((Map<String, Object>) feature.get("properties")).get("label"))
                    .toArray(String[]::new); // Convert List<String> to String[]
        } catch (Exception e) {
            e.printStackTrace();
            return new String[0]; // Return an empty array if there's an error
        }
    }

    /**
     * Get address suggestions based on a query, city, and country.
     *
     * @param query   The search term for the address (e.g., "123 Main St").
     * @param city    The selected city (e.g., "Paris").
     * @param country The selected country (e.g., "France").
     * @return Array of address suggestions.
     */
    public String[] getAddressSuggestions(String query, String city, String country) {
        try {
            // Fetch the ISO2 code for the country
            String iso2CountryCode = getIso2CountryCode(country);
            if (iso2CountryCode == null) {
                throw new IllegalArgumentException("Invalid country name: " + country);
            }

            // Build the API URL for address suggestions, filtered by city and country
            String url = ORS_GEOCODE_SEARCH_URL + "?api_key=" + orsApiKey + "&text=" + query + "&boundary.locality=" + city + "&boundary.country=" + iso2CountryCode + "&layers=address";
            System.out.println("Calling ORS API for address suggestions: " + url); // Log the URL

            // Call the ORS API
            String jsonResponse = restTemplate.getForObject(url, String.class);
            System.out.println("ORS API Response for address suggestions: " + jsonResponse); // Log the response

            // Parse the JSON response
            Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, Map.class);

            // Extract the "features" array
            List<Map<String, Object>> features = (List<Map<String, Object>>) responseMap.get("features");

            // Extract the "label" field from each feature and convert to a String array
            return features.stream()
                    .map(feature -> (String) ((Map<String, Object>) feature.get("properties")).get("label"))
                    .toArray(String[]::new); // Convert List<String> to String[]
        } catch (Exception e) {
            e.printStackTrace();
            return new String[0]; // Return an empty array if there's an error
        }
    }
}