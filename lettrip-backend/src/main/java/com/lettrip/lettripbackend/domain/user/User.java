package com.lettrip.lettripbackend.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @Column(name="USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Email
    private String email;

    private String password;

    private String name;

    @Size(min=2,max=30)
    private String nickname;

    private String imageUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;
    @NotNull
    @Column(name="provider_type")
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    private int point;

    @Builder
    public User(String email, String password, String name,
                String nickname, String imageUrl, ProviderType providerType, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.providerType = providerType;
        this.role = role;
        this.point = 0;
    }

    public void update(String name, String nickname, String imageUrl) {
        this.name = name;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }

    public void withdraw() {
        this.name="";
        this.email = "";
        this.password = "";
        this.role=Role.NOUSER;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl= imageUrl;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void addPoint(int point) {
        this.point += point;
    }

}
