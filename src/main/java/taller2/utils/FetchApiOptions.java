package taller2.utils;

import com.google.gson.Gson;

import javax.swing.text.html.parser.Entity;
import java.util.HashMap;
import java.util.Map;

public class FetchApiOptions {
  private Map<String, String> headers = new HashMap<>();
  private String body = null;
  private String method = "GET";
  
  public FetchApiOptions() {
  }
  
  public FetchApiOptions(String method, Object body) {
    this.method = method;
    this.body = new Gson().toJson(body);
  }
  
  public FetchApiOptions(Map<String, String> headers, String method, Object body) {
    this.headers = headers;
    this.method = method;
    this.body = new Gson().toJson(body);
  }
  
  public Map<String, String> getHeaders() {
    return headers;
  }
  public void addHeader(String key, String value) {
    this.headers.put(key, value);
  }
  
  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }
  
  public String getBody() {
    return body;
  }
  
  public void setBody(Object body) {
    this.body = new Gson().toJson(body);
  }
  
  public String getMethod() {
    return method;
  }
  
  public void setMethod(String method) {
    this.method = method;
  }
}