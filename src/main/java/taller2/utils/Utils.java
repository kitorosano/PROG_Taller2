package taller2.utils;


import jakarta.ws.rs.core.Response;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
public class Utils {
  
  public static Response FetchApi(String url) {
    FetchApiOptions options = new FetchApiOptions();
    options.addHeader("Content-Type", "application/json");
    CloseableHttpClient client = HttpClients.createDefault();
    
    HttpRequest request = new HttpGet(url);
    
    for (Map.Entry<String, String> entry : options.getHeaders().entrySet()) {
      request.addHeader(entry.getKey(), entry.getValue());
    }
    
    try {
      CloseableHttpResponse response = client.execute((HttpUriRequest) request);
      client.close();
      String responseString = new BasicResponseHandler().handleResponse(response);
      return Response.status(response.getStatusLine().getStatusCode()).entity(responseString).build();
    } catch (Exception e) {
      e.printStackTrace();
      return Response.status(500).entity(e.getMessage()).build();
    }
  }
  public static Response FetchApi(String str, FetchApiOptions options) {
    switch (options.getMethod()) {
      default:
      case "POST": {
        try {
          URL url = new URL(str);
          HttpURLConnection con = (HttpURLConnection) url.openConnection();
          con.setRequestMethod("POST");
          con.setRequestProperty("Content-Type", "application/json");
          con.setRequestProperty("Accept", "application/json");
          con.setDoOutput(true);
  
          byte[] out = options.getBody().getBytes(StandardCharsets.UTF_8);
          int length = out.length;
  
          con.setFixedLengthStreamingMode(length);
          con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
          con.connect();
          try(OutputStream os = con.getOutputStream()) {
            os.write(out);
          }
  
          BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
          String inputLine;
          while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
          }
          in.close();
          con.disconnect();
          return Response.status(con.getResponseCode()).entity(inputLine).build();
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println("Error");
          return Response.status(500).entity(e.getMessage()).build();
        }
      }
    }
  }
  
}
