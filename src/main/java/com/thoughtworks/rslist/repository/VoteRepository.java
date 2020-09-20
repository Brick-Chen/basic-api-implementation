package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepository extends PagingAndSortingRepository<VoteEntity, Integer> {
    List<VoteEntity> findAll();

//    @Query(nativeQuery = true, value = "SELECT * FROM vote")
    List<VoteEntity> findByTimeBetween(Timestamp start, Timestamp end);

    List<VoteEntity> findAllByUserIdAndRsEventId(int userId, int rsEventId);
}
