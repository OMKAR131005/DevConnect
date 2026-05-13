package com.devconnect.bakend.user;

import com.devconnect.bakend.sharedata.UserSummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/search")
    public ResponseEntity<Page<UserSummaryDTO>> searchUsers(@RequestParam String keyword, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.searchUser(keyword, pageable));
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteAccount() {
        return ResponseEntity.ok(userService.deleteUser());
    }
}