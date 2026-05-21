# Evidencia — Validación WebSocket en dos pestañas

**Proyecto:** LibroTech + ChatTech  
**Issue:** ISSUE-006 — Validar flujo WebSocket en dos pestañas  
**Responsable:** Luis Mejia  
**Fecha:** 20/05/2026

---

## Objetivo

Validar que el módulo ChatTech permite comunicación en tiempo real entre dos pestañas del navegador usando WebSocket, STOMP y SockJS.

---

## Precondiciones

- La aplicación compila correctamente.
- MongoDB está activo.
- El endpoint `/admin/chat` carga correctamente.
- El endpoint WebSocket `/chat-websocket` está disponible.
- La vista se suscribe a `/tema/mensajes`.
- La vista envía mensajes a `/app/enviar`.

---

## Comandos ejecutados

```bash
docker compose up -d
mvn clean test
mvn spring-boot:run