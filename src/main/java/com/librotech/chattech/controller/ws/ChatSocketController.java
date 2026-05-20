package com.librotech.chattech.controller.ws;

import com.librotech.chattech.model.Mensaje;
import com.librotech.chattech.service.BotIAService;
import com.librotech.chattech.service.MensajeService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatSocketController {

    private final MensajeService mensajeService;
    private final BotIAService botIAService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatSocketController(
            MensajeService mensajeService,
            BotIAService botIAService,
            SimpMessagingTemplate messagingTemplate
    ) {
        this.mensajeService = mensajeService;
        this.botIAService = botIAService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/enviar")
    @SendTo("/tema/mensajes")
    public Mensaje procesarMensajeUsuario(Mensaje mensajeRecibido) {

        Mensaje mensajeGuardado = mensajeService.guardarMensaje(mensajeRecibido);

        Thread hiloRespuestaIA = new Thread(() -> {
            Mensaje respuestaIA = botIAService.generarRespuestaIA(mensajeGuardado.getContenido());
            messagingTemplate.convertAndSend("/tema/mensajes", respuestaIA);
        });

        hiloRespuestaIA.start();

        return mensajeGuardado;
    }
}