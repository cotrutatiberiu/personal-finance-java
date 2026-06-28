INSERT INTO tr.roles(name)
VALUES ('USER')
ON CONFLICT DO NOTHING;

INSERT INTO tr.currencies(name)
VALUES ('EUR'),
       ('USD'),
       ('RON')
ON CONFLICT (lower(name)) DO NOTHING;

INSERT INTO tr.account_types(name)
VALUES ('CASH'),
       ('CARD'),
       ('BANK'),
       ('SAVINGS')
ON CONFLICT DO NOTHING;