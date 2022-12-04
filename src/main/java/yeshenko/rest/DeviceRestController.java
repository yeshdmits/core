package yeshenko.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yeshenko.dto.DeviceDto;
import yeshenko.mapper.DeviceMapper;
import yeshenko.model.Device;
import yeshenko.service.DeviceService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class DeviceRestController {

    private final DeviceService deviceService;
    private final DeviceMapper mapper;

    @KafkaListener(topics = "devices", groupId = "device_group_id")
    public DeviceDto create(@Valid @RequestBody DeviceDto device) {

        Device model = mapper.toModel(device);

        return mapper.toDto(deviceService.create(model));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<DeviceDto> findAll() {
        return deviceService.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{serialNumber}")
    @PreAuthorize("hasRole('USER')")
    public DeviceDto find(@PathVariable String serialNumber) {
        return mapper.toDto(deviceService.findBySerialNumber(serialNumber));
    }

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public DeviceDto update(@Valid @RequestBody DeviceDto device) {

        Device model = mapper.toModel(device);

        return mapper.toDto(deviceService.update(model));
    }

    @DeleteMapping("/{serialNumber}")
    @PreAuthorize("hasRole('USER')")
    public void delete(@PathVariable String serialNumber) {
        deviceService.deleteBySerialNumber(serialNumber);
    }
}
