package ca.jrvs.apps.stockquote.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class QuoteHttpHelper {
    private String apiKey = System.getenv("ALPHA_VANTAGE_KEY");
    private OkHttpClient httpClient = new OkHttpClient();

    final Logger infoLogger = LoggerFactory.getLogger("infoLogger");
    final Logger errorLogger = LoggerFactory.getLogger("errorLogger");

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Fetch latest quote data from Alpha Vantage endpoint
     * @param symbol symbol of stock to fetch
     * @return Quote with latest data
     * @throws IllegalArgumentException - if no data was found for the given symbol
     */
    public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException {
        Quote quote = new Quote();
        Request request = new Request.Builder()
                .url("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol="+symbol+"&datatype=json")
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            ObjectMapper m = new ObjectMapper();
            JsonNode jsonBody = m.readTree(response.body().string());
            JsonNode quoteNode = jsonBody.get("Global Quote");
            quote = m.convertValue(quoteNode, Quote.class);

            quote.setTimestamp(quoteTimestamp());

        } catch (IllegalArgumentException e) {
            errorLogger.info("API call failed, symbol invalid or no data found.", e);
        } catch (IOException e) {
            errorLogger.error("Error fetching quote info from API call", e);
            throw new RuntimeException(e);
        }

        return quote;
    }

    private Timestamp quoteTimestamp() {
        return new Timestamp(Instant.now().toEpochMilli());
    }

}
