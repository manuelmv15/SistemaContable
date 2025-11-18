INSERT INTO cuentacontable (codigo, nombre, tipo_id) VALUES
    -- EFECTIVO Y EQUIVALENTE
    ('1101.01.01', 'Caja General', 1),
    ('1101.01.02', 'Caja Chica', 1),

    -- BANCOS
    ('1101.02.01', 'Bancos Nacionales', 1),
    ('1101.02.02', 'Bancos Extranjeros', 1),
    ('1101.02.03', 'Cheques en Tránsito', 1),

    -- CUENTAS POR COBRAR
    ('1102.01', 'Clientes', 1),
    ('1102.02', 'Documentos por Cobrar', 1),

    -- IVA CRÉDITO
    ('1103', 'Iva Crédito Fiscal', 1),

    -- INVENTARIOS
    ('1104', 'Inventarios de Mercaderías', 1),

    -- ACTIVOS NO CORRIENTES
    ('1201.01', 'Terrenos', 2),
    ('1201.02', 'Edificios', 2),
    ('1201.03', 'Mobiliario y Equipo', 2),
    ('1201.04', 'Vehículos', 2),
    ('1201.05', 'Depreciación Acumulada', 2),

    -- PASIVO CORRIENTE
    ('2101.01', 'Proveedores', 2),
    ('2101.02', 'Documentos por Pagar', 2),
    ('2101.03', 'IVA Débito Fiscal', 2),
    ('2101.04', 'Retenciones por Pagar', 2),
    ('2101.05', 'Sueldos por Pagar', 2),

    -- PASIVO NO CORRIENTE
    ('2201', 'Préstamos Bancarios a Largo Plazo', 2),

    -- CAPITAL CONTABLE
    ('3101', 'Capital Social', 3),
    ('3102', 'Aportaciones de Socios', 3),
    ('3103', 'Utilidades Retenidas', 3),
    ('3104', 'Resultado del Ejercicio', 3),

    -- INGRESOS
    ('4101', 'Ventas de Mercaderías', 4),
    ('4102', 'Ingresos por Servicios', 4),
    ('4103', 'Otros Ingresos', 4)

    -- COSTOS
ON CONFLICT (codigo) DO NOTHING;