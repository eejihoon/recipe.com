package com.dunk.django.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final Environment environment;

    @GetMapping("/profile")
    public String profile() {
        //현재 실행 중인 Active Profile을 모두 불러온다.
        List<String> profiles = Arrays.asList(environment.getActiveProfiles());
        List<String> runtimeProfiles = Arrays.asList("runtime", "runtime2");
        String defaultProfile = profiles.isEmpty() ? "local" : profiles.get(0);
        return profiles.stream()
                .filter(runtimeProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }
}
