package it.prova.triage.web.api;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import it.prova.triage.dto.DottorePazienteReqDTO;
import it.prova.triage.dto.DottorePazienteRespDTO;
import it.prova.triage.dto.PazienteDTO;
import it.prova.triage.model.Paziente;
import it.prova.triage.model.StatoPaziente;
import it.prova.triage.service.PazienteSerivice;
import it.prova.triage.web.api.exception.IdNotNullForInsertException;
import it.prova.triage.web.api.exception.PazienteNotFounfException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/paziente")
public class PazienteController {
	
private static final Logger LOGGER = LogManager.getLogger(AssegnaPazienteController.class);
	
	@Autowired
	private PazienteSerivice pazienteService;
	
	@Autowired
	private WebClient webClient;
	
	@GetMapping
	public List<PazienteDTO> listAlList(){
		return PazienteDTO.createListDTOFromModel(pazienteService.listAll());
	}
	
	@GetMapping("/{id}")
	public PazienteDTO findById(@PathVariable(required = true) Long id) {
		Paziente pazienteDaCaricare = pazienteService.caricaSingoloElemento(id);
		
		if(pazienteDaCaricare == null)
			throw new PazienteNotFounfException("paziente non trovato");
		
		return PazienteDTO.buildPazienteDTOFromModel(pazienteDaCaricare);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PazienteDTO inserisci(@RequestBody PazienteDTO paziente) {
		if(paziente.getId() != null)
			throw new IdNotNullForInsertException("impossibile aggiornare un record se viene inserito anche l'id");
		
		return PazienteDTO.buildPazienteDTOFromModel(pazienteService.inserisciNuovo(paziente.buildPazienteModel()));
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@RequestBody PazienteDTO paziente) {
		if(paziente.getId() == null)
			throw new IdNotNullForInsertException("impossibile aggiornare un campo se non si inserisce l'id");
		
		pazienteService.aggiorna(paziente.buildPazienteModel());
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT) 
	public void delete(@PathVariable(required = true) Long id) {
		Paziente pazienteDaEliminare = pazienteService.caricaSingoloElemento(id);
		
		if(pazienteDaEliminare == null)
			throw new PazienteNotFounfException("paziente non trovato");
		if(!pazienteDaEliminare.stato().equals(StatoPaziente.DIMESSO))
			throw new PazienteNotFounfException("impossibile eliminare un paziente non dimesso");
			
		pazienteService.rimuovi(id);
	}
	
	@PostMapping("/ricovera/{id}")
	public DottorePazienteRespDTO ricoveraPaziente(@PathVariable(required = true) Long id,@RequestBody DottorePazienteReqDTO dottore) {
		LOGGER.info(".........invocazione servizio esterno............");
		
		pazienteService.ricovera(id);
		
		ResponseEntity<DottorePazienteRespDTO> response = webClient.post().uri("/ricovera")
				.body(Mono.just(DottorePazienteReqDTO.builder().codiceDottore(dottore.getCodiceDottore())
						.codFiscalePazienteAttualmenteInVisita(dottore.getCodFiscalePazienteAttualmenteInVisita())
						.build()), DottorePazienteReqDTO.class)
				.retrieve().toEntity(DottorePazienteRespDTO.class).block();
		
		LOGGER.info(".........invocazione servizio esterno completata............");
		
		return DottorePazienteRespDTO.builder().codiceDottore(response.getBody().getCodiceDottore())
				.codFiscalePazienteAttualmenteInVisita(response.getBody().getCodFiscalePazienteAttualmenteInVisita()).build();
	}
	
	@PostMapping("/dimetti/{id}")
	public DottorePazienteRespDTO dimettiPaziente(@PathVariable(required = true) Long id,@RequestBody DottorePazienteReqDTO dottore) {
		LOGGER.info(".........invocazione servizio esterno............");
		
		pazienteService.dimetti(id);
		
		ResponseEntity<DottorePazienteRespDTO> response = webClient.post().uri("/ricovera")
				.body(Mono.just(DottorePazienteReqDTO.builder().codiceDottore(dottore.getCodiceDottore())
						.codFiscalePazienteAttualmenteInVisita(dottore.getCodFiscalePazienteAttualmenteInVisita())
						.build()), DottorePazienteReqDTO.class)
				.retrieve().toEntity(DottorePazienteRespDTO.class).block();
		
		LOGGER.info(".........invocazione servizio esterno completata............");
		
		return DottorePazienteRespDTO.builder().codiceDottore(response.getBody().getCodiceDottore())
				.codFiscalePazienteAttualmenteInVisita(response.getBody().getCodFiscalePazienteAttualmenteInVisita()).build();
	}
}
