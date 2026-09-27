package com.librotech.chattech.service;

import com.librotech.chattech.model.Mensaje;
import com.librotech.chattech.repository.MensajeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MensajeService {

    private static final int MAX_REMITENTE_LENGTH = 60;
    private static final int MAX_CONTENIDO_LENGTH = 2000;

    private final MensajeRepository mensajeRepository;

    public MensajeService(MensajeRepository mensajeRepository) {
        this.mensajeRepository = mensajeRepository;
    }

    public Mensaje guardarMensaje(Mensaje mensaje) {
        validarMensaje(mensaje);

        if (mensaje.getFechaEnvio() == null) {
            mensaje.setFechaEnvio(LocalDateTime.now());
        }

        return mensajeRepository.save(mensaje);
    }

    public Mensaje guardarMensajeUsuario(Mensaje mensajeRecibido) {
        if (mensajeRecibido == null) {
            throw new IllegalArgumentException("El mensaje no puede ser nulo.");
        }

        if (mensajeRecibido.getRemitente() != null
                && "LibroBot IA".equalsIgnoreCase(mensajeRecibido.getRemitente().trim())) {
            throw new IllegalArgumentException("El nombre LibroBot IA está reservado.");
        }

        Mensaje mensaje = new Mensaje(
                mensajeRecibido.getRemitente(),
                mensajeRecibido.getContenido()
        );
        return guardarMensaje(mensaje);
    }

    public List<Mensaje> obtenerHistorial() {
        return mensajeRepository.findAll(Sort.by(Sort.Direction.ASC, "fechaEnvio"));
    }

    public List<Mensaje> obtenerHistorialReciente() {
        List<Mensaje> mensajes = new ArrayList<>(mensajeRepository.findTop10ByOrderByFechaEnvioDesc());
        Collections.reverse(mensajes);
        return mensajes;
    }

    private void validarMensaje(Mensaje mensaje) {
        if (mensaje == null) {
            throw new IllegalArgumentException("El mensaje no puede ser nulo.");
        }

        if (mensaje.getRemitente() == null || mensaje.getRemitente().isBlank()) {
            throw new IllegalArgumentException("El remitente es obligatorio.");
        }

        if (mensaje.getRemitente().length() > MAX_REMITENTE_LENGTH) {
            throw new IllegalArgumentException("El remitente no puede superar 60 caracteres.");
        }

        if (mensaje.getContenido() == null || mensaje.getContenido().isBlank()) {
            throw new IllegalArgumentException("El contenido del mensaje es obligatorio.");
        }

        if (mensaje.getContenido().length() > MAX_CONTENIDO_LENGTH) {
            throw new IllegalArgumentException("El contenido no puede superar 2000 caracteres.");
        }
    }
}
