package yeshenko.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import yeshenko.model.Device;
import yeshenko.model.DeviceInfo;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import static yeshenko.constant.Constants.DATE_TIME_PATTERN;
import static yeshenko.constant.Constants.DEVICE_INFO_JSON;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${minio.bucket.name}")
    private String bucketName;

    private final DeviceInfo deviceInfo;

    private final MinioClient minioClient;

    @Scheduled(fixedRate = 1000 * 60 * 24)
    public void save() {

        deviceInfo.setDateTime(Timestamp.from(Instant.now()));
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(DEVICE_INFO_JSON);

        try {
            objectMapper.writeValue(file, deviceInfo);
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Bucket: {} created", bucketName);
            }

            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketName)
                            .object(new SimpleDateFormat(DATE_TIME_PATTERN).format(new Date()).concat(".json"))
                            .filename(DEVICE_INFO_JSON)
                            .build());

        } catch (Exception e) {
            log.error("Could not save file to S3");
        }
    }

    public void addDevice(Device device) {
        deviceInfo.getDevices().add(device);
    }
}
