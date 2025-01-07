package com.example.Tenpin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/example")
@Tag(name = "예시 API", description = "예시를 위한 API입니다")
public class ExampleController {

    @GetMapping("/hello")
    @Operation(summary = "안녕", description = "안녕 api입니다")
    public String hello() {
        return "안녕@@@@@@@@@@@@";
    }


}
