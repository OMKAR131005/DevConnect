package com.devconnect.bakend.profile;

import com.devconnect.bakend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Long> {

    Profile findByUser(User user);

    void deleteByUser(User user);
}
