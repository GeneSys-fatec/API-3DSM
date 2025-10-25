package com.taskmanager.taskmngr_backend.controller.Coluna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.model.converter.ColunaConverter;
import com.taskmanager.taskmngr_backend.model.dto.ColunaDTO;
import com.taskmanager.taskmngr_backend.model.entidade.ColunaModel;
import com.taskmanager.taskmngr_backend.service.Coluna.CriaColunaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/colunas")
public class CriaColunaController {
    @Autowired
    private CriaColunaService criaColunaService;

    @Autowired
    private ColunaConverter colunaConverter;

    @PostMapping("/cadastrar")
    public ResponseEntity<ColunaDTO> criarColuna(@Valid @RequestBody ColunaDTO colunaDTO) {
       ColunaModel colunaParaSalvar = colunaConverter.dtoParaModel(colunaDTO);

        ColunaModel colunaSalva = criaColunaService.criarColuna(colunaParaSalvar);

        ColunaDTO dtoDeResposta = colunaConverter.modelParaDto(colunaSalva);

        return new ResponseEntity<>(dtoDeResposta, HttpStatus.CREATED);
    }
}
