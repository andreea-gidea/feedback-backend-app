package com.endava.endavibe.reporting;

import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "weekly_report", schema = "public", catalog = "endavibe")
public class WeeklyReportEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1945670924947820279L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "team_members_total")
    private Long nrOfTeamMembers;

    @Column(name = "team_members_answered")
    private Long nrOfRespondents;

    @Column(name = "overall_score", columnDefinition="numeric", precision=2, scale=1)
    private Double overallScore;

    @Column(name = "week_year_stat")
    private String weekAndYearOfStat;

    @Column(name = "ins_date")
    @Builder.Default()
    private LocalDateTime insDate = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        setUuid(java.util.UUID.randomUUID());
    }

    public record ProjectId(Long projectId) {};

}
