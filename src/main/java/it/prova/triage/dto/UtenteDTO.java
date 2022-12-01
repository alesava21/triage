package it.prova.triage.dto;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

import it.prova.triage.model.Ruolo;
import it.prova.triage.model.StatoUtente;
import it.prova.triage.model.Utente;
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
public class UtenteDTO {

	private Long id;

	@NotBlank(message = "{username.notblank}")
	@Size(min = 3, max = 15, message = "Il valore inserito '${validatedValue}' deve essere lungo tra {min} e {max} caratteri")
	private String username;

	@NotBlank(message = "{password.notblank}")
	@Size(min = 8, max = 15, message = "Il valore inserito deve essere lungo tra {min} e {max} caratteri")
	private String password;

	private String confermaPassword;

	@NotBlank(message = "{nome.notblank}")
	private String nome;

	@NotBlank(message = "{cognome.notblank}")
	private String cognome;

	@NotBlank(message = "{email.notblank}")
	private String email;

	private LocalDate dateRegistrazione;

	private StatoUtente stato;

	private Long[] ruoliIds;

	public Utente buildUtenteModel(boolean includeIdRoles) {
		Utente result = Utente.builder()
				.id(id)
				.username(username)
				.password(password)
				.nome(nome)
				.cognome(cognome)
				.dataRegistrazione(dateRegistrazione)
				.stato(stato)
				.build();
		if (includeIdRoles && ruoliIds != null)
			result.ruoli(Arrays.asList(ruoliIds).stream().map(id -> Ruolo.builder().id(id).build())
					.collect(Collectors.toList()));

		return result;
	}

	public static UtenteDTO buildUtenteDTOFromModel(Utente utenteModel) {
		UtenteDTO result = UtenteDTO.builder()
				.id(utenteModel.id())
				.username(utenteModel.username())
				.nome(utenteModel.nome())
				.cognome(utenteModel.cognome())
				.dateRegistrazione(utenteModel.dataRegistrazione())
				.stato(utenteModel.stato())
				.build();
		if (!utenteModel.ruoli().isEmpty())
			result.ruoliIds = utenteModel.ruoli().stream().map(r -> r.id()).collect(Collectors.toList())
					.toArray(new Long[] {});

		return result;
	}

	public static List<UtenteDTO> createUtenteListDTOFromModel(List<Utente> utenteModelList) {
		return utenteModelList.stream().map(utente -> {
			return UtenteDTO.buildUtenteDTOFromModel(utente);
		}).collect(Collectors.toList());
	}

	public static List<Utente> createUtenteListModelFromDTO(List<UtenteDTO> utenteDTOlist) {
		return utenteDTOlist.stream().map(utente -> {
			return utente.buildUtenteModel(true);
		}).collect(Collectors.toList());
	}
}