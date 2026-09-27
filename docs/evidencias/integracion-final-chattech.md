# Evidencia - Integración final LibroTech + ChatTech

**Proyecto:** LibroTech escalado con ChatTech  
**Issue:** ISSUE-012 - Ejecutar integración final y regresión
**Responsable:** Camilo Mitnick  
**Fecha original:** 20/05/2026
**Última verificación:** 27/09/2026

## Objetivo

Validar que ChatTech funciona integrado con LibroTech sin afectar el catálogo, la API REST ni las vistas existentes.

## Verificación automatizada

Con Docker activo:

```bash
mvn test
```

Resultado de la última verificación:

```text
Tests run: 20, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

La prueba de MongoDB utiliza Testcontainers y no depende de datos ni servicios persistentes del entorno local.

## Verificación manual

Preparar el entorno:

```bash
docker compose up -d
mvn spring-boot:run
```

Comprobar los siguientes puntos:

1. Abrir `/admin/libros` y verificar que se muestran los datos iniciales.
2. Crear un libro desde `/admin/libros/nuevo`.
3. Abrir `/swagger-ui.html` y consultar los endpoints REST.
4. Abrir `/admin/chat` y verificar la conexión WebSocket.
5. Enviar un mensaje y comprobar su persistencia después de recargar.
6. Repetir la prueba con OpenAI habilitado mediante `AI_CHAT_MODEL=openai` y `OPENAI_API_KEY`.

## Componentes cubiertos

- Contexto completo de Spring Boot.
- Repositorios JPA de libros y categorías.
- Repositorio MongoDB en un contenedor temporal.
- Validaciones del servicio de mensajes.
- Fallback del asistente cuando OpenAI está deshabilitado.
- Flujo asíncrono del controlador WebSocket.

## Conclusión

La regresión automatizada finaliza correctamente. El proyecto está preparado como MVP reproducible para evaluación local; las limitaciones para un despliegue público están documentadas en el README.
