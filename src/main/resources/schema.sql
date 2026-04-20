CREATE TABLE brand (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE game (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    brand_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    game_engine VARCHAR(100) NOT NULL,
    CONSTRAINT fk_game_brand FOREIGN KEY (brand_id) REFERENCES brand(id)
);

CREATE TABLE game_version (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    game_id BIGINT NOT NULL,
    game_version VARCHAR(100) NOT NULL,
    version_name VARCHAR(200),
    location VARCHAR(500),
    updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_game_version_game FOREIGN KEY (game_id) REFERENCES game(id)
);