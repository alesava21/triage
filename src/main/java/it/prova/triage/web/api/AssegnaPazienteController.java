package it.prova.triage.web.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import it.prova.triage.dto.DottorePazienteReqDTO;
import it.prova.triage.dto.DottorePazienteRespDTO;
import it.prova.triage.service.PazienteSerivice;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/assegnapaziente")
public class AssegnaPazienteController {

	private static final Logger LOGGER = LogManager.getLogger(AssegnaPazienteController.class);

	@Autowired
	private WebClient webClient;

	@Autowired
	private PazienteSerivice pazienteService;

	@GetMapping("/{cd}")
	public DottorePazienteRespDTO verificaDisponibilitaDottore(@PathVariable(required = true) String cd) {

		LOGGER.info("invoco il servizio esterno");

		DottorePazienteRespDTO dottoreResponseDTO = webClient.get().uri("/verificaDisponibilitaDottore/" + cd)
				.retrieve().bodyToMono(DottorePazienteRespDTO.class).block();

		LOGGER.info("invoco il servizio esterno COMPLETATA");

		return dottoreResponseDTO;
	}

	@PostMapping("/impostaVisita")
	public DottorePazienteRespDTO impostaVisita(@RequestBody DottorePazienteReqDTO dottore) {

		pazienteService.impostaCodiceDottore(dottore.getCodFiscalePazienteAttualmenteInVisita(),
				dottore.getCodiceDottore());

		LOGGER.info("invoco il servizio esterno");

		ResponseEntity<DottorePazienteRespDTO> response = webClient
				.post().uri("/impostaVisita").body(
						Mono.just(DottorePazienteReqDTO.builder().codiceDottore(dottore.getCodiceDottore())
								.codFiscalePazienteAttualmenteInVisita(
										dottore.getCodFiscalePazienteAttualmenteInVisita())
								.build()),
						DottorePazienteRespDTO.class)
				.retrieve().toEntity(DottorePazienteRespDTO.class).block();

		LOGGER.info("invoco il servizio esterno COMPLETATA");

		return DottorePazienteRespDTO.builder().codiceDottore(response.getBody().getCodiceDottore())
				.codFiscalePazienteAttualmenteInVisita(response.getBody().getCodFiscalePazienteAttualmenteInVisita())
				.build();
	}
}
