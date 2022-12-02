package it.prova.triage.web.api.exception;

public class PazienteNotFounfException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public PazienteNotFounfException(String message) {
		super(message);
	}

}
