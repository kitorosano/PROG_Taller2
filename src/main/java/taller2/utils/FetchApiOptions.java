package taller2.utils;

import java.util.HashMap;
import java.util.Map;

public class FetchApiOptions {
  private Map<String, String> headers = new HashMap<>();
  private String body = null;
  private String method = "GET";
  
  public FetchApiOptions() {
  }
  
  public FetchApiOptions(String method, String body) {
    this.method = method;
    this.body = body;
  }
  
  public FetchApiOptions(Map<String, String> headers, String method, String body) {
    this.headers = headers;
    this.method = method;
    this.body = body;
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
  
  public void setBody(String body) {
    this.body = body;
  }
  
  public String getMethod() {
    return method;
  }
  
  public void setMethod(String method) {
    this.method = method;
  }
}