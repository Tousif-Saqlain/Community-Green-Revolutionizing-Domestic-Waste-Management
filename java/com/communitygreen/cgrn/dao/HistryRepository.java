package com.communitygreen.cgrn.dao;

import com.communitygreen.cgrn.entity.Histry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistryRepository extends JpaRepository<Histry,Integer> {

    @Query("from Histry as h where h.user.id=:userId")
    //currentPage-page
    //Histry per page - 10
    public Page<Histry> findHistriesByUser(@Param("userId")int userId, Pageable pePageable);


    //L

}
