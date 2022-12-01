package it.prova.triage.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.prova.triage.dto.PazienteDTO;
import it.prova.triage.service.PazienteSerivice;

@RestController
@RequestMapping("/api/paziente")
public class PazienteController {
	
	@Autowired
	private PazienteSerivice pazienteService;
	
	@GetMapping
	public List<PazienteDTO> listAlList(){
		return PazienteDTO.createListDTOFromModel(pazienteService.listAll());
	}
}
