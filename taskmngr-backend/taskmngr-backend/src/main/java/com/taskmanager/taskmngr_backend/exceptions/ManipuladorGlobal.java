package com.taskmanager.taskmngr_backend.exceptions;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.autenticação.CredenciaisInvalidasException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.autenticação.TokenCriacaoException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.autenticação.TokenInvalidoException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.comentário.ComentarioEmBrancoException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes.EquipeNaoEncontradaException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes.EquipeSemInformacaoException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.projetos.ProjetoNaoEncontradoException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.projetos.ProjetoSemInformacaoException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.tarefas.AnexoTamanhoExcedente;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.tarefas.InvalidTaskDataException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.usuário.EmailJaCadastradoException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.usuário.UsuarioNaoEncontradoException;
import com.taskmanager.taskmngr_backend.model.dto.ErroRespostaDTO;


@ControllerAdvice
public class ManipuladorGlobal {

    // Exception de Validação de Cadastro do UsuarioCadastroDTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroRespostaDTO> manipularValidacaoCampos(MethodArgumentNotValidException ex) {
        String mensagens = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> f.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ErroRespostaDTO erro = new ErroRespostaDTO("Erro de validação", mensagens);
        return ResponseEntity.badRequest().body(erro);
    }

    // CredenciaisInvalidasException
    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<ErroRespostaDTO> manipularCredenciaisInvalidas(CredenciaisInvalidasException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // TokenCriacaoException
    @ExceptionHandler(TokenCriacaoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularTokenCriacao(TokenCriacaoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // TokenInvalidoException
    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularTokenInvalido(TokenInvalidoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // UsuárioNãoEncontradoException
    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // EmailJáCadastradoException
    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularEmailJaCadastrado(EmailJaCadastradoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // ProjetoNãoEncontradoException
    @ExceptionHandler(ProjetoNaoEncontradoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularProjetoNaoEncontrado(ProjetoNaoEncontradoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // ProjetoSemInformacaoException
    @ExceptionHandler(ProjetoSemInformacaoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularProjetoSemInformacao(ProjetoSemInformacaoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // InvalidTaskDataException
    @ExceptionHandler(InvalidTaskDataException.class)
    public ResponseEntity<ErroRespostaDTO> manipularInvalidTaskData(InvalidTaskDataException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // EquipeNaoEncontradaException
    @ExceptionHandler(EquipeNaoEncontradaException.class)
    public ResponseEntity<ErroRespostaDTO> manipularEquipeNaoEncontrada(EquipeNaoEncontradaException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // EquipeSemInformacaoException
    @ExceptionHandler(EquipeSemInformacaoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularEquipeSemInformacao(EquipeSemInformacaoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // AnexoTamanhoExcedente
    @ExceptionHandler(AnexoTamanhoExcedente.class)
    public ResponseEntity<ErroRespostaDTO> manipularAnexoTamanhoExcedente(AnexoTamanhoExcedente ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(erro);
    }

    // Quando o limite do multipart do servidor é excedido (antes do controller)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErroRespostaDTO> manipularUploadExcedido(MaxUploadSizeExceededException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), "O tamanho do arquivo excede o limite permitido.");
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(erro);
    }

    // ComentarioEmBrancoException
    @ExceptionHandler(ComentarioEmBrancoException.class)
    public ResponseEntity<ErroRespostaDTO> manipularComentarioEmBranco(ComentarioEmBrancoException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), ex.getMensagem());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
}
