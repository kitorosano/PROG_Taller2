package taller2.utils;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Fetch {
  
  private String url;
  private String body = null;
  private String content = null;
  private static String prefix = "http://localhost:8081/api/";
  
  public Fetch(){}
  public Fetch(String url) {
    this.url = prefix + url;
  }
  
  public Fetch(String url, Object body) {
    this.url = url;
    this.body = new Gson().toJson(body);
  }
  
  public Fetch Get() throws IOException {
    try {
      HttpClient client = HttpClients.createDefault();
  
      HttpRequest request = new HttpGet(this.prefix + this.url);
  
      HttpResponse response = client.execute((HttpUriRequest) request);
      String responseString = new BasicResponseHandler().handleResponse(response);
  
      if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 201) {
        throw new RuntimeException(response.getStatusLine().getReasonPhrase());
      }
      this.content = responseString;
    } catch (HttpResponseException e) {
      System.out.println(e.getMessage());
    }
    return this;
  }
  
  public Fetch Post() throws IOException {
    URL url = new URL(this.prefix + this.url);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/json");
    con.setRequestProperty("Accept", "application/json");
    con.setDoOutput(true);
    
    byte[] out = this.body.getBytes(StandardCharsets.UTF_8);
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
    
    if(con.getResponseCode() != 200) {
      throw new RuntimeException(con.getResponseMessage());
    }
  
    this.content = inputLine;
    return this;
  }
  
  public Fetch Put() throws IOException {
    URL url = new URL(this.prefix + this.url);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("PUT");
    con.setRequestProperty("Content-Type", "application/json");
    con.setRequestProperty("Accept", "application/json");
    con.setDoOutput(true);

    byte[] out = this.body.getBytes(StandardCharsets.UTF_8);
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

    if(con.getResponseCode() != 200) {
      throw new RuntimeException(con.getResponseMessage());
    }

    this.content = inputLine;
    return this;
  }
  
  public Fetch Delete() throws IOException {
    URL url = new URL(this.prefix + this.url);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("DELETE");
    con.setRequestProperty("Content-Type", "application/json");
    con.setRequestProperty("Accept", "application/json");
    con.setDoOutput(true);

    byte[] out = this.body.getBytes(StandardCharsets.UTF_8);
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

    if(con.getResponseCode() != 200) {
      throw new RuntimeException(con.getResponseMessage());
    }

    this.content = inputLine;
    return this;
  }
  
  
  public String getUrl() {
    return url;
  }
  public String getBody() {
    return body;
  }
  public Fetch Set(String url) {
    this.url = url;
    this.content = null;
    return this;
  }
  public Fetch Set(String url, Object body) {
    this.url = url;
    this.body = new Gson().toJson(body);
    this.content = null;
    return this;
  }
  
  public <T> T getContent(Class<T> classOfT) throws JsonSyntaxException {
    //return new Gson().fromJson(this.content, classOfT);
    T object = new Gson().fromJson(this.content, TypeToken.get(classOfT));
    return Primitives.wrap(classOfT).cast(object);
  }
  
  //get content map
  public <T> Map<String, T> getContentMap(Class<T> classOfT) throws JsonSyntaxException {
    Type type = new TypeToken<Map<String, T>>(){}.getType();
    Map<String, T> map = new Gson().fromJson(this.content, type);
    return map;
  }
  
  public String getContentString() {
    return this.content;
  }
  
}
