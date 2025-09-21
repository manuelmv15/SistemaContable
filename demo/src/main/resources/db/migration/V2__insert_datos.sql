
--v2__inserDatos.sql
-- Roles
INSERT INTO rol (nombre) VALUES ('superAdmin'),('admin'), ('contador'), ('usuario')
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
