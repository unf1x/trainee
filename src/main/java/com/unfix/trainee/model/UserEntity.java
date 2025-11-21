package com.unfix.trainee.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private String id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_name", nullable = false)
    private TeamEntity team;

    @ManyToMany(mappedBy = "reviewers")
    @Builder.Default
    private Set<PullRequestEntity> reviewPullRequests = new HashSet<>();
}
