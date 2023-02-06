-- insert question category --
insert into question_category (title) values
    ('related to the project'),
    ('related to personal satisfaction'),
    ('related to the peers'),
    ('related to the manager');

-- insert answer --
insert into answer (title, value) values
    ('Strongly disagree', 1),
    ('Disagree', 2),
    ('Agree', 4),
    ('Strongly agree', 5);

-- insert questions --
insert into question (content, category_id) values
    ('Do you feel your work is recognized on the project?', 1),
    ('Do you feel your leaders on your project are transparent?', 1),
    ('Do you feel fulfilled in your current role?', 1),
    ('Do you feel like you have enough time to complete your tasks?', 1),
    ('Do you feel like you have enough information to complete your work?', 1),
    ('Do you feel your project went well?', 1),
    ('Are you satisfied with the communication in your current project?', 1),
    ('Do you think that the project meetings achieve their objectives?', 1),
    --('Do you think that there are too many meetings in a week?', 1),
    ('How reasonable is your workload?', 1),
    ('Do you enjoy the work that you do?', 1),
    ('Are your responsibilities clear?', 1),
    ('How would you rate the way your organization makes use of your strengths?', 1),
    ('Have your skills improved since you started this project?', 1),
    ('Do you like the technologies you are working with?', 1),
    ('Do you feel secure in your role in the company?', 1),
    --('Do you feel like there are aspects of your current project impeding your long-term progress in the company?', 1),

    ('How do I feel today?', 2),
    ('Overall, how satisfied are you with your job?', 2),
    ('How did you feel in the last daily meeting?', 2),
    ('How would you rate your level of happiness at work?', 2),
    ('How would you rate the frequency at which you receive recognition?', 2),
    ('Is recognition meaningful when you receive it?', 2),
    ('Is there someone at work who helps you grow and develop?', 2),
    ('Do you feel you have enough freedom to decide how you do your work?', 2),
    ('How would you describe the level of challenge you have at work?', 2),
    ('Do you have enough time to get involved in other projects for Endava?', 2),
    ('How motivated are you to start your workday?', 2),
    ('Do you feel valued at work?', 2),
    ('Do you feel that you receive recognition for your hard work?', 2),
    --('Have you missed a personal event because of work?', 2),
    --('Would offloading some of your work to a colleague reduce your stress levels?', 2),
    ('Does your work environment allow you to work distraction-free when you need to?', 2),
    ('Are you satisfied with the level of comfort in your physical workplace?', 2),
    ('Do you feel positive about coming to work?', 2),
    ('Do you feel you have a good work-life balance?', 2),
    --('Does your job cause an unreasonable amount of stress for you?', 2),

    ('How do you get along with your teammates?', 3),
    ('Did you receive the necessary help when you needed it?', 3),
    ('Do you and your colleagues help each other when a problem appears?', 3),
    ('Do you feel connected to your colleagues and team members?', 3),
    ('Do you feel appreciated and respected by your teammates?', 3),
    ('Does your team provide the support you need?', 3),
    ('Does your team inspire you to do your best?', 3),
    ('Do you feel your opinions are heard and valued by your team?', 3),
    ('Do you feel like you are part of the team?', 3),
    ('Do you feel like your peers are committed to doing quality work?', 3),
    ('Do you have a best friend at the office?', 3),

    ('Are you satisfied with the frequency of feedback coming from your direct manager?', 4),
    ('Do you feel like your direct manager cares about your well-being?', 4),
    ('Do you feel like your direct manager is someone you can trust?', 4),
    ('On a scale of 1-5 how satisfied are you with how frequently you communicate with your direct manager?', 4),
    ('How comfortable do you feel giving feedback to management?', 4),
    ('Do you feel appreciated and respected by your line manager?', 4),
    ('Does your manager communicate expectations and feedback clearly and professionally?', 4),
    ('Does your manager provide the support you need?', 4),
    ('Do you feel you are recognized for your achievements?', 4),
    ('Do you feel your opinions are heard and valued by your manager?', 4);

-- insert question_config --
    do $$
    declare
        f record;
    begin
        for f in select id from question loop
            for i in 1 .. 4 loop
                insert into question_config (question_id, answer_id) values  (f.id, i);
            end loop;
        end loop;
    end $$;

