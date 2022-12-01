package it.prova.triage.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;

import it.prova.triage.model.Paziente;
import it.prova.triage.model.StatoPaziente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PazienteDTO {

	private Long id;
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private LocalDate dataRegistrazione;
	private StatoPaziente stato;

	public Paziente buildPazienteModel() {
		Paziente result = Paziente.builder().id(id).nome(cognome).cognome(cognome).codiceFiscale(codiceFiscale)
				.dataRegistrazione(dataRegistrazione).stato(stato).build();

		return result;
	}

	public static PazienteDTO buildPazienteDTOFromModel(Paziente pazienteModel) {
		PazienteDTO result = PazienteDTO.builder().id(pazienteModel.id()).nome(pazienteModel.nome())
				.cognome(pazienteModel.cognome()).codiceFiscale(pazienteModel.codiceFiscale())
				.dataRegistrazione(pazienteModel.dataRegistrazione()).stato(pazienteModel.stato()).build();

		return result;
	}

	public static List<PazienteDTO> createListDTOFromModel(List<Paziente> listaPazienteModel) {
		return listaPazienteModel.stream().map(paziente -> {
			return PazienteDTO.buildPazienteDTOFromModel(paziente);
		}).collect(Collectors.toList());
	}

	public static List<Paziente> createModelListFromDTO(List<PazienteDTO> listaPazienteDTO) {
		return listaPazienteDTO.stream().map(paziente -> {
			return paziente.buildPazienteModel();
		}).collect(Collectors.toList());
	}
}
