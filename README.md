# Core service

Spring-boot application with REST API for CRUD operations on objects representing some devices.

Using the mongock library, add a data migration that will run once and save 3 devices to the database

| Serial Number        | Model        | Description           |
|:---------------------|:-------------|:----------------------|
| C36B7811             | Z2XQ         | ThinkStation P620     |
| 246277C3             | 20HA         | ThinkPad T410         |
| 5623-KU              | GalaxyS9     | Samsung Galaxy S9     |

## Getting Started

To rise this service execute the following commands:

    1. Go in main menu: 'Run' -> 'Edit configurations...' -> 'Configuration'
    -> 'Spring boot' -> 'Active profiles' and type 'dev'.

    2. In IDE`s terminal execute 'docker-compose up' - to run required docker containers.
    
    3. Run application.

## Deployment

Deployment variables:

| Variable name                     | Default value                                         | Description            |
|:----------------------------------|:------------------------------------------------------|:-----------------------|
| MONGODB_URL                       | `mongodb://localhost:27017/core`                      | Host of mongo database |
| KAFKA_URL                         | `localhost:9092`                                      | Host of kafka          |
| MINIO_URL                         | `http://localhost:9000`                               | Host of minio server   |
| OAUTH_URL                         | `http://localhost:8081/auth/realms/yeshenko-personal` | Host of keycloak oauth |

## Built With

* [Spring Boot](https://start.spring.io/) - An application framework and inversion of control container for the Java
  platform.
* [Maven](https://maven.apache.org/) - Dependency Management

