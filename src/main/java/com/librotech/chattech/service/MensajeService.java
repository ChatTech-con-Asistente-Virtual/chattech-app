package com.librotech.chattech.service;

import com.librotech.chattech.model.Mensaje;
import com.librotech.chattech.repository.MensajeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class MensajeService {

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

    public List<Mensaje> obtenerHistorial() {
        return mensajeRepository.findAll(Sort.by(Sort.Direction.ASC, "fechaEnvio"));
    }

    public List<Mensaje> obtenerHistorialReciente() {
        List<Mensaje> mensajes = mensajeRepository.findTop10ByOrderByFechaEnvioDesc();
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

        if (mensaje.getContenido() == null || mensaje.getContenido().isBlank()) {
            throw new IllegalArgumentException("El contenido del mensaje es obligatorio.");
        }
    }
}