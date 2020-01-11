package jp.ekuro.slack;

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
    final private static String APPLICATION_JSON = "application/json";
    final private static String UTF8 = "UTF-8";

    public static void main(String[] args) {
        try {
            assert args.length >= 3 : "token or channel or command are required.";

            final String BEARER_AUTHORIZATION = String.format("Bearer %s", args[0]);
            URL url = new URL(END_POINT);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(REQUEST_METHOD);
            conn.setRequestProperty(AUTHORIZATION, BEARER_AUTHORIZATION);
            conn.setRequestProperty(CONTENT_TYPE, String.format("%s; charset=%s", APPLICATION_JSON, UTF8));
            conn.setDoOutput(true);

            String channel = String.format("\"channel\": \"%s\",", args[1]);
            String command = String.format("\"command\": \"%s\"", args[2]);
            String text = hasText(args)? String.format(",\"text\": \"%s\"", args[3]) : "";

            String input = String.format("{%s%s%s}", channel, command, text);

            System.out.println(input);
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

    private static boolean hasText(String[] args) {
        return args.length >= 4;
    }

}
