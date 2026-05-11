package com.devconnect.bakend.follow;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/follow")
public class FollowController {
    private final FollowService followService;

    @PostMapping("/{username}")
    public ResponseEntity<String> toggleFollow(@PathVariable String username) {
        return ResponseEntity.ok(followService.toggleFollow(username));
    }

    @GetMapping("/{username}/status")
    public ResponseEntity<String> getFollowStatus(@PathVariable String username) {
        return ResponseEntity.ok(followService.getFollowStatus(username));
    }
}