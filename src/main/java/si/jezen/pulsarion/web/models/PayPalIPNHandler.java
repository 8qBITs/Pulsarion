package si.jezen.pulsarion.web.models;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import si.jezen.pulsarion.Pulsarion;

import javax.net.ssl.SSLSocketFactory;

public class PayPalIPNHandler {

    public static void handleIPN(String postreq, Connection conn, Pulsarion plugin) {
        try {
            URL url = new URL("https://ipnpb.paypal.com/cgi-bin/webscr");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set up the connection properties
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "close");
            connection.setDoOutput(true);

            // Send the request
            try (OutputStream os = connection.getOutputStream()) {
                os.write(postreq.getBytes());
            }

            // Read the response
            try (InputStream is = connection.getInputStream();
                 InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader br = new BufferedReader(isr)) {

                String line;
                StringBuilder response = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                // Check the response from PayPal
                if (response.toString().startsWith("VERIFIED")) {
                    // Extract the "custom" field from the original postreq
                    String customField = extractCustomField(postreq);

                    Purchase.completePurchase(conn, customField, plugin);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String extractCustomField(String postreq) {
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(postreq, JsonObject.class);
        return json.get("custom").getAsString();
    }
}
