/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package gateway;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.time.Duration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CdrGatewayApplicationRsApiIT {
    @LocalServerPort
    private int port;
    private WebTestClient webClient;
    @Rule
    public WireMockClassRule rsApiMock = new WireMockClassRule(WireMockSpring.options().httpsPort(8094).keystorePath("src/test/resources/rs-api.jks").keystorePassword("changeit"));

    @Before
    public void setup() throws SSLException {
        String baseUri = "https://rs.aspsp.dev-ob.forgerock.financial:" + port;

        SslContext sslContext = SslContextBuilder
                .forClient()
                .sslProvider(SslProvider.JDK)
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(
                HttpClient.create().secure(t -> t.sslContext(sslContext)

                ));

        this.webClient = WebTestClient.bindToServer(connector).responseTimeout(Duration.ofSeconds(10)).baseUrl(baseUri).build();
    }

    @Test
    public void rsApiExternalActuatorInfoForwardToInternalActuatorInfo() {
        // Given
        rsApiMock.stubFor(get(urlEqualTo("/actuator/info"))
                .willReturn(aResponse()
                        .withStatus(200)));

        // When
        webClient.get()
                .uri("/external/actuator/info")
                .exchange()

                // Then
                .expectStatus()
                .isOk();
    }

    @Test
    public void rsApiExternalActuatorHealthForwardToInternalActuatorHealth() {
        // Given
        rsApiMock.stubFor(get(urlEqualTo("/actuator/health"))
                .willReturn(aResponse()
                        .withStatus(200)));

        // When
        webClient.get()
                .uri("/external/actuator/health")
                .exchange()

                // Then
                .expectStatus()
                .isOk();
    }

}
