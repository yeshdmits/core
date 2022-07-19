package yeshenko.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yeshenko.exception.DeviceDatabaseException;
import yeshenko.exception.DeviceNotFoundException;
import yeshenko.model.Device;
import yeshenko.repository.DeviceRepository;

import java.util.List;

import static yeshenko.constant.Constants.NO_DEVICE_FOUND_IN_DATABASE;
import static yeshenko.constant.Constants.SERIAL_NUMBER_ALREADY_EXISTS;
import static yeshenko.constant.Constants.SERIAL_NUMBER_NOT_EXISTS;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final FileStorageService fileStorageService;


    public Device create(Device device) {
        log.debug("Execute Create method, device: {}", device);

        if (deviceRepository.existsById(device.getSerialNumber())) {
            log.error("Serial number already exists");
            throw new DeviceDatabaseException(SERIAL_NUMBER_ALREADY_EXISTS);
        }
        Device saved = deviceRepository.save(device);
        log.info("Create successfully, serialNumber: {}", saved.getSerialNumber());
        fileStorageService.addDevice(device);
        return saved;
    }

    public List<Device> findAll() {
        log.debug("Execute Read All method.");

        List<Device> devices = deviceRepository.findAll();

        log.info("Found list of devices successfully, number of devices: {}", devices.size());
        return devices;
    }

    public Device findBySerialNumber(String serialNumber) {
        log.debug("Execute Read method. Serial Number: {}", serialNumber);

        Device device = deviceRepository.findById(serialNumber)
                .orElseThrow(() -> {
                    log.error("No devices found in the database");
                    return new DeviceNotFoundException(NO_DEVICE_FOUND_IN_DATABASE);
                });

        log.info("Found device successfully, device: {}", device);
        return device;
    }

    public Device update(Device device) {
        log.debug("Execute Update method, device: {}", device);

        if (!deviceRepository.existsById(device.getSerialNumber())) {
            log.error("Serial number not exists: {}", device.getSerialNumber());
            throw new DeviceNotFoundException(SERIAL_NUMBER_NOT_EXISTS);
        }
        Device updated = deviceRepository.save(device);
        log.info("Updated device successfully, serialNumber: {}", updated.getSerialNumber());
        return updated;
    }

    public void deleteBySerialNumber(String serialNumber) {
        log.debug("Execute Delete method, serialNumber: {}", serialNumber);

        if (!deviceRepository.existsById(serialNumber)) {
            log.error("Serial number not exists: {}", serialNumber);
            throw new DeviceNotFoundException(SERIAL_NUMBER_NOT_EXISTS);
        }

        deviceRepository.deleteById(serialNumber);
        log.info("Deleted device successfully, serialNumber: {}", serialNumber);
    }
}
