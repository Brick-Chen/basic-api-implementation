package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepository extends PagingAndSortingRepository<VoteEntity, Integer> {
    List<VoteEntity> findAll();

    @Query(nativeQuery = true, value = "SELECT * FROM vote WHERE time >= :start AND time <= :end")
    List<VoteEntity> findAllByTimeBetween(LocalDateTime start, LocalDateTime end);
}
