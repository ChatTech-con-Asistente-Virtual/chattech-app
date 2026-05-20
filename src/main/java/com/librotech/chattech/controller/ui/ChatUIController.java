package com.librotech.chattech.controller.ui;

import com.librotech.chattech.service.MensajeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatUIController {

    private final MensajeService mensajeService;

    public ChatUIController(MensajeService mensajeService) {
        this.mensajeService = mensajeService;
    }

    @GetMapping("/admin/chat")
    public String mostrarSalaChat(Model model) {
        model.addAttribute("historial", mensajeService.obtenerHistorial());
        return "chat/sala";
    }
}