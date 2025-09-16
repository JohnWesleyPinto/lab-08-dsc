package br.ufpb.dcx.dsc.repositorios.exceptions;

public class ResourceAlreadyExisteException extends RuntimeException {
    public ResourceAlreadyExisteException(String message) {
        super(message);
    }
}
