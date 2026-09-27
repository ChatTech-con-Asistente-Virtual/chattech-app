package com.librotech.chattech.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "mensajes")
public class Mensaje {

    @Id
    private String id;

    private String remitente;

    private String contenido;

    private LocalDateTime fechaEnvio = LocalDateTime.now();

    public Mensaje() {
    }

    public Mensaje(String remitente, String contenido) {
        this.remitente = remitente;
        this.contenido = contenido;
        this.fechaEnvio = LocalDateTime.now();
    }

    public Mensaje(String id, String remitente, String contenido, LocalDateTime fechaEnvio) {
        this.id = id;
        this.remitente = remitente;
        this.contenido = contenido;
        this.fechaEnvio = fechaEnvio;
    }

    public String getId() {
        return id;
    }

    public String getRemitente() {
        return remitente;
    }

    public String getContenido() {
        return contenido;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}