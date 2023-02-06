package com.endava.endavibe.statistics;

import com.endava.endavibe.common.dto.stats.AnswerStatDto;
import com.endava.endavibe.reporting.WeeklyReportEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "answer_stat", schema = "public", catalog = "endavibe")
public class AnswerStatEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1945670924947820279L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "question_category_id")
    private Long questionCategoryId;

    @Column(name = "from_date")
    private LocalDateTime fromDate;

    @Column(name = "to_date")
    private LocalDateTime toDate;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "avg_value", columnDefinition="numeric", precision=2, scale=1)
    private Double avgValue;

    @Column(name = "number_of_respondents")
    private Long nrOfRespondents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weekly_report")
    private WeeklyReportEntity weeklyReport;

    @Column(name = "is_verified_by_weekly")
    private Boolean isVerifiedByWeekly = false;

    @Column(name = "week_year_stat")
    private String weekAndYearOfStat;


    @Column(name = "ins_date")
    private LocalDateTime insDate = LocalDateTime.now();

    public record ProjectWeekYear(Long projectId, String weekAndYearOfStat) {};
    public record Category(Long questionCategoryId) {};


    public AnswerStatDto toDto() {
        return new ModelMapper().map(this, AnswerStatDto.class);
    }
}
