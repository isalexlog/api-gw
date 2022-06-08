package io.kadmos.apigw;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RouteTests {
    private static final Logger log = LoggerFactory.getLogger(RouteTests.class);
    private static final String HTTP_LOCALHOST = "http://localhost";

    @RegisterExtension
    static WireMockExtension savingsAServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @RegisterExtension
    static WireMockExtension savingsBServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();


    @DynamicPropertySource
    static void registerBackendServers(DynamicPropertyRegistry registry) {
        registry.add("app.savings-a", () -> HTTP_LOCALHOST + ":" + savingsAServer.getPort());
        registry.add("app.savings-b", () -> HTTP_LOCALHOST + ":" + savingsBServer.getPort());
    }

    @LocalServerPort
    private int localPort;

    @Test
    void savingsAServerRewriteTest(@Autowired WebTestClient webClient) {
        savingsAServer.stubFor(get("/balance").willReturn(ok().withBody("20")));

        webClient.get()
                .uri(HTTP_LOCALHOST + ":" + localPort + "/savings/a/balance")
                .exchange()
                .expectBody()
                .consumeWith((result) -> {
                    String body = new String(Objects.requireNonNull(result.getResponseBody()));
                    assertEquals("20", body);
                });
    }

    @Test
    void savingsBServerRewriteTest(@Autowired WebTestClient webClient) {
        savingsBServer.stubFor(get("/balance").willReturn(ok().withBody("0")));

        webClient.get()
                .uri(HTTP_LOCALHOST + ":" + localPort + "/savings/b/balance")
                .exchange()
                .expectBody()
                .consumeWith((result) -> {
                    String body = new String(Objects.requireNonNull(result.getResponseBody()));
                    assertEquals("0", body);
                });
    }

    @Test
    void savingsBServerTimeOutTest() {
        WebTestClient webTestClient = WebTestClient.bindToServer().responseTimeout(Duration.ofSeconds(10)).build();
        savingsBServer.stubFor(get("/balance").willReturn(aResponse().withFixedDelay(6000).withBody("0")));

        webTestClient.get()
                .uri(HTTP_LOCALHOST + ":" + localPort + "/savings/b/balance")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.GATEWAY_TIMEOUT_504);
    }
}
