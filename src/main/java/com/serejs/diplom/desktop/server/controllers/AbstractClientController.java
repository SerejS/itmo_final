package com.serejs.diplom.desktop.server.controllers;


import com.google.gson.JsonArray;
import com.serejs.diplom.desktop.text.container.JsonSerializable;
import com.serejs.diplom.desktop.ui.states.State;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;


public abstract class AbstractClientController {
    protected static final String baseUrl = "http://localhost:8080";

    private static String request(String method, String endpoint, List<NameValuePair> params, String payload)
            throws IOException, HttpException, URISyntaxException {

        HttpClient httpclient = HttpClients.createDefault();

        var requestBuilder = new URIBuilder(baseUrl + endpoint);

        //Выбор метода запроса
        HttpRequestBase request;
        switch (method) {
            case "GET" -> {
                if (params != null) requestBuilder.addParameters(params);
                request = new HttpGet(requestBuilder.build());
            }
            case "POST" -> {
                request = new HttpPost(requestBuilder.build());
                System.out.println(payload);
                ((HttpPost) request).setEntity(new StringEntity(payload, StandardCharsets.UTF_8));
            }
            case "DELETE" -> request = new HttpDelete(requestBuilder.build());

            default -> throw new MethodNotSupportedException("Метод не поддерживается");
        }

        // Заголовок запроса
        String encoding = getEncoding();
        request.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");

        HttpResponse response = httpclient.execute(request);

        var responseCode = response.getStatusLine().getStatusCode();
        if (responseCode == HttpStatus.SC_UNAUTHORIZED)
            throw new HttpException("Ошибка авторизации. " + responseCode);

        var responseEntity = response.getEntity();
        if (responseEntity == null) return String.valueOf(responseCode);

        try (var is = response.getEntity().getContent()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder jsonText = new StringBuilder();

            int cp;
            while ((cp = rd.read()) != -1)
                jsonText.append((char) cp);

            return jsonText.toString();
        }
    }

    protected static String getRequest(String endpoint, List<NameValuePair> params)
            throws HttpException, IOException, URISyntaxException {
        return request("GET", endpoint, params, null);
    }

    protected static String postRequest(String endpoint, JsonSerializable jsonObj)
            throws HttpException, IOException, URISyntaxException {
        return request("POST", endpoint, null, jsonObj.toJson().toString());
    }

    protected static void postRequest(String endpoint, List<? extends JsonSerializable> jsonList)
            throws HttpException, IOException, URISyntaxException {
        var jsonArray = new JsonArray();
        jsonList.forEach(obj -> jsonArray.add(obj.toJson()));

        request("POST", endpoint, null, jsonArray.toString());
    }

    protected static void deleteRequest(String endpoint)
            throws HttpException, IOException, URISyntaxException {
        request("DELETE", endpoint, null, null);
    }


    protected static String getEncoding() {
        var user = State.getUser();
        var name = user.getUsername();
        var pass = user.getPassword();
        return Base64.getEncoder().encodeToString((name + ":" + pass).getBytes(StandardCharsets.UTF_8));
    }
}
