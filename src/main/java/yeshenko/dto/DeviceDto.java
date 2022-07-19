package yeshenko.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yeshenko.constant.Constants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto {

    @NotNull
    @Pattern(regexp = Constants.PATTERN_SERIAL_NUMBER)
    private String serialNumber;

    @NotNull
    @Pattern(regexp = Constants.PATTERN_MODEL)
    private String model;

    @NotNull
    private String description;
}
