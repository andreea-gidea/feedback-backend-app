-- --LANDING PE 4 PROIECTE PE 4 PERIOADE
-- --DETALII PE 1 PROIECT
--
-- -- insert projects --
-- insert into project (name, is_engine_active)
-- values ('Test project 1', true),
--        ('Test project 2', true),
--        ('Test project 3', true);
--
-- -- insert app_users --
-- insert into app_user (first_name, last_name, email, password)
-- values ('Andreea', 'Gidea', 'Andreea.Gidea@endava.com', 'test'),
--        ('Andreea', 'Gidea', 'Andreea.Gidea@endava.com', 'test'),
--        ('Andreea', 'Gidea', 'Andreea.Gidea@endava.com', 'test');
--
-- -- insert project_app_user --
-- do
-- $$
--     declare
-- f record;
-- begin
-- for f in
-- select id
-- from project loop
--     for i in 1..3 loop
-- insert
-- into project_app_user (app_user_id, project_id, is_owner)
-- values (f.id, i, false);
-- end loop;
-- end loop;
-- end $$;
--
-- -- insert quiz --
-- insert into quiz (is_active)
-- values (true);
--
-- -- insert question_quiz --
-- insert into question_quiz (quiz_id, question_id)
-- values (1, 8),
--        (1, 29),
--        (1, 40),
--        (1, 50);
--
-- -- insert answer_user --
-- insert into answer_user (quiz_id, app_user_id, project_id, ins_date)
-- values (1, 1, 1, current_date),
--        (1, 2, 1, current_date),
--        (1, 3, 1, current_date),
--        (1, 1, 2, current_date),
--        (1, 2, 2, current_date),
--        (1, 3, 2, current_date),
--        (1, 1, 3, current_date),
--        (1, 2, 3, current_date),
--        (1, 3, 3, current_date);
--
-- -- insert answer_user --
-- insert into answer_log (project_id, question_category_id, question_id, question_quiz_id, answer_value,
--                         question_content, ins_date)
-- values (1, 1, 8, 1, 2, 'Do you think that the project meetings achieve their objectives?', current_date),
--        (1, 2, 29, 2, 2, 'Does your work environment allow you to work distraction-free when you need to?', current_date),
--        (1, 3, 40, 3, 3, 'Do you feel your opinions are heard and valued by your team?', current_date),
--        (1, 4, 50, 4, 1, 'Does your manager communicate expectations and feedback clearly and professionally?', current_date),
--        (2, 1, 8, 1, 2, 'Do you think that the project meetings achieve their objectives?', current_date),
--        (2, 2, 29, 2, 2, 'Does your work environment allow you to work distraction-free when you need to?', current_date),
--        (2, 3, 40, 3, 3, 'Do you feel your opinions are heard and valued by your team?', current_date),
--        (2, 4, 50, 4, 1, 'Does your manager communicate expectations and feedback clearly and professionally?', current_date),
--        (3, 1, 8, 1, 2, 'Do you think that the project meetings achieve their objectives?', current_date),
--        (3, 2, 29, 2, 2, 'Does your work environment allow you to work distraction-free when you need to?', current_date),
--        (3, 3, 40, 3, 3, 'Do you feel your opinions are heard and valued by your team?', current_date),
--        (3, 4, 50, 4, 1, 'Does your manager communicate expectations and feedback clearly and professionally?', current_date),
--        (1, 1, 8, 1, 4, 'Do you think that the project meetings achieve their objectives?', current_date),
--        (1, 2, 29, 2, 3, 'Does your work environment allow you to work distraction-free when you need to?', current_date),
--        (1, 3, 40, 3, 1, 'Do you feel your opinions are heard and valued by your team?', current_date),
--        (1, 4, 50, 4, 2, 'Does your manager communicate expectations and feedback clearly and professionally?', current_date),
--        (2, 1, 8, 1, 1, 'Do you think that the project meetings achieve their objectives?', current_date),
--        (2, 2, 29, 2, 4, 'Does your work environment allow you to work distraction-free when you need to?', current_date),
--        (2, 3, 40, 3, 3, 'Do you feel your opinions are heard and valued by your team?', current_date),
--        (2, 4, 50, 4, 2, 'Does your manager communicate expectations and feedback clearly and professionally?', current_date),
--        (3, 1, 8, 1, 5, 'Do you think that the project meetings achieve their objectives?', current_date),
--        (3, 2, 29, 2, 4, 'Does your work environment allow you to work distraction-free when you need to?', current_date),
--        (3, 3, 40, 3, 2, 'Do you feel your opinions are heard and valued by your team?', current_date),
--        (3, 4, 50, 4, 2, 'Does your manager communicate expectations and feedback clearly and professionally?', current_date);
--
--