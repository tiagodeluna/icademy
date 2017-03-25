package br.com.tiagoluna.agenda;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by tiago on 24/03/2017.
 */

public class WebClient {

    public String post(String json) {
        try {
            URL url = new URL("https://www.caelum.com.br/mobile");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-type", "application/json");
            con.setRequestProperty("Accept", "application/json");

            //Sends content
            con.setDoOutput(true);
            PrintStream output = new PrintStream(con.getOutputStream());
            output.println(json);

            //Opens connection
            con.connect();
            //Receives the response
            Scanner scanner = new Scanner(con.getInputStream());
            String response = scanner.next();

            return response;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
