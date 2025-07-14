-- ----------------------------------------------------------------------------
-- Model
-------------------------------------------------------------------------------

-- Eliminar las tablas si existen
DROP TABLE IF EXISTS Compra;
DROP TABLE IF EXISTS Partido;

-- Crear la tabla Partido
CREATE TABLE Partido (
    partidoId BIGINT NOT NULL AUTO_INCREMENT,
    equipoVisitante VARCHAR(255) COLLATE latin1_bin NOT NULL,
    fechaHoraCelebracion DATETIME NOT NULL,
    precioEntradas FLOAT NOT NULL,
    entradasDisponibles SMALLINT,
    fechaHoraAlta DATETIME NOT NULL,
    entradasVendidas SMALLINT NOT NULL,
    CONSTRAINT PartidoPK PRIMARY KEY(partidoId),
    CONSTRAINT CHK_validPrice CHECK (precioEntradas >= 0 AND precioEntradas <= 10000000),
    CONSTRAINT CHK_validEntradasDisponibles CHECK (entradasDisponibles >= 0 AND entradasDisponibles <= 10000000),
    CONSTRAINT CHK_validEntradasVendidas CHECK (entradasVendidas >= 0 AND entradasVendidas <= entradasDisponibles)
) ENGINE = InnoDB;

-- Crear la tabla Compra
CREATE TABLE Compra (
    saleId BIGINT NOT NULL AUTO_INCREMENT,
    emailUsuario VARCHAR(255) COLLATE latin1_bin NOT NULL,
    partidoId BIGINT NOT NULL,
    precioEntradas DOUBLE NOT NULL,
    numeroTarjetaBancaria VARCHAR(255) COLLATE latin1_bin NOT NULL,
    numeroEntradasCompradas SMALLINT,
    fechaHoraCompra DATETIME NOT NULL,
    entradasRecogidas SMALLINT NOT NULL,
    CONSTRAINT CompraPK PRIMARY KEY(saleId),
    CONSTRAINT CompraPartidoIdFK FOREIGN KEY(partidoId)
        REFERENCES Partido(partidoId) ON DELETE CASCADE
) ENGINE = InnoDB;