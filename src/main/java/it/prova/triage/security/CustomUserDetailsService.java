package it.prova.triage.security;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.repository.utente.UtenteRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UtenteRepository utenteRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Utente user = utenteRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
		return new org.springframework.security.core.userdetails.User(user.username(), user.password(),
				user.isAttivo(), true, true, !user.isDisabilitato(), getAuthorities(user));

	}

	private static Collection<? extends GrantedAuthority> getAuthorities(Utente user) {
		String[] userRoles = user.ruoli().stream().map((role) -> role.codice()).toArray(String[]::new);
		Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
		return authorities;
	}

}
