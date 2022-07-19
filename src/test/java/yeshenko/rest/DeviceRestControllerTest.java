package yeshenko.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import yeshenko.dto.DeviceDto;
import yeshenko.mapper.DeviceMapper;
import yeshenko.model.Device;
import yeshenko.repository.DeviceRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DeviceRestControllerTest {


    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private DeviceRepository repository;

    @Autowired
    private DeviceMapper mapper;

    @Test
    @Order(1)
    public void testGetAllDevices() {
        webTestClient.get().uri("/api")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(DeviceDto.class);

        Assertions.assertFalse(repository.findAll().isEmpty());
    }

    @Test
    @Order(2)
    public void testPostCreateDevice() {
        DeviceDto deviceToCreate = new DeviceDto("NewSerialNumber", "NewModel", "NewDescription");

        webTestClient.post().uri("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(deviceToCreate), DeviceDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.serialNumber").isNotEmpty()
                .jsonPath("$.serialNumber").isEqualTo(deviceToCreate.getSerialNumber());

        Assertions.assertTrue(repository.existsById(deviceToCreate.getSerialNumber()));
    }

    @Test
    @Order(3)
    public void testGetDeviceBySerialNumber() {
        webTestClient.get()
                .uri("/api/{serialNumber}", "NewSerialNumber")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(entityExchangeResult ->
                        Assertions.assertNotNull(entityExchangeResult.getResponseBody()))
                .jsonPath("$.serialNumber").isNotEmpty()
                .jsonPath("$.serialNumber").isEqualTo("NewSerialNumber");

        Assertions.assertTrue(repository.existsById("NewSerialNumber"));

    }

    @Test
    @Order(4)
    public void testPutUpdateDevice() {
        DeviceDto deviceDto = new DeviceDto("NewSerialNumber", "UpdatedModel", "NewDescription");

        webTestClient.put().uri("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(deviceDto), DeviceDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.serialNumber").isNotEmpty()
                .jsonPath("$.serialNumber").isEqualTo(deviceDto.getSerialNumber())
                .jsonPath("$.model").isEqualTo(deviceDto.getModel());

        Device device = repository.findById(deviceDto.getSerialNumber()).orElseThrow();
        Assertions.assertEquals(mapper.toDto(device), deviceDto);
    }

    @Test
    @Order(5)
    public void testDeleteRemoveDevice() {
        webTestClient.delete().uri("/api/{serialNumber}", "NewSerialNumber")
                .exchange()
                .expectStatus().isOk();

        Assertions.assertFalse(repository.existsById("NewSerialNumber"));
    }

    @Test
    public void testPostCreate_ShouldReturnBadRequestNotValidInputData() {
        DeviceDto deviceToCreate = new DeviceDto("", "NewModel", "NewDescription");


        webTestClient.post().uri("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(deviceToCreate), DeviceDto.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testPostCreate_ShouldReturnErrorItemAlreadyExists() {
        DeviceDto deviceToCreate = new DeviceDto("C36B7811", "NewModel", "NewDescription");


        webTestClient.post().uri("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(deviceToCreate), DeviceDto.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody();
    }

    @Test
    public void testPutUpdate_ShouldReturnErrorItemNotExists() {
        DeviceDto deviceToCreate = new DeviceDto("NotExists", "NewModel", "NewDescription");


        webTestClient.put().uri("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(deviceToCreate), DeviceDto.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NO_CONTENT)
                .expectBody();
    }

    @Test
    public void testDelete_ShouldReturnErrorItemNotExists() {
        webTestClient.delete().uri("/api/{serialNumber}", "NotExists")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NO_CONTENT)
                .expectBody();
    }
}