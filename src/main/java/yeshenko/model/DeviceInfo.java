package yeshenko.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class DeviceInfo {
    private List<Device> devices;
    private Timestamp dateTime;
}
