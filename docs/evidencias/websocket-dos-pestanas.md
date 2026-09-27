# Evidencia - Validación WebSocket en dos pestañas

**Proyecto:** LibroTech + ChatTech  
**Issue:** ISSUE-006 - Validar flujo WebSocket en dos pestañas
**Responsable:** Luis Mejia  
**Fecha original:** 20/05/2026

## Objetivo

Validar que ChatTech distribuye mensajes en tiempo real entre dos clientes mediante WebSocket, STOMP y SockJS.

## Precondiciones

- Java, Maven y Docker disponibles.
- MongoDB iniciado mediante Docker Compose.
- Aplicación disponible en `http://localhost:8080`.
- Endpoint SockJS `/chat-websocket` habilitado.

## Preparación

```bash
docker compose up -d
mvn spring-boot:run
```

OpenAI no es necesario para validar la comunicación. Sin una clave configurada, LibroBot publica el mensaje de fallback esperado.

## Procedimiento manual

1. Abrir `http://localhost:8080/admin/chat` en dos pestañas.
2. Comprobar que ambas muestran el estado `Conectado al chat en tiempo real`.
3. Enviar un mensaje desde la primera pestaña.
4. Confirmar que el mensaje aparece en ambas pestañas sin recargarlas.
5. Confirmar que la respuesta de LibroBot aparece en ambas pestañas.
6. Recargar una pestaña y comprobar que el historial se recupera desde MongoDB.

## Resultado esperado

- Ambos clientes reciben los mensajes publicados en `/tema/mensajes`.
- Los mensajes enviados a `/app/enviar` quedan almacenados en MongoDB.
- La fecha y el identificador son administrados por el servidor.
- El cliente no puede utilizar `LibroBot IA` como nombre.
- El campo de nombre admite hasta 60 caracteres y el contenido hasta 2000.

## Evidencia automatizada

El flujo controlador-servicio-publicación está cubierto por:

```text
src/test/java/com/librotech/chattech/controller/ws/ChatSocketControllerTest.java
src/test/java/com/librotech/chattech/service/MensajeServiceTest.java
src/test/java/com/librotech/chattech/repository/MensajeRepositoryTest.java
```

La evidencia visual original no se encontraba almacenada en el repositorio. Se conserva este procedimiento reproducible para volver a capturarla antes de publicar una demostración.
