package yeshenko.mapper;

import org.mapstruct.Mapper;
import yeshenko.dto.DeviceDto;
import yeshenko.model.Device;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    DeviceDto toDto(Device device);

    Device toModel(DeviceDto deviceDto);
}
