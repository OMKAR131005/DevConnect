package com.devconnect.bakend.user;


import com.devconnect.bakend.exceptions.ResourceNotFoundException;
import com.devconnect.bakend.follow.FollowRepository;
import com.devconnect.bakend.notification.NotificationRepository;
import com.devconnect.bakend.post.CommentRepository;
import com.devconnect.bakend.post.PostLikeRepository;
import com.devconnect.bakend.post.PostRepository;
import com.devconnect.bakend.profile.ProfileRepository;
import com.devconnect.bakend.sharedata.UserSummaryDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final NotificationRepository notificationRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final FollowRepository followRepository;
    public Page<UserSummaryDTO> searchUser(String username, Pageable pageable){
       return userRepository.searchUsers(username,pageable);
    }
    @Transactional
    public String deleteUser() {
        Long id = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        notificationRepository.deleteByFromUser(user);
        notificationRepository.deleteByToUser(user);
        postLikeRepository.deleteByUser(user);
        commentRepository.deleteByUser(user);
        postRepository.deleteByUser(user);
        followRepository.deleteByFollower(user);
        followRepository.deleteByFollowing(user);
        profileRepository.deleteByUser(user);
        userRepository.deleteById(id);
        return "thanks for your journey with us hope you will be keep going in your life";
    }

}
