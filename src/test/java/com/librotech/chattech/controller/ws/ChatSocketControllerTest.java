package com.librotech.chattech.controller.ws;

import com.librotech.chattech.model.Mensaje;
import com.librotech.chattech.service.BotIAService;
import com.librotech.chattech.service.MensajeService;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChatSocketControllerTest {

    @Test
    void shouldSaveUserMessageAndPublishBotResponse() {
        MensajeService mensajeService = mock(MensajeService.class);
        BotIAService botIAService = mock(BotIAService.class);
        SimpMessagingTemplate messagingTemplate = mock(SimpMessagingTemplate.class);
        Executor directExecutor = Runnable::run;
        ChatSocketController controller = new ChatSocketController(
                mensajeService,
                botIAService,
                messagingTemplate,
                directExecutor
        );
        Mensaje recibido = new Mensaje("Luis", "Recomiendame un libro");
        Mensaje guardado = new Mensaje("1", "Luis", recibido.getContenido(), recibido.getFechaEnvio());
        Mensaje respuesta = new Mensaje("2", "LibroBot IA", "Lee El Quijote", recibido.getFechaEnvio());

        when(mensajeService.guardarMensajeUsuario(recibido)).thenReturn(guardado);
        when(botIAService.generarRespuestaIA(guardado.getContenido())).thenReturn(respuesta);

        Mensaje resultado = controller.procesarMensajeUsuario(recibido);

        assertSame(guardado, resultado);
        verify(mensajeService).guardarMensajeUsuario(recibido);
        verify(botIAService).generarRespuestaIA("Recomiendame un libro");
        verify(messagingTemplate).convertAndSend("/tema/mensajes", respuesta);
    }
}
