package com.serejs.diplom.desktop.server.controllers;

import com.serejs.diplom.desktop.enums.SourceType;
import com.serejs.diplom.desktop.text.container.Literature;
import com.serejs.diplom.desktop.ui.states.State;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class FileClientController extends AbstractClientController {
    private static final String endpoint = "/api/files";

    public static void upload(Literature literature) throws Exception {
        if (literature.getSourceType() == SourceType.WEB) throw new Exception("Данный тип источника не имеет файла");

        LiteratureClientController.sendLiterature(literature);
        var id = literature.getId();

        var encoding = getEncoding();

        HttpClient httpclient = HttpClients.createDefault();

        var file = new File(literature.getUri());
        var fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("file", fileBody);

        var uriBuilder = new URIBuilder(baseUrl + endpoint);
        uriBuilder.addParameter("literatureId", Long.toString(id));

        var request = new HttpPost(uriBuilder.build());
        request.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
        request.setEntity(builder.build());

        HttpResponse response = httpclient.execute(request);
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED)
            throw new Exception("Ошибка отправления файла на сервер");
    }

    public static void download(Literature literature) throws URISyntaxException, IOException, HttpException {
        var outputDir = State.getOutputDirectory().getPath();
        var fileName = FilenameUtils.getName(literature.getUri().getPath());
        var file = new File(outputDir + "/" + fileName);

        var pairs = new LinkedList<NameValuePair>();
        pairs.add(new BasicNameValuePair("literatureId", String.valueOf(literature.getId())));

        //Изменить выбор кодировки файла при получении ответа
        var bytes  = getRequest(endpoint, pairs).getBytes("Windows-1251");

        var output = new FileOutputStream(file);
        output.write(bytes);

        literature.setUri(file.getAbsoluteFile().toURI());
    }
}
