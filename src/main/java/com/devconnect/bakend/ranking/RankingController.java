package com.devconnect.bakend.ranking;

import com.devconnect.bakend.ranking.dto.RankingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("/top")
    public ResponseEntity<List<RankingResponse>> getTopRankings(@RequestParam(defaultValue = "10") int topN) {
        return ResponseEntity.ok(rankingService.getTopRankings(topN));
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshRankings() {
        rankingService.refreshAllRankings();
        return ResponseEntity.ok("Rankings refreshed successfully");
    }
}
