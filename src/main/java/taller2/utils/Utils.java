package taller2.utils;

import jakarta.ws.rs.core.Response;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.net.URI;
import java.util.Map;
public class Utils {
  
  private static String prefix = "http://localhost:8080/api";
  public static Response FetchApi(String url) {
    FetchApiOptions options = new FetchApiOptions();
    options.addHeader("Content-Type", "application/json");
    CloseableHttpClient client = HttpClients.createDefault();
    
    HttpRequest request;
    switch (options.getMethod()) {
      case "POST":
        HttpPost post = new HttpPost(prefix + url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("body", options.getBody(), ContentType.TEXT_PLAIN);
        HttpEntity multipart = builder.build();
        post.setEntity(multipart);
        request = post;
        break;
      case "GET":
      default:
        request = new HttpGet(url);
        break;
    }
    
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
  public static Response FetchApi(String url, FetchApiOptions options) {
    CloseableHttpClient client = HttpClients.createDefault();
    options.addHeader("Content-Type", "application/json");
    
    switch (options.getMethod()) {
      case "POST":
        HttpPost post = new HttpPost(prefix + url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("user", options.getBody(), ContentType.TEXT_PLAIN);
        HttpEntity multipart = builder.build();
        post.setEntity(multipart);
        
        for (Map.Entry<String, String> entry : options.getHeaders().entrySet()) {
          post.addHeader(entry.getKey(), entry.getValue());
        }
        
        try {
          CloseableHttpResponse response = client.execute(post);
          client.close();
          String responseString = new BasicResponseHandler().handleResponse(response); //TOFIX: Error aqui nose porque
          return Response.status(response.getStatusLine().getStatusCode()).entity(responseString).build();
        } catch (Exception e) {
          e.printStackTrace();
          return Response.status(500).entity(e.getMessage()).build();
        }
      case "GET":
      default:
        HttpGet get = new HttpGet(url);
        
        for (Map.Entry<String, String> entry : options.getHeaders().entrySet()) {
          get.addHeader(entry.getKey(), entry.getValue());
        }
        
        try {
          CloseableHttpResponse response = client.execute(get);
          client.close();
          String responseString = new BasicResponseHandler().handleResponse(response);
          return Response.status(response.getStatusLine().getStatusCode()).entity(responseString).build();
        } catch (Exception e) {
          e.printStackTrace();
          return Response.status(500).entity(e.getMessage()).build();
        }
    }
  }
  
}
