package io.kadmos.savings;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SavingsApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void basicBusinessLogicTest() {
		var result = restTemplate.getForObject("http://localhost:" + port + "/balance", String.class);
		assertThat(result).isEqualTo("0.00");

		var headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>("{\"amount\": 5.02}", headers);

		result = restTemplate.postForObject("http://localhost:" + port + "/balance", request, String.class);
		assertThat(result).isEqualTo("5.02");

		request = new HttpEntity<>("{\"amount\": -4.01}", headers);
		result = restTemplate.postForObject("http://localhost:" + port + "/balance", request, String.class);
		assertThat(result).isEqualTo("1.01");
	}

}
