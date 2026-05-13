package com.devconnect.bakend.user;


import com.devconnect.bakend.sharedata.UserSummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    public Page<UserSummaryDTO> searchUser(String username, Pageable pageable){
       return userRepository.searchUsers(username,pageable);
    }
    public String deleteUser(){
        Long id=(Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userRepository.deleteById(id);
        return "thanks for your journey with us hope you will be keep going in your life";
    }

}
