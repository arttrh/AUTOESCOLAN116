package br.com.senain116.autoescolan116.exception.type.usuario;

public class SenhaIncorretaException extends RuntimeException {
    public SenhaIncorretaException(String message) {
        super(message);
    }
}