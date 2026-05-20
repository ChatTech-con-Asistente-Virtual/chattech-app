package com.librotech.chattech.service;

import com.librotech.chattech.model.Mensaje;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BotIAService {

    private static final String BOT_NAME = "LibroBot IA";

    private final ObjectProvider<ChatClient.Builder> chatClientBuilderProvider;
    private final MensajeService mensajeService;

    public BotIAService(
            ObjectProvider<ChatClient.Builder> chatClientBuilderProvider,
            MensajeService mensajeService
    ) {
        this.chatClientBuilderProvider = chatClientBuilderProvider;
        this.mensajeService = mensajeService;
    }

    public Mensaje generarRespuestaIA(String preguntaUsuario) {
        validarPregunta(preguntaUsuario);

        String historialMongo = construirHistorialComoTexto();
        String prompt = construirPrompt(historialMongo, preguntaUsuario);

        String respuestaTexto = generarRespuestaConIA(prompt);

        Mensaje mensajeBot = new Mensaje();
        mensajeBot.setRemitente(BOT_NAME);
        mensajeBot.setContenido(respuestaTexto);

        return mensajeService.guardarMensaje(mensajeBot);
    }

    private String construirHistorialComoTexto() {
        List<Mensaje> historial = mensajeService.obtenerHistorialReciente();

        if (historial == null || historial.isEmpty()) {
            return "No hay historial previo en la conversación.";
        }

        return historial.stream()
                .map(mensaje -> mensaje.getRemitente() + ": " + mensaje.getContenido())
                .collect(Collectors.joining("\n"));
    }

    private String construirPrompt(String historialMongo, String preguntaUsuario) {
        return """
                Eres LibroBot IA, el asistente virtual de LibroTech.
                Ayudas a bibliotecarios dentro de una sala de chat.
                Responde de forma clara, breve y útil.

                Usa el siguiente historial reciente como contexto:

                %s

                Pregunta o mensaje del usuario:
                %s
                """.formatted(historialMongo, preguntaUsuario);
    }

    private String generarRespuestaConIA(String prompt) {
        ChatClient.Builder builder = chatClientBuilderProvider.getIfAvailable();

        if (builder == null) {
            return "No puedo responder con IA en este momento porque el modelo no está configurado.";
        }

        try {
            ChatClient chatClient = builder.build();

            String respuesta = chatClient
                    .prompt()
                    .user(prompt)
                    .call()
                    .content();

            if (respuesta == null || respuesta.isBlank()) {
                return "No logré generar una respuesta en este momento.";
            }

            return respuesta;

        } catch (Exception exception) {
            return "Ocurrió un problema al generar la respuesta con IA. Intenta nuevamente.";
        }
    }

    private void validarPregunta(String preguntaUsuario) {
        if (preguntaUsuario == null || preguntaUsuario.isBlank()) {
            throw new IllegalArgumentException("La pregunta del usuario es obligatoria.");
        }
    }
}