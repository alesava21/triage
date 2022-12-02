package it.prova.triage.repository.paziente;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.prova.triage.model.Paziente;

public interface PazienteRepository  extends CrudRepository<Paziente, Long>{
	
	Optional<Paziente> findByNomeAndCognome(String nome, String cognome);

}
