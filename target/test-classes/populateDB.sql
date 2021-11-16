INSERT INTO placeTest (type, name, address) VALUES
    ('Bar', 'Televisor', 'Radisheva, 4') ON CONFLICT DO NOTHING;
INSERT INTO placeTest (type, name, address) VALUES
    ('Bar', 'Melodiya', 'Pervomayskaya, 36') ON CONFLICT DO NOTHING;