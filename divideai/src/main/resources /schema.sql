CREATE TABLE usuario (
    id          BIGSERIAL PRIMARY KEY,
    nome        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    senha_hash  VARCHAR(255) NOT NULL
);

CREATE TABLE grupo (
    id           BIGSERIAL PRIMARY KEY,
    nome         VARCHAR(100) NOT NULL,
    descricao    TEXT,
    data_criacao DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE grupo_usuario (
    grupo_id     BIGINT NOT NULL REFERENCES grupo(id) ON DELETE CASCADE,
    usuario_id   BIGINT NOT NULL REFERENCES usuario(id),
    data_entrada DATE NOT NULL DEFAULT CURRENT_DATE,
    PRIMARY KEY (grupo_id, usuario_id)
);

CREATE TABLE despesa (
    id           BIGSERIAL PRIMARY KEY,
    grupo_id     BIGINT NOT NULL REFERENCES grupo(id) ON DELETE CASCADE,
    pagador_id   BIGINT NOT NULL REFERENCES usuario(id),
    descricao    VARCHAR(200) NOT NULL,
    valor        NUMERIC(12,2) NOT NULL CHECK (valor > 0),
    data         DATE NOT NULL,
    categoria    VARCHAR(30) NOT NULL,
    tipo_divisao VARCHAR(20) NOT NULL
);

CREATE TABLE divisao_despesa (
    id            BIGSERIAL PRIMARY KEY,
    despesa_id    BIGINT NOT NULL REFERENCES despesa(id) ON DELETE CASCADE,
    usuario_id    BIGINT NOT NULL REFERENCES usuario(id),
    valor_devido  NUMERIC(12,2) NOT NULL CHECK (valor_devido >= 0),
    quitada       BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE (despesa_id, usuario_id)
);