package com.taskmanager.taskmngr_backend.validator;

import com.taskmanager.taskmngr_backend.model.ProjetoModel;

public class ProjetoValidator {
    public static String validar(ProjetoModel projeto) {
		if (projeto.getProj_nome() == null || projeto.getProj_status().trim().isEmpty()) {
			return "O Nome do projeto é obrigatório";
            //se for null ou "    " o trim remove os espacos e fica so ""
            //ai ele mostra a mensagem de erro
		}
        else{
            return null;
        }
    }
}
