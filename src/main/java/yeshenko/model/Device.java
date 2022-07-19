package yeshenko.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import yeshenko.constant.Constants;

@Data
@Document(Constants.DEVICES_COLLECTION)
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @Id
    private String serialNumber;

    private String model;

    private String description;
}
