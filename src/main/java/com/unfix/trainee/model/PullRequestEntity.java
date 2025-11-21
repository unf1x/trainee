package com.unfix.trainee.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pull_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PullRequestEntity {

    @Id
    @Column(name = "pull_request_id", nullable = false, updatable = false)
    private String id;

    @Column(name = "pull_request_name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PullRequestStatus status;

    @ManyToMany
    @JoinTable(
            name = "pull_request_reviewers",
            joinColumns = @JoinColumn(name = "pull_request_id"),
            inverseJoinColumns = @JoinColumn(name = "reviewer_id")
    )
    @Builder.Default
    private Set<UserEntity> reviewers = new HashSet<>();

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "merged_at")
    private Instant mergedAt;
}
