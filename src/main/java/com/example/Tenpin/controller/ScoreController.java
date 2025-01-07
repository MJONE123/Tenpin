package com.example.Tenpin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/scores")
@Tag(name = "Score", description = "점수 기록 및 통계 API")
public class ScoreController {

    @Operation(summary = "점수 기록 추가", description = "사용자의 점수를 기록합니다.")
    @PostMapping
    public ResponseEntity<Map<String, String>> addScore(@RequestBody Map<String, Object> score) {
        // 점수 기록 로직
        return ResponseEntity.ok(Map.of(
                "message", "Score added successfully"
        ));
    }

    @Operation(summary = "점수 통계 조회", description = "사용자의 점수 통계를 조회합니다.")
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics(@RequestParam String userId) {
        // 점수 통계 조회 로직
        return ResponseEntity.ok(Map.of(
                "mostHitPins", 10,
                "mostMissedPins", 2,
                "strikeRate", 0.75,
                "spareRate", 0.60
        ));
    }
}
