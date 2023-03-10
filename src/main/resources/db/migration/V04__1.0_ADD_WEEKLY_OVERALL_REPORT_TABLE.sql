CREATE TABLE IF NOT EXISTS weekly_report
(
    id                    bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    uuid                  UUID      DEFAULT gen_random_uuid(),
    project_id            bigint,
    team_members_total    bigint,
    team_members_answered bigint,
    overall_score         numeric(2, 1),
    week_year_stat        character varying(255),
    ins_date              TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT weekly_report_pkey PRIMARY KEY (id)

);
ALTER TABLE IF EXISTS answer_stat
    ADD COLUMN weekly_report bigint,
    ADD CONSTRAINT weekly_report_fk FOREIGN KEY (weekly_report) REFERENCES weekly_report (id);

