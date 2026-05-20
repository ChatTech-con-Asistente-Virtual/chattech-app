# LibroTech

Proyecto Spring Boot para practicar MVC, JPA, CRUD, ResponseEntity, paginación, Swagger y pruebas con `@DataJpaTest`.

## Ejecutar

Desde la carpeta donde está el `pom.xml`:

```bash
mvn spring-boot:run
```

## Endpoints principales

### Libros

```http
GET    /api/libros
GET    /api/libros/{id}
GET    /api/libros/autor/{autor}
POST   /api/libros
PUT    /api/libros/{id}
DELETE /api/libros/{id}
```

### Categorías

```http
GET    /api/categorias
GET    /api/categorias/{id}
POST   /api/categorias
PUT    /api/categorias/{id}
DELETE /api/categorias/{id}
```

## Paginación y ordenamiento

```http
GET /api/libros?page=0&size=5
GET /api/libros?sort=autor,desc
GET /api/libros?page=0&size=10&sort=titulo,asc
```

El endpoint `GET /api/libros` devuelve un objeto `Page<Libro>` con metadatos como `totalElements`, `totalPages`, `last`, `first`, etc.

## Swagger UI

Con la aplicación corriendo:

```text
http://localhost:8080/swagger-ui.html
```

## Consola H2

```text
http://localhost:8080/h2-console
```

Datos de conexión:

```text
JDBC URL: jdbc:h2:file:./data/librotech_db
User Name: sa
Password: vacío
```

## Tests

```bash
mvn test
```

Incluye pruebas con `@DataJpaTest` para:

- `LibroRepository`
- `CategoriaRepository`


## Panel administrativo web con Thymeleaf

Con la aplicación corriendo:

```text
http://localhost:8080/admin/libros
```

Rutas UI agregadas:

```http
GET  /admin/libros
GET  /admin/libros/nuevo
POST /admin/libros/guardar
```

La interfaz web usa:

- `@Controller` para rutas `/admin/**`.
- `Model` para enviar datos a las vistas.
- Thymeleaf con `th:text`, `th:each`, `th:if`, `th:object`, `th:field`, `th:href` y `th:replace`.
- Patrón Post-Redirect-Get en el guardado del formulario.
- Fragmentos reutilizables en `templates/layout/componentes.html`.
- Validación visual para evitar años de publicación futuros.
