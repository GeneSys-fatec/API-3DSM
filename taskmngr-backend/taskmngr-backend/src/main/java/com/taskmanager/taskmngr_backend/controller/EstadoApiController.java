package com.taskmanager.taskmngr_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EstadoApiController {
    @GetMapping("/boasvindas")
	public String boasVindas() {
		return "API funcionando!";
	}
}
