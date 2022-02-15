package com.serejs.diplom.desktop.utils;

import com.serejs.diplom.desktop.enums.SourceType;
import com.serejs.diplom.desktop.text.container.Source;
import com.serejs.diplom.desktop.text.container.Theme;
import org.apache.http.annotation.Experimental;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GoogleSearchEngine {
    private final URIBuilder uriBuilder = new URIBuilder("https://customsearch.googleapis.com/customsearch/v1?");

    public GoogleSearchEngine(String cx, String key) throws URISyntaxException {
        uriBuilder.addParameter("cx", cx);
        uriBuilder.addParameter("key", key);
    }

    /**
     * Поиск интернет-страниц по теме
     *
     * @param theme По какой теме происходит поиск
     * @return Список интернет-источников по теме
     */
    @Experimental
    public List<Source> getSources(Theme theme) throws IOException, URISyntaxException {
        var query = theme.getTitle();

        URL url = uriBuilder.addParameter("q", query).build().toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();

        for (String inputLine = in.readLine(); inputLine != null; inputLine = in.readLine()) {
            response.append(inputLine);
        }
        connection.disconnect();

        String content = response.toString();

        //Элементы с ссылками на сайты
        var jArr= new JSONObject(content).getJSONArray("items");
        Set<URI> uris = new HashSet<>();
        for (int i = 0; i < jArr.toList().size(); i++) {
            uris.add(new URI(jArr.getJSONObject(i).get("link").toString()));
        }

        return uris.stream().map(uri -> new Source(uri, SourceType.WEB)).toList();
    }
}