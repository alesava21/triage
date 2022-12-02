package it.prova.triage.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.triage.model.Paziente;
import it.prova.triage.model.StatoPaziente;
import it.prova.triage.repository.paziente.PazienteRepository;
import it.prova.triage.web.api.exception.PazienteNonInVisitaException;
import it.prova.triage.web.api.exception.PazienteNotFounfException;

@Service
@Transactional(readOnly = true)
public class PazienteServiceImpl implements PazienteSerivice {

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

	@Override
	public void ricovera(long id) {
		Paziente result = pazienteRepository.findById(id).orElse(null);

		if (result == null)
			throw new PazienteNotFounfException("Non e stato possibile trovare un Paziente");

		if (!result.stato().equals(StatoPaziente.IN_VISITA))
			throw new PazienteNonInVisitaException("Non e possibile ricoverare un paziente che non e in visita");

		result.stato(StatoPaziente.RICOVERATO);
		result.codiceDottore(null);

		pazienteRepository.save(result);
	}

	@Override
	public void impostaCodiceDottore(String codiceFiscale, String codiceDottore) {
		Paziente result = pazienteRepository.FindBycodiceFiscale(codiceFiscale).orElse(null);

		if (result == null)
			throw new PazienteNotFounfException("paziente non trovato");

		result.codiceDottore(codiceDottore);
		pazienteRepository.save(result);

	}

	@Override
	public void dimetti(Long id) {
		Paziente result = pazienteRepository.findById(id).orElse(null);

		if (result == null)
			throw new PazienteNotFounfException("Non e stato possibile trovare un Paziente");

		if (!result.stato().equals(StatoPaziente.IN_VISITA))
			throw new PazienteNonInVisitaException("Non e possibile ricoverare un paziente che non e in visita");

		result.stato(StatoPaziente.DIMESSO);
		result.codiceDottore(null);

		pazienteRepository.save(result);
	}

}
