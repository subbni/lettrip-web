package com.lettrip.lettripbackend.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Email
    private String email;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String nickname;
    @Column(nullable = true)
    private String imageUrl;

    @NotNull
    private ProviderType providerType;

    @Builder
    public User(String email, String password, String name,
                String nickname, String imageUrl, ProviderType providerType ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.providerType = providerType;
    }

    public void update(String name, String nickname, String imageUrl) {
        this.name = name;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }
}
