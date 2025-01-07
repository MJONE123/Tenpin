package com.example.Tenpin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "카카오 소셜 로그인 API")
public class AuthController {

    private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private final String CLIENT_ID = "1831e1a8b72bcab5c73a684fcaaf4d6a"; // 카카오 앱 REST API 키
    private final String REDIRECT_URI = "http://localhost:8080/api/auth/kakao/callback";

    @Operation(summary = "카카오 로그인", description = "카카오에서 발급받은 액세스 토큰을 통해 인증을 진행합니다.")
    @PostMapping("/kakao")
    public ResponseEntity<Map<String, Object>> authenticateKakao(@RequestBody Map<String, String> request) {
        String code = request.get("code");

        // 1. Access Token 요청
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = String.format(
                "grant_type=authorization_code&client_id=%s&redirect_uri=%s&code=%s",
                CLIENT_ID, REDIRECT_URI, code
        );

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> tokenResponse = restTemplate.exchange(
                KAKAO_TOKEN_URL, HttpMethod.POST, entity, Map.class
        );

        String accessToken = (String) tokenResponse.getBody().get("access_token");

        // 2. 사용자 정보 요청
        headers.clear();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> userInfoRequest = new HttpEntity<>(headers);
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                KAKAO_USER_INFO_URL, HttpMethod.GET, userInfoRequest, Map.class
        );

        Map<String, Object> userInfo = userInfoResponse.getBody();

        // 사용자 정보 및 토큰 반환
        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "userInfo", userInfo
        ));
    }

    @Operation(summary = "카카오 로그인 Callback", description = "카카오 로그인 완료 후 Authorization Code를 수신합니다.")
    @GetMapping("/kakao/callback")
    public ResponseEntity<String> kakaoCallback(@RequestParam("code") String code) {
        return ResponseEntity.ok("Authorization Code received: " + code);
    }

    @PostMapping("/kakao/token")
    public ResponseEntity<Map<String, Object>> getKakaoToken(@RequestBody Map<String, String> request) {
        String code = request.get("code"); // Authorization Code

        String body = String.format(
                "grant_type=authorization_code&client_id=%s&redirect_uri=%s&code=%s",
                "1831e1a8b72bcab5c73a684fcaaf4d6a", // REST API 키
                "http://localhost:8080/api/auth/kakao/callback", // Redirect URI
                code
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            // Access Token 반환
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", "Failed to get Access Token",
                    "details", e.getMessage()
            ));
        }
    }

    @GetMapping("/kakao/userinfo")
    public ResponseEntity<Map<String, Object>> getUserInfo(@RequestHeader("Authorization") String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            // 사용자 정보 반환
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", "Failed to get User Info",
                    "details", e.getMessage()
            ));
        }
    }



}
