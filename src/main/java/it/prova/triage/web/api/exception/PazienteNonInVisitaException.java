package it.prova.triage.web.api.exception;

public class PazienteNonInVisitaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PazienteNonInVisitaException(String message) {
		super(message);
	}

}
