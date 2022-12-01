package it.prova.triage.service;

import java.util.List;

import it.prova.triage.model.Paziente;

public interface PazienteSerivice {
	
	public List<Paziente> listAll();

	public Paziente caricaSingoloElemento(Long id);

	public void aggiorna(Paziente pazienteInstance);

	public void inserisciNuovo(Paziente pazienteInstance);

	public void rimuovi(Long idToRemove);

}
