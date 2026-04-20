INSERT INTO brand (name)
VALUES ('newport');

INSERT INTO brand (name)
VALUES ('camel');

INSERT INTO game (brand_id, name, game_engine)
VALUES (1, 'findables', 'findObject');

INSERT INTO game (brand_id, name, game_engine)
VALUES (1, 'super game', 'superEngine');

INSERT INTO game_version (game_id, game_version, version_name, location)
VALUES (1, 'v1', 'Findables Week 1', 'games/findables/v1/index.html');

INSERT INTO game_version (game_id, game_version, version_name, location)
VALUES (1, 'v2', 'Findables Week 2', 'games/findables/v2/index.html');

INSERT INTO game_version (game_id, game_version, version_name, location)
VALUES (2, 'v1', 'Super Game Version 1', 'games/super-game/v1/index.html');