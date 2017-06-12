/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fintech.project.momo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;



/**
 *
 * @author taiker
 */
public class HttpConnection {
    private final String USER_AGENT = "Mozilla/5.0";

    // HTTP POST request
    void sendPost() throws Exception {

        String url = "https://www.google.com.tw/search?";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        con.setDoInput(true);
        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        //con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        con.setRequestProperty("Accept-Language",
                "zh-tw,en-us;q=0.7,en;q=0.3");
        con.setRequestProperty("Accept-Charse",
                "Big5,utf-8;q=0.7,*;q=0.7");
        String urlParameters = "username='taiker'&password='1qaz2wsx'";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print result
        System.out.println(response.toString());

    }

}
