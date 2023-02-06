ALTER TABLE IF EXISTS answer_stat
    DROP COLUMN category_kpi,
    ADD COLUMN is_verified_by_weekly boolean DEFAULT FALSE,
    ADD COLUMN number_of_respondents bigint,
    ADD COLUMN week_year_stat character varying(255);

alter table answer_log
    add column IF NOT EXISTS question_category character varying(255);

alter table project_quiz
    add column IF NOT EXISTS uuid UUID DEFAULT gen_random_uuid();
