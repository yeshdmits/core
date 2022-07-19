package yeshenko.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import yeshenko.model.Device;

@Repository
public interface DeviceRepository extends MongoRepository<Device, String> {
}
