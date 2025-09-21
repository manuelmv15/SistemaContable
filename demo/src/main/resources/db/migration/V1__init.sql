--v1__init.sql
CREATE TABLE IF NOT EXISTS rol (
  id_rol     SERIAL PRIMARY KEY,
  nombre     VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS tipocuenta (
  id_tipocuenta  SERIAL PRIMARY KEY,
  nombre         VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS tipoestadofinanciero (
  id_tipoestadofinanciero SERIAL PRIMARY KEY,
  nombre                  VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS clasificaciondocumento (
  id_clasificacion  SERIAL PRIMARY KEY,
  nombre            VARCHAR(100) UNIQUE NOT NULL,
  descripcion       VARCHAR(255)
);


CREATE TABLE IF NOT EXISTS usuario (
  id_usuario   SERIAL PRIMARY KEY,
  nombre       VARCHAR(100)        NOT NULL,
  email        VARCHAR(150)        NOT NULL UNIQUE,
  usuario      VARCHAR(50)         NOT NULL UNIQUE,
  tipo_id      INT                 NOT NULL,      -- FK -> rol
  contrasenia  VARCHAR(255)        NOT NULL,      -- hash (BCrypt)
  estado       BOOLEAN             NOT NULL DEFAULT TRUE,
  CONSTRAINT fk_usuario_rol
    FOREIGN KEY (tipo_id) REFERENCES rol(id_rol)
    ON UPDATE RESTRICT ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS evento (
  id_evento   SERIAL PRIMARY KEY,
  usuario_id  INT                  NOT NULL,   -- FK -> usuario
  accion      VARCHAR(100)         NOT NULL,
  detalle     TEXT,
  fecha       TIMESTAMP            NOT NULL DEFAULT NOW(),
  CONSTRAINT fk_evento_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario)
    ON UPDATE RESTRICT ON DELETE RESTRICT
);



CREATE TABLE IF NOT EXISTS cuentacontable (
  id_cuenta   SERIAL PRIMARY KEY,
  codigo      VARCHAR(20)  NOT NULL UNIQUE,
  nombre      VARCHAR(150) NOT NULL,
  tipo_id     INT          NOT NULL,           -- FK -> tipocuenta
  CONSTRAINT fk_cuenta_tipocuenta
    FOREIGN KEY (tipo_id) REFERENCES tipocuenta(id_tipocuenta)
    ON UPDATE RESTRICT ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS periodocontable (
  id_periodo   SERIAL PRIMARY KEY,
  fecha_inicio TIMESTAMP NOT NULL,
  fecha_fin    TIMESTAMP NOT NULL,
  CONSTRAINT ck_periodo_rango CHECK (fecha_fin > fecha_inicio)
  -- (Opcional) puedes asegurar no solapar periodos con EXCLUDE si más adelante lo necesitas
);

CREATE TABLE IF NOT EXISTS partida (
  id_partida  SERIAL PRIMARY KEY,
  fecha       DATE            NOT NULL,
  concepto    VARCHAR(255)    NOT NULL,
  usuario_id  INT             NOT NULL,         -- FK -> usuario (quien crea)
  CONSTRAINT fk_partida_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario)
    ON UPDATE RESTRICT ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS detallepartida (
  id_detalle  SERIAL PRIMARY KEY,
  partida_id  INT             NOT NULL,         -- FK -> partida
  cuenta_id   INT             NOT NULL,         -- FK -> cuentacontable
  debe        DECIMAL(12,2)   NOT NULL DEFAULT 0.00,
  haber       DECIMAL(12,2)   NOT NULL DEFAULT 0.00,
  CONSTRAINT ck_detalle_signo CHECK (debe >= 0 AND haber >= 0),
  CONSTRAINT fk_detalle_partida
    FOREIGN KEY (partida_id) REFERENCES partida(id_partida)
    ON UPDATE RESTRICT ON DELETE CASCADE,
  CONSTRAINT fk_detalle_cuenta
    FOREIGN KEY (cuenta_id) REFERENCES cuentacontable(id_cuenta)
    ON UPDATE RESTRICT ON DELETE RESTRICT
);

-- Libro mayor: saldo por cuenta en un período
CREATE TABLE IF NOT EXISTS libromayor (
  id_libro       SERIAL PRIMARY KEY,
  cuenta_id      INT           NOT NULL,  -- FK -> cuentacontable
  periodo_id     INT           NOT NULL,  -- FK -> periodocontable
  saldo_deudor   DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  saldo_acreedor DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  CONSTRAINT uq_libromayor UNIQUE (cuenta_id, periodo_id),
  CONSTRAINT fk_libro_cuenta
    FOREIGN KEY (cuenta_id) REFERENCES cuentacontable(id_cuenta)
    ON UPDATE RESTRICT ON DELETE RESTRICT,
  CONSTRAINT fk_libro_periodo
    FOREIGN KEY (periodo_id) REFERENCES periodocontable(id_periodo)
    ON UPDATE RESTRICT ON DELETE RESTRICT
);

-- Balance general por periodo (único)
CREATE TABLE IF NOT EXISTS balance (
  id_balance     SERIAL PRIMARY KEY,
  periodo_id     INT           NOT NULL UNIQUE, -- un balance por periodo
  tot_activo     DECIMAL(14,2) NOT NULL DEFAULT 0.00,
  tot_pasivo     DECIMAL(14,2) NOT NULL DEFAULT 0.00,
  tot_patrimonio DECIMAL(14,2) NOT NULL DEFAULT 0.00,
  CONSTRAINT fk_balance_periodo
    FOREIGN KEY (periodo_id) REFERENCES periodocontable(id_periodo)
    ON UPDATE RESTRICT ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS estadofinanciero (
  id_estado                SERIAL PRIMARY KEY,
  periodo_id               INT           NOT NULL,
  tipoestadofinanciero_id  INT           NOT NULL,
  ruta_pdf                 VARCHAR(255),
  CONSTRAINT uq_estado UNIQUE (periodo_id, tipoestadofinanciero_id),
  CONSTRAINT fk_estado_periodo
    FOREIGN KEY (periodo_id) REFERENCES periodocontable(id_periodo)
    ON UPDATE RESTRICT ON DELETE RESTRICT,
  CONSTRAINT fk_estado_tipo
    FOREIGN KEY (tipoestadofinanciero_id) REFERENCES tipoestadofinanciero(id_tipoestadofinanciero)
    ON UPDATE RESTRICT ON DELETE RESTRICT
);

-- Documentos fuente asociados a partidas y su clasificación
CREATE TABLE IF NOT EXISTS documentofuente (
  id_documento     SERIAL PRIMARY KEY,
  partida_id       INT            NOT NULL,     -- FK -> partida
  clasificacion_id INT            NOT NULL,     -- FK -> clasificaciondocumento
  nombre           VARCHAR(100)   NOT NULL,
  descripcion      VARCHAR(255),
  ruta_pdf         VARCHAR(255),
  CONSTRAINT fk_doc_partida
    FOREIGN KEY (partida_id) REFERENCES partida(id_partida)
    ON UPDATE RESTRICT ON DELETE RESTRICT,
  CONSTRAINT fk_doc_clasif
    FOREIGN KEY (clasificacion_id) REFERENCES clasificaciondocumento(id_clasificacion)
    ON UPDATE RESTRICT ON DELETE RESTRICT
);

-- ========== 4) Índices útiles (performance) ==========

CREATE INDEX IF NOT EXISTS ix_partida_fecha      ON partida(fecha);
CREATE INDEX IF NOT EXISTS ix_detalle_partida    ON detallepartida(partida_id);
CREATE INDEX IF NOT EXISTS ix_detalle_cuenta     ON detallepartida(cuenta_id);
CREATE INDEX IF NOT EXISTS ix_libro_periodo      ON libromayor(periodo_id);
CREATE INDEX IF NOT EXISTS ix_doc_partida        ON documentofuente(partida_id);
CREATE INDEX IF NOT EXISTS ix_evento_usuario     ON evento(usuario_id);