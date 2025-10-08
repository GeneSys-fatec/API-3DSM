package com.taskmanager.taskmngr_backend.validator;

import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;

public class ProjetoValidator {
    public static String validar(ProjetoModel projeto) {
		if (projeto.getProjNome() == null || projeto.getProjStatus().trim().isEmpty()) {
			return "O Nome do projeto é obrigatório";
            //se for null ou "    " o trim remove os espacos e fica so ""
            //ai ele mostra a mensagem de erro
		}
        else{
            return null;
        }
    }
}
