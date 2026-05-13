package com.devconnect.bakend.user;

import com.devconnect.bakend.sharedata.UserSummaryDTO;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String Email);

    User findByUsername(@NotEmpty String username);

    @Query("SELECT new com.devconnect.bakend.sharedata.UserSummaryDTO(u.username, p.fullName, p.profilePicture) " +
            "FROM User u JOIN Profile p ON p.user = u " +
            "WHERE u.username LIKE %:keyword% OR p.fullName LIKE %:keyword%")
    Page<UserSummaryDTO> searchUsers(@Param("keyword") String keyword, Pageable pageable);
}
