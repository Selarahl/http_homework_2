import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main {

    public static final String REMOTE_SERVICE_URI = "https://api.nasa.gov/planetary/apod?api_key=E7UahtbgSmtdv7IV996nIY9mTqj26vo51Pa6Vq5o";
    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        HttpGet request = new HttpGet(REMOTE_SERVICE_URI);
        CloseableHttpResponse response = httpClient.execute(request);

        Nasa nasa = MAPPER.readValue(response.getEntity().getContent(),
                new TypeReference<>() {}
        );

        String imgUrl = nasa.getUrl();
        String[] imgUrlParts = imgUrl.split("/");
        String imgName = imgUrlParts[imgUrlParts.length - 1];

        File file = new File(imgName);
        URL url = new URL(imgUrl);
        FileUtils.copyURLToFile(url, file);
    }
}
