package taller2.utils;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import taller2.DTOs.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Fetch {
  
  private String url;
  private String body;
  private String content;
  private int responseCode;
  private String responseMessage;
  private static String prefix = System.getProperty("API_URL");
  
  public Fetch(){}
  public Fetch(String url) {
    this.url = url.replace(" ", "%20");
  }
  
  public Fetch(String url, Object body) {
    this.url = url.replace(" ", "%20");
    this.body = new Gson().toJson(body);
  }
  
  public Fetch Get() throws IOException {
    try {
      HttpClient client = HttpClients.createDefault();
  
      HttpRequest request = new HttpGet(this.prefix + this.url);
  
      HttpResponse response = client.execute((HttpUriRequest) request);
      String responseString = new BasicResponseHandler().handleResponse(response);
  
      this.responseCode = response.getStatusLine().getStatusCode();
      this.responseMessage = response.getStatusLine().getReasonPhrase();
      if (this.responseCode != 200 && this.responseCode != 201) {
        throw new RuntimeException(this.responseMessage);
      }
      this.content = responseString;
    } catch (HttpResponseException e) {
      this.responseCode = e.getStatusCode();
      this.responseMessage = e.getMessage();
      throw new RuntimeException(this.responseMessage);
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
    this.url = url.replace(" ", "%20");
    this.content = null;
    this.responseCode = 0;
    this.responseMessage = null;
    return this;
  }
  public Fetch Set(String url, Object body) {
    this.url = url.replace(" ", "%20");
    this.body = new Gson().toJson(body);
    this.content = null;
    return this;
  }
  public String getContent() {
    return this.content;
  }
  
  public UsuarioDTO getUsuario() throws JsonSyntaxException {
    return new Gson().fromJson(this.content, UsuarioDTO.class);
  }
  
  //convert content json to map
  public Map<String, UsuarioDTO> getMapUsuario() throws JsonSyntaxException {
    Type type = new TypeToken<Map<String, UsuarioDTO>>(){}.getType();
    return new Gson().fromJson(this.content, type);
  }
  
  public EspectaculoDTO getEspectaculo() throws JsonSyntaxException {
    return new Gson().fromJson(this.content, EspectaculoDTO.class);
  }
  
  //convert content json to map
  public Map<String, EspectaculoDTO> getMapEspectaculo() throws JsonSyntaxException {
    Type type = new TypeToken<Map<String, EspectaculoDTO>>(){}.getType();
    return new Gson().fromJson(this.content, type);
  }
  
  public FuncionDTO getFuncion() throws JsonSyntaxException {
    return new Gson().fromJson(this.content, FuncionDTO.class);
  }
  
  //convert content json to map
  public Map<String, FuncionDTO> getMapFuncion() throws JsonSyntaxException {
    Type type = new TypeToken<Map<String, FuncionDTO>>(){}.getType();
    return new Gson().fromJson(this.content, type);
  }
  
  public PlataformaDTO getPlataforma() throws JsonSyntaxException {
    return new Gson().fromJson(this.content, PlataformaDTO.class);
  }
  
  //convert content json to map
  public Map<String, PlataformaDTO> getMapPlataforma() throws JsonSyntaxException {
    Type type = new TypeToken<Map<String, PlataformaDTO>>(){}.getType();
    return new Gson().fromJson(this.content, type);
  }
  
  public PaqueteDTO getPaquete() throws JsonSyntaxException {
    return new Gson().fromJson(this.content, PaqueteDTO.class);
  }
  
  //convert content json to map
  public Map<String, PaqueteDTO> getMapPaquete() throws JsonSyntaxException {
    Type type = new TypeToken<Map<String, PaqueteDTO>>(){}.getType();
    return new Gson().fromJson(this.content, type);
  }
  
  public CategoriaDTO getCategoria() throws JsonSyntaxException {
    return new Gson().fromJson(this.content, CategoriaDTO.class);
  }
  
  //convert content json to map
  public Map<String, CategoriaDTO> getMapCategoria() throws JsonSyntaxException {
    Type type = new TypeToken<Map<String, CategoriaDTO>>(){}.getType();
    return new Gson().fromJson(this.content, type);
  }
  
  public EspectadorRegistradoAFuncionDTO getEspectadorRegistradoAFuncion() throws JsonSyntaxException {
    return new Gson().fromJson(this.content, EspectadorRegistradoAFuncionDTO.class);
  }
  
  //convert content json to map
  public Map<String, EspectadorRegistradoAFuncionDTO> getMapEspectadorRegistradoAFuncion() throws JsonSyntaxException {
    Type type = new TypeToken<Map<String, EspectadorRegistradoAFuncionDTO>>(){}.getType();
    return new Gson().fromJson(this.content, type);
  }
  
  public String getString() {
    return new Gson().fromJson(this.content, String.class);
  }
  
  
}
