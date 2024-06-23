package com.communitygreen.cgrn.dao;

import com.communitygreen.cgrn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {

    @Query("select u from User u where u.email=:email")
    public User getUserByUserName(@Param("email") String email);

    //L
}
