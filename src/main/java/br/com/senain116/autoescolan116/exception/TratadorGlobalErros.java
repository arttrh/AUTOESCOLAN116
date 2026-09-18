package br.com.senain116.autoescolan116.exception;

import br.com.senain116.autoescolan116.exception.type.aluno.AlunoNotFoundException;
import br.com.senain116.autoescolan116.exception.type.instrucao.DadosIncompletosException;
import br.com.senain116.autoescolan116.exception.type.instrucao.ValidacaoException;
import br.com.senain116.autoescolan116.exception.type.instrutor.InstrutorNotFoundException;
import br.com.senain116.autoescolan116.exception.type.usuario.SenhaIncorretaException;
import br.com.senain116.autoescolan116.exception.type.usuario.UsuarioNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@RestControllerAdvice
public class TratadorGlobalErros {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> tratarNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Stream<DadosBadRequest>> tratarBadRequest(MethodArgumentNotValidException ex) {
        log.warn("Requisição com dados inválidos: {}", ex.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList());
        List<FieldError> erros = ex.getFieldErrors();
        return ResponseEntity
                .badRequest()
                .body(erros.stream().map(DadosBadRequest::new));
    }

    @ExceptionHandler(SenhaIncorretaException.class)
    public ResponseEntity<String> tratarSenhaIncorreta(SenhaIncorretaException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<String> tratarUsuarioNotFound(UsuarioNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InstrutorNotFoundException.class)
    public ResponseEntity<String> tratarInstrutorNotFound(InstrutorNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(AlunoNotFoundException.class)
    public ResponseEntity<String> tratarAlunoNotFound(AlunoNotFoundException ex) {
        log.warn("Aluno não encontrado!");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DadosIncompletosException.class)
    public ResponseEntity<String> TratarDadosIncompletos(DadosIncompletosException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<String> TratarValidacao(ValidacaoException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> tratarDadosIncompletos(Exception ex) {
        log.error("Erro inesperado: ", ex);
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    private record DadosBadRequest(String field, String message) {
        public DadosBadRequest(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}