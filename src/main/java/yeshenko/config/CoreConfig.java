package yeshenko.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import yeshenko.converter.JwtClientRoleConverter;
import yeshenko.model.DeviceInfo;

import java.util.ArrayList;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CoreConfig {
    @Value("${minio.access.key}")
    private String accessKey;

    @Value("${minio.access.secret}")
    private String accessSecret;

    @Value("${minio.url}")
    private String url;

    @Bean
    public MinioClient minioClient() {
        return new MinioClient.Builder()
                .endpoint(url)
                .credentials(accessKey, accessSecret)
                .build();
    }

    @Bean
    public DeviceInfo devicesToSave() {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDevices(new ArrayList<>());
        return deviceInfo;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .oauth2ResourceServer()
                .jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
        return http.build();
    }

    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthConverter = new JwtAuthenticationConverter();
        jwtAuthConverter.setJwtGrantedAuthoritiesConverter(new JwtClientRoleConverter());
        return jwtAuthConverter;
    }
}
