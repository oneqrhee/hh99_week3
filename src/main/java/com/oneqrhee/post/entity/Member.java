package com.oneqrhee.post.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    String nickname;

    @Column(nullable = false)
    String password;

    @Enumerated(value = EnumType.STRING)
    private Authority authority;

    @Builder
    public Member(String nickname, String password, Authority authority){
        this.nickname = nickname;
        this.password = password;
        this.authority = authority;
    }
}
