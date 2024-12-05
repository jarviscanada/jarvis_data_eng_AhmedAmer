package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.Quote;
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
import java.time.format.DateTimeFormatter;

public class QuoteHttpHelper {
    private final String apiKey = System.getenv("ALPHA_VANTAGE_KEY");
    private final OkHttpClient httpClient = new OkHttpClient();
    final Logger errorLogger = LoggerFactory.getLogger("errorLogger");

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

        try (Response response = httpClient.newCall(request).execute()) {

            ObjectMapper m = new ObjectMapper();
            JsonNode jsonBody = m.readTree(response.body().string());
            JsonNode quoteNode = jsonBody.get("Global Quote");
            quote = m.convertValue(quoteNode, Quote.class);

            if (quote == null) {
                errorLogger.error("API returned null quote possibly because limit of calls has been reached.");
            } else {
                quote.setTimestamp(quoteTimestamp());
            }

        } catch (IOException e) {
            errorLogger.error("Error fetching quote info from API call", e);
        }

        return quote;
    }

    private Timestamp quoteTimestamp() {
        return new Timestamp(Instant.now().toEpochMilli());
    }

}
