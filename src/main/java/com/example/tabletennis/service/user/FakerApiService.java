package com.example.tabletennis.service.user;

import com.example.tabletennis.dto.request.user.UserInitRequest;
import com.example.tabletennis.dto.response.user.FakerApiUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Service
public class FakerApiService {

    private final RestTemplate restTemplate;

    public FakerApiUserResponse initUsers(UserInitRequest userInitRequest) {
         String uri = UriComponentsBuilder
                .fromUriString("https://fakerapi.it/api/v1/users")
                .queryParam("_seed", userInitRequest.seed())
                .queryParam("_quantity", userInitRequest.quantity())
                .queryParam("_locale", "ko_KR")
                .encode()
                .toUriString();
        try {
            ResponseEntity<FakerApiUserResponse> response = restTemplate.getForEntity(uri, FakerApiUserResponse.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new IllegalStateException();
            }

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
