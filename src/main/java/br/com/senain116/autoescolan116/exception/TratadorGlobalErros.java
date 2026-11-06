package br.com.senain116.autoescolan116.exception;
import br.com.senain116.autoescolan116.exception.type.aluno.AlunoNotFoundException;
import br.com.senain116.autoescolan116.exception.type.instrucao.*;
import br.com.senain116.autoescolan116.exception.type.instrutor.InstrutorNotFoundException;
import br.com.senain116.autoescolan116.exception.type.usuario.*;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestControllerAdvice
public class TratadorGlobalErros {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Map<String, String>>> dadosInvalidos(
        MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(ex.getFieldErrors()
                .stream()
                .map(e
                    -> Map.of("field", e.getField(), "message",
                        Objects.toString(e.getDefaultMessage(), "Valor inválido")))
                .toList());
    }
    @ExceptionHandler({org.springframework.http.converter.HttpMessageNotReadableException.class,
        org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class,
        org.springframework.web.bind.MissingServletRequestParameterException.class})
    public ResponseEntity<Map<String, String>> requisicaoInvalida() {
        return ResponseEntity.badRequest().body(
            Map.of("message", "Confira os campos e o formato dos dados enviados."));
    }
    @ExceptionHandler({AlunoNotFoundException.class, InstrutorNotFoundException.class,
        UsuarioNotFoundException.class, jakarta.persistence.EntityNotFoundException.class})
    public ResponseEntity<Map<String, String>> naoEncontrado(RuntimeException ex) {
        return ResponseEntity.status(404).body(Map.of("message", "Cadastro não encontrado"));
    }
    @ExceptionHandler({DadosIncompletosException.class, ValidacaoException.class,
        SenhaIncorretaException.class, IllegalArgumentException.class})
    public ResponseEntity<Map<String, String>> validacao(RuntimeException ex) {
        return ResponseEntity.unprocessableEntity().body(Map.of("message", ex.getMessage()));
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> duplicado() {
        return ResponseEntity.status(409).body(Map.of(
            "message", "Já existe cadastro com estes dados. Confira e-mail, CPF, CNH ou usuário."));
    }
    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<Map<String, String>> login() {
        return ResponseEntity.status(401).body(Map.of("message", "Credenciais inválidas"));
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> negado() {
        return ResponseEntity.status(403).body(Map.of("message", "Acesso negado"));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> inesperado(Exception ex) {
        log.error("Falha interna na operação", ex);
        return ResponseEntity.internalServerError().body(
            Map.of("message", "Não foi possível concluir a operação."));
    }
}
