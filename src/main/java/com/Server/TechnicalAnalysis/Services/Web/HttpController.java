package com.Server.TechnicalAnalysis.Services.Web;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class HttpController {
    private final Logger logger = LoggerFactory.getLogger(HttpController.class);

    public static class HttpRequest {
        private String url;
        private Map<String, Object> params = new HashMap<>();
        private String[] auth = new String[2];

        public String getUrl() {
            return url;
        }

        public Map<String, Object> getParams() {
            return params;
        }

        public HttpRequest setUrl(String url) {
            this.url = url;
            return this;
        }

        public HttpRequest setParams(Map<String, Object> params) {
            this.params = params;
            return this;
        }

        public HttpRequest setParam(String key, String value) {
            this.params.put(key, value);
            return this;
        }

        public String getUsername() {
            return this.auth[0];
        }

        public String getPassword() {
            return this.auth[1];
        }

        public HttpRequest setAuth(String username, String password) {
            this.auth[0] = username;
            this.auth[1] = password;
            return this;
        }
    }

    public HttpResponse<JsonNode> getRequest(HttpRequest httpRequest) {
        try {
            return Unirest
                    .get(httpRequest.getUrl())
                    .queryString(httpRequest.getParams())
                    .basicAuth(httpRequest.getUsername(), httpRequest.getPassword())
                    .asJson();
        } catch (UnirestException e) {
            this.logger.error("Get request failed: {}", e.getMessage());
        }
        return null;
    }

    public HttpResponse<JsonNode> postRequest(HttpRequest httpRequest) {
        try {
            return Unirest
                    .post(httpRequest.getUrl())
                    .fields(httpRequest.getParams())
                    .basicAuth(httpRequest.getUsername(), httpRequest.getPassword())
                    .asJson();
        } catch (UnirestException e) {
            this.logger.error("Post request failed: {}", e.getMessage());
        }
        return null;
    }
}
