package cc.gozal.slack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {

    @Deprecated
    final private static String END_POINT = "https://slack.com/api/chat.command";
    final private static String REQUEST_METHOD = "POST";
    final private static String AUTHORIZATION = "Authorization";
    final private static String CONTENT_TYPE = "Content-Type";
    final private static String APPLICATION_JSON = "application/json; charset=utf-8";

    public static void main(String[] args) {
        try {
            final String BEARER_AUTHORIZATION = String.format("Bearer %s", args[0]);
            URL url = new URL(END_POINT);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(REQUEST_METHOD);
            conn.setRequestProperty(AUTHORIZATION, BEARER_AUTHORIZATION);
            conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
            conn.setDoOutput(true);

            String input = String.format("{\"channel\": \"%s\",\"command\":\"%s\",\"text\":\"%s\"}", args[1], args[2], args[3]);

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            System.out.println(String.format("POST Response Code : %s", responseCode));
            System.out.println(String.format("POST Response Message : %s", conn.getResponseMessage()));
            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in .readLine()) != null) {
                    response.append(inputLine);
                } in .close();
                // print result
                System.out.println(response.toString());
            } else {
                System.out.println("Not Working");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
