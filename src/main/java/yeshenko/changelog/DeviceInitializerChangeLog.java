package yeshenko.changelog;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import yeshenko.constant.Constants;
import yeshenko.model.Device;
import yeshenko.repository.DeviceRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@ChangeUnit(id = Constants.CHANGE_LOG_ID, order = "1")
@Slf4j
public class DeviceInitializerChangeLog {

    @BeforeExecution
    public void beforeExecution(MongoTemplate mongoTemplate) {
        mongoTemplate.createCollection(Constants.DEVICES_COLLECTION);
    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution(MongoTemplate mongoTemplate) {
        mongoTemplate.dropCollection(Constants.DEVICES_COLLECTION);
    }

    @Execution
    public void execution(DeviceRepository deviceRepository) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Device>> typeReference = new TypeReference<>() {
        };

        try (InputStream inputStream = TypeReference.class.getResourceAsStream(Constants.PATH_TO_DEVICES_COLLECTION)) {
            List<Device> devices = mapper.readValue(inputStream, typeReference);
            log.info("Read devices from json: {}", devices);
            deviceRepository.saveAll(devices);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RollbackExecution
    public void rollbackExecution(DeviceRepository deviceRepository) {
        deviceRepository.deleteAll();
    }
}
