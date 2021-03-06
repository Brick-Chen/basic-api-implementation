package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String userName;
    private Integer age;
    private String gender;
    private String email;
    private String phone;
    private int voteNum;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<RsEventEntity> rsEventS;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<VoteEntity> voteEntities;
}
