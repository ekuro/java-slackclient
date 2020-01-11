package jp.ekuro.slack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {

    final private static String COMMAND_END_POINT = "https://slack.com/api/chat.command";
    final private static String MESSAGE_END_POINT = "https://slack.com/api/chat.postMessage";
    final private static String REQUEST_METHOD = "POST";
    final private static String AUTHORIZATION = "Authorization";
    final private static String CONTENT_TYPE = "Content-Type";
    final private static String APPLICATION_JSON = "application/json";
    final private static String UTF8 = "UTF-8";
    final private static boolean AS_USER = true;

    public static void main(String[] args) {
        try {
            assert args.length >= 1 : "token is required.";
            assert args.length >= 2 : "channel is required.";
            assert args.length >= 3 : "text or command is required.";

            // TODO: fix args variables
            HttpURLConnection conn = startConnection(args);
            write(conn, isCommand(args)? commandInput(args) : textInput(args));
            printResult(conn);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String commandInput(String[] args) {
        String channel = String.format("\"channel\": \"%s\",", args[1]);
        String command = String.format("\"command\": \"%s\"", args[2]);
        String text = isCommandAndText(args)? String.format(",\"text\": \"%s\"", args[3]) : "";

        return String.format("{%s%s%s}", channel, command, text);
    }

    private static String textInput(String[] args) {
        String channel = String.format("\"channel\": \"%s\",", args[1]);
        String text = String.format("\"text\": \"%s\",", args[2]);
        String asUser = String.format("\"as_user\": \"%s\"", AS_USER);

        return String.format("{%s%s%s}", channel, text, asUser);
    }

    private static HttpURLConnection startConnection(String[] args) throws IOException {
        final String BEARER_AUTHORIZATION = String.format("Bearer %s", args[0]);
        URL url = new URL(endPoint(args));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(REQUEST_METHOD);
        conn.setRequestProperty(AUTHORIZATION, BEARER_AUTHORIZATION);
        conn.setRequestProperty(CONTENT_TYPE, String.format("%s; charset=%s", APPLICATION_JSON, UTF8));
        conn.setDoOutput(true);
        return conn;
    }

    private static void write(HttpURLConnection conn, String input) throws IOException {
        OutputStream os = conn.getOutputStream();
        os.write(input.getBytes());
        os.flush();
        os.close();
    }

    private static void printResult(HttpURLConnection conn) throws IOException {
        int responseCode = conn.getResponseCode();
        System.out.println(String.format("POST Response Code : %s", responseCode));
        System.out.println(String.format("POST Response Message : %s", conn.getResponseMessage()));
        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in .readLine()) != null) {
                response.append(inputLine);
            } in .close();
            System.out.println(response.toString());
        } else {
            System.out.println("Not Working");
        }
    }

    private static String endPoint(String[] args) {
        return isCommand(args)? COMMAND_END_POINT : MESSAGE_END_POINT;
    }

    private static boolean isCommand(String[] args) {
        return args[2].startsWith("/");
    }

    private static boolean isCommandAndText(String[] args) {
        return args.length >= 4;
    }

}
