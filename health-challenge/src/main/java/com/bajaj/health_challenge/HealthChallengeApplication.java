package com.bajaj.health_challenge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.*;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class HealthChallengeApplication {
	private final NthLevelFollowerService nthLevelService;

	@Autowired
	public HealthChallengeApplication(NthLevelFollowerService nthLevelService) {
		this.nthLevelService = nthLevelService;
	}

	public static void main(String[] args) {
		SpringApplication.run(HealthChallengeApplication.class, args);
	}

	@Bean
	public WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}

	@Bean
	public CommandLineRunner run(WebClient.Builder webClientBuilder) {
		return args -> {
			WebClient client = webClientBuilder
					.baseUrl("https://bfhldevapigw.healthrx.co.in/hiring/")
					.defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
					.build();

			String requestBody = "{\"name\": \"Soumya Singh\", \"regNo\": \"RA2211027010209\", \"email\": \"ss2623@srmist.edu.in\"}";

			client.post()
					.uri("generateWebhook")
					.bodyValue(requestBody)
					.retrieve()
					.bodyToMono(ApiResponse.class)
					.subscribe(response -> {
						String webhook = response.getWebhook();
						String accessToken = response.getAccessToken();
						ApiResponse.UsersContainer usersContainer = response.getData().getUsers();

						int findId = usersContainer.getFindId();
						int n = usersContainer.getN();
						List<ApiResponse.User> users = usersContainer.getUsers();

						List<Integer> outcome = nthLevelService.findNthLevelFollowers(findId, n, users);

						WebhookResult result = new WebhookResult();
						result.setRegNo("RA2211027010209");
						result.setOutcome(outcome);

						sendToWebhook(webhook, accessToken, result);
					});
		};
	}

	private void sendToWebhook(String webhook, String accessToken, WebhookResult result) {
		WebClient client = WebClient.builder()
				.baseUrl(webhook)
				.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
				.build();

		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonBody = mapper.writeValueAsString(result);
			client.post()
					.bodyValue(jsonBody)
					.retrieve()
					.bodyToMono(String.class)
					.retryWhen(Retry.fixedDelay(4, Duration.ofSeconds(1))) // Retry 4 times
					.subscribe(
							res -> System.out.println("Success: " + res),
							err -> System.err.println("Failed after retries: " + err)
					);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }
}