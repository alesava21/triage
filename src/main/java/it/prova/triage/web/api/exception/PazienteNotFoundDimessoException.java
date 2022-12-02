package it.prova.triage.web.api.exception;

public class PazienteNotFoundDimessoException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public PazienteNotFoundDimessoException(String message) {
		super(message);
	}

}
