# Sistema Contable — Spring Boot + PostgreSQL + Docker

## Descargar y desplegar el proyecto

``` bash
git clone https://github.com/manuelmv15/SistemaContable.git
cd SistemaContable
```

El proyecto incluye un archivo llamado '.env.examlple' crea un archivo '.env' copialo y cambia las credensiales

**A la hora de levantar el proyecto se crear automaticamente las tablas junto con las inserciones**
Para ejecutar con Docker solo necesitas Docker/Compose.

Docker 24+ y Docker Compose v2

## Despliegue con Docker

```bahs
docker compose down -v        # (opcional) limpia contenedores/volúmenes previos
docker compose build --no-cache
docker compose up -d
```

*la primera ves puede llegar a tardar varios segundos ya que*  descargará imágenes y ejecutará migraciones Flyway automáticamente.

verificar si se levantaron los dos contenedores con:

```bash
docker ps
```

vera algo como

```bash
CONTAINER ID   IMAGE          COMMAND                  CREATED         STATUS                    PORTS                                         NAMES
d96a1e3c3ea3   proyecto-app   "sh -c 'java $JAVA_O…"   4 minutes ago   Up 14 seconds             0.0.0.0:8080->8080/tcp, [::]:8080->8080/tcp   app_sistemacontable
fb2b424d003a   postgres:16    "docker-entrypoint.s…"   4 minutes ago   Up 20 seconds (healthy)   0.0.0.0:5432->5432/tcp, [::]:5432->5432/tcp   pg_sistemacontable
```

La app quedará accesible en:  [http://localhost:8080](http://localhost:8080)

### Detener

```bash
docker compose down 
```

## Estructura del proyecto

``` swift
├── docker-compose.yml
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
           └── templates/

```
