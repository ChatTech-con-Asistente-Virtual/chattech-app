package com.librotech.chattech.controller.ws;

import com.librotech.chattech.model.Mensaje;
import com.librotech.chattech.service.BotIAService;
import com.librotech.chattech.service.MensajeService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.concurrent.Executor;

@Controller
public class ChatSocketController {

    private final MensajeService mensajeService;
    private final BotIAService botIAService;
    private final SimpMessagingTemplate messagingTemplate;
    private final Executor chatTaskExecutor;

    public ChatSocketController(
            MensajeService mensajeService,
            BotIAService botIAService,
            SimpMessagingTemplate messagingTemplate,
            @Qualifier("chatTaskExecutor") Executor chatTaskExecutor
    ) {
        this.mensajeService = mensajeService;
        this.botIAService = botIAService;
        this.messagingTemplate = messagingTemplate;
        this.chatTaskExecutor = chatTaskExecutor;
    }

    @MessageMapping("/enviar")
    @SendTo("/tema/mensajes")
    public Mensaje procesarMensajeUsuario(Mensaje mensajeRecibido) {

        Mensaje mensajeGuardado = mensajeService.guardarMensajeUsuario(mensajeRecibido);

        chatTaskExecutor.execute(() -> {
            Mensaje respuestaIA = botIAService.generarRespuestaIA(mensajeGuardado.getContenido());
            messagingTemplate.convertAndSend("/tema/mensajes", respuestaIA);
        });

        return mensajeGuardado;
    }
}
