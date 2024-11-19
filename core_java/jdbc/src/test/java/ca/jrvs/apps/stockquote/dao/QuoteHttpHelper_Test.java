package ca.jrvs.apps.stockquote.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;

public class QuoteHttpHelper_Test {
    QuoteHttpHelper helper = new QuoteHttpHelper();

    @Test
    void test_fetchQuoteInfo() {
        Quote quote = helper.fetchQuoteInfo("IBM");
        System.out.println(quote);
    }

//    @Test
//    void test_isolatedHttpRequest() {
//        Quote quote = new Quote();
//        String apiKey = System.getenv("ALPHA_VANTAGE_KEY");
//        OkHttpClient client = new OkHttpClient();
//        String symbol = "IBM";
//        Request request = new Request.Builder()
//                .url("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol="+symbol+"&datatype=json")
//                .header("X-RapidAPI-Key", apiKey)
//                .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
//                .build();
//        try(Response response = client.newCall(request).execute()) {
//            System.out.println(response.body().string());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
}
