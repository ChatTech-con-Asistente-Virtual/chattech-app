package com.librotech.chattech.service;

import com.librotech.chattech.model.Mensaje;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BotIAServiceTest {

    @SuppressWarnings("unchecked")
    @Test
    void shouldReturnFallbackWhenAiIsNotConfigured() {
        ObjectProvider<ChatClient.Builder> provider = mock(ObjectProvider.class);
        MensajeService mensajeService = mock(MensajeService.class);
        BotIAService botIAService = new BotIAService(provider, mensajeService);
        Mensaje pregunta = new Mensaje("Luis", "Hola");

        when(mensajeService.obtenerHistorialReciente()).thenReturn(List.of(pregunta));
        when(provider.getIfAvailable()).thenReturn(null);
        when(mensajeService.guardarMensaje(any(Mensaje.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Mensaje resultado = botIAService.generarRespuestaIA("Hola");

        assertEquals("LibroBot IA", resultado.getRemitente());
        assertEquals(
                "No puedo responder con IA en este momento porque el modelo no está configurado.",
                resultado.getContenido()
        );
        verify(mensajeService).guardarMensaje(resultado);
    }
}
