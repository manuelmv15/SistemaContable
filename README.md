# Sistema Contable — Spring Boot

## creado por


### Carlos Manuel Melendez Villatoro #MV23036

### Cristian Alexis Ventura Ventura #VV23011

### César Josue Zuleta Villalobos #ZV23005

## Requisitos y versiones

SO: Linux/Mac/Windows (WSL2 en Windows recomendado).

Docker Engine ≥ 24, Docker Compose v2 ≥ 2.24.

Puertos libres: 8080 (app) y 5432 (PostgreSQL).

## Descargar y desplegar el proyecto

``` bash
git clone https://github.com/manuelmv15/SistemaContable.git

cd SistemaContable

```

## Configuracion

El proyecto incluye un archivo llamado '.env.example' renombrarla a '.env'.

*En caso de que no exista el archivo ".env" 0 ".env.example" crearlo como el siguente ejemplo:*

 ```   .env

# --- Postgres ---
POSTGRES_DB=sistemacontable
POSTGRES_USER=admin
POSTGRES_PASSWORD=adminpass

# --- Spring DataSource ---
SPRING_DATASOURCE_URL="jdbc:postgresql://db:5432/${POSTGRES_DB}"
SPRING_DATASOURCE_USERNAME="${POSTGRES_USER}"
SPRING_DATASOURCE_PASSWORD="${POSTGRES_PASSWORD}"

# --- JPA ---
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=true

# --- Perfil activo ---
SPRING_PROFILES_ACTIVE=docker
<<<<<<< HEAD
```

*A la hora de levantar el proyecto se crear automaticamente las tablas junto con las inserciones*.

### Esto crea 3 perfiles automaticamente, los cuales son

usuario: admin, contrenia: 1234

usuario: contador contrenia: 1234

## Despliegue con Docker

```bahs
docker compose down -v        # (opcional) limpia contenedores/volúmenes previos
docker compose build --no-cache
docker compose up -d
```

*la primera ves puede llegar a tardar varios segundos ya que*  descargará imágenes y ejecutará migraciones Flyway automáticamente.

La app quedará accesible en:  [http://localhost:8080](http://localhost:8080)

la primera ves puede llegar a tardar varios segundos ya que  descargará imágenes y ejecutará migraciones Flyway automáticamente.
>>>>>>> f1e48e7748ee292a1d7e45224c31b4c50ce4870f

## comandos utiles

Para la app
<<<<<<< HEAD

```bash
# Levantar (en segundo plano)
docker compose up -d

# ver los contenedores activos
docker ps 

# Ver logs (en vivo) de todos o de un servicio
docker compose logs -f
docker compose logs -f app     # servicio app
docker compose logs -f db      # servicio db/postgres

```

Para la base de datos

```bash
# Dentro del contenedor
docker compose exec db psql -U "$POSTGRES_USER" -d "$POSTGRES_DB"
```

### **Dentro de la base de datos usando la se pueden usar los comandos de PosgrestSQL**

### base de datos

``` mermaid

    rol ||--o{ usuario : tiene
    usuario ||--o{ evento : registra
    usuario ||--o{ partida : crea
    partida ||--o{ detallepartida : contiene
    detallepartida }o--|| cuentacontable : usa
    cuentacontable }o--|| tipocuenta : pertenece_a
    partida ||--o{ documentofuente : tiene
    documentofuente }o--|| clasificaciondocumento : clasificada_como
    libromayor }o--|| cuentacontable : calcula
    libromayor }o--|| periodocontable : pertenece_a
    balance }o--|| periodocontable : pertenece_a
    estadofinanciero }o--|| periodocontable : pertenece_a
    estadofinanciero }o--|| tipoestadofinanciero : es_de_tipo
```

## Estructura del proyecto

``` swift
├── docker-compose.yml
├── .env
├── Dockerfile
├── pom.xml
├── README.md
├── src
   ├── main
       ├── java/com/example/demo
       │   └── SistemaContableApplication.java
       └── resources
           ├── application.properties
           ├── db/migration/
           └── templates/
```
