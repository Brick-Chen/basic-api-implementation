package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "rs_event")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RsEventEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String eventName;

    private String keyword;

    private int voteNum;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "rsEvent", cascade = CascadeType.REMOVE)
    private List<VoteEntity> voteEntities;
}
