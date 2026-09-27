package com.librotech.chattech.controller.rest;

import com.librotech.chattech.model.Mensaje;
import com.librotech.chattech.service.MensajeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mensajes")
public class MensajeRestController {

    private final MensajeService mensajeService;

    public MensajeRestController(MensajeService mensajeService) {
        this.mensajeService = mensajeService;
    }

    @GetMapping
    public ResponseEntity<List<Mensaje>> obtenerHistorial() {
        List<Mensaje> historial = mensajeService.obtenerHistorial();
        return ResponseEntity.ok(historial);
    }
}