package yeshenko.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import yeshenko.model.DeviceInfo;

import java.util.ArrayList;

@Configuration
public class MinioConfig {

    @Value("${minio.access.key}")
    private String accessKey;

    @Value("${minio.access.secret}")
    private String accessSecret;

    @Value("${minio.url}")
    private String url;

    @Bean
    public MinioClient minioClient() {
        return new MinioClient.Builder()
                .endpoint(url)
                .credentials(accessKey, accessSecret)
                .build();
    }

    @Bean
    public DeviceInfo devicesToSave() {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDevices(new ArrayList<>());
        return deviceInfo;
    }

}
