package com.smallbazaar.rest.service;

import static java.util.Collections.singletonList;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smallbazaar.rest.exceptions.EntityNotFoundException;
import com.smallbazaar.rest.model.User;
import com.smallbazaar.rest.repository.UserRepository;

@Service
public class UserDetailsServiceImpl  implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userRepository.findByUsername(username).orElseThrow(()-> new EntityNotFoundException(UserDetails.class, "username", username));
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true,
                true, getAuthorities(user.getRole().name()));
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(String role) {
		return singletonList(new SimpleGrantedAuthority(role));
	}

}
