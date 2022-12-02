package it.prova.triage.service;

import java.util.List;

import it.prova.triage.model.Paziente;

public interface PazienteSerivice {
	
	public List<Paziente> listAll();

	public Paziente caricaSingoloElemento(Long id);

	public void aggiorna(Paziente pazienteInstance);

	public Paziente inserisciNuovo(Paziente pazienteInstance);

	public void rimuovi(Long idToRemove);
	
	public void ricovera(long id);
	
	public void impostaCodiceDottore(String codiceFiscale, String codiceDottore);
	
	public void dimetti (Long id);

}
