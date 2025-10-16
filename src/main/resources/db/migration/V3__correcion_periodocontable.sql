
--v2__inserDatos.sql
-- Roles
INSERT INTO rol (nombre) VALUES ('superAdmin'),('admin'), ('contador'), ('auditor')
ON CONFLICT (nombre) DO NOTHING;

-- Tipos de cuenta
INSERT INTO tipocuenta (nombre) VALUES
  ('Activo'), ('Pasivo'), ('Patrimonio'), ('Ingreso'), ('Gasto')
ON CONFLICT (nombre) DO NOTHING;

-- Tipos de estados financieros
INSERT INTO tipoestadofinanciero (nombre) VALUES
  ('Balance General'),
  ('Estado de Resultados'),
  ('Flujo de Efectivo')
ON CONFLICT (nombre) DO NOTHING;

-- Clasificaciones de documento
INSERT INTO clasificaciondocumento (nombre, descripcion) VALUES
  ('Factura', 'Factura de proveedor o cliente'),
  ('Recibo', 'Recibo de caja o ingreso'),
  ('Nota de crédito', 'Ajuste contable')
ON CONFLICT (nombre) DO NOTHING;

INSERT INTO usuario (nombre, email, usuario, tipo_id, contrasenia, estado)
VALUES
    ('superAdmin', 'superadmin@gmail.com', 'superadmin', 1, '$2a$10$jqecdu2uyb/BdgURC1BZZ.OBXhXp1ZPcS7.oKHX2luSBTdgDX5f46', TRUE),
    ('admin', 'admin@gmail.com', 'admin', 2, '$2a$10$jqecdu2uyb/BdgURC1BZZ.OBXhXp1ZPcS7.oKHX2luSBTdgDX5f46', TRUE),
    ('contador', 'contador@gmail.com', 'contador', 3, '$2a$10$jqecdu2uyb/BdgURC1BZZ.OBXhXp1ZPcS7.oKHX2luSBTdgDX5f46', TRUE),
    ('usuario', 'usuario@gmail.com', 'usuario', 4, '$2a$10$jqecdu2uyb/BdgURC1BZZ.OBXhXp1ZPcS7.oKHX2luSBTdgDX5f46', TRUE)
ON CONFLICT (usuario) DO NOTHING;