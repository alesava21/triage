package it.prova.triage.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.prova.triage.model.Paziente;
import it.prova.triage.model.StatoPaziente;
import it.prova.triage.repository.paziente.PazienteRepository;

public class PazienteServiceImpl implements PazienteSerivice{
	
	@Autowired
	PazienteRepository pazienteRepository;

	@Override
	public List<Paziente> listAll() {
		return (List<Paziente>) pazienteRepository.findAll();
	}

	@Override
	public Paziente caricaSingoloElemento(Long id) {
		return pazienteRepository.findById(id).orElse(null);
	}

	@Override
	public void aggiorna(Paziente pazienteInstance) {
		pazienteRepository.save(pazienteInstance);
		
	}

	@Override
	public void inserisciNuovo(Paziente pazienteInstance) {
		pazienteInstance.dataRegistrazione(LocalDate.now());
		pazienteInstance.stato(StatoPaziente.INT_ATTESA_VISITA);
		pazienteRepository.save(pazienteInstance);
		
	}

	@Override
	public void rimuovi(Long idToRemove) {
		pazienteRepository.deleteById(idToRemove);
		
	}

}
