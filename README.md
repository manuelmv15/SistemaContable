# Sistema Contable – Spring Boot + PostgreSQL (Docker)

Proyecto basado en Spring Boot con PostgreSQL usando Docker Compose y migraciones con Flyway.
No necesitas tener Java ni Postgres instalados en tu máquina.

``` swift

📁 Estructura mínima

demo/
├─ docker-compose.yml
├─ Dockerfile
├─ pom.xml
├─ mvnw / mvnw.cmd
└─ src/
   ├─ main/java/com/example/demo/...
   └─ main/resources/
      ├─ application.properties
      └─ db/migration/V1__init.sql
```

## 1. Configurar variables de entorno

En el directorio demo/ crea un archivo .env:

``` env
# Base de datos
POSTGRES_DB=sistemacontable
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

# App (Spring Boot)
SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/sistemacontable
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=true
SPRING_PROFILES_ACTIVE=docker
```

## 🐳 2. Levantar el proyecto con Docker

### Desde demo/:

``` bash
docker compose build
docker compose up -d
```

Ver logs de la aplicación:

``` bash
docker compose logs -f app
```

Cuando aparezca:

Tomcat started on port(s): 8080
Started SistemaContableApplication...

La app estará disponible en: [http://localhost:8080](http://localhost:8080).

## 🗄️ 4. Migraciones con Flyway

Los scripts están en src/main/resources/db/migration/.
Flyway los ejecuta automáticamente al iniciar la app.

Ejemplo: V1__init.sql crea todas las tablas.

Para revisar las tablas en Postgres:

``` bash
docker exec -it pg_sistemacontable psql -U postgres -d sistemacontable

# Dentro de psql:

\dt
select * from rol;
```

## 🔧 5. Comandos útiles

Parar contenedores

``` bash
docker compose down
```

Parar y borrar datos (⚠️ elimina el volumen de Postgres)

``` bash
docker compose down -v
```

Reconstruir solo la app (tras cambios en el código)

``` bash
docker compose up -d --build app
```

Ver logs

``` bsah
docker compose logs -f app
```

📌 Notas

application.properties está configurado para leer las variables del .env.

El healthcheck en docker-compose.yml asegura que la app arranque cuando Postgres ya está listo.

Si obtienes 404 en /, asegúrate de tener un controlador simple como HelloController.