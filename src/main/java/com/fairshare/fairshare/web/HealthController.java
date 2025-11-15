package com.fairshare.fairshare.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/")
    public String home() {
        return " FairShare backend is live ";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
