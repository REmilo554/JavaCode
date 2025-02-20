INSERT INTO books (name, author_id)
VALUES ('Властелин колец: Братство кольца', (SELECT author_id FROM authors WHERE full_name = 'Джон Р.Р. Толкин')),
       ('Гордость и предубеждение', (SELECT author_id FROM authors WHERE full_name = 'Джейн Остин')),
       ('1984', (SELECT author_id FROM authors WHERE full_name = 'Джордж Оруэлл')),
       ('Убийство в Восточном экспрессе', (SELECT author_id FROM authors WHERE full_name = 'Агата Кристи')),
       ('Старик и море', (SELECT author_id FROM authors WHERE full_name = 'Эрнест Хемингуэй')),
       ('Властелин колец: Две крепости', (SELECT author_id FROM authors WHERE full_name = 'Джон Р.Р. Толкин')),
       ('Властелин колец: Возвращение короля', (SELECT author_id FROM authors WHERE full_name = 'Джон Р.Р. Толкин')),
       ('Эмма', (SELECT author_id FROM authors WHERE full_name = 'Джейн Остин')),
       ('Скотный двор', (SELECT author_id FROM authors WHERE full_name = 'Джордж Оруэлл')),
       ('Десять негритят', (SELECT author_id FROM authors WHERE full_name = 'Агата Кристи'));