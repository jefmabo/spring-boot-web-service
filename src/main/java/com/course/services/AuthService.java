package com.course.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.course.dto.CredentialsDTO;
import com.course.dto.TokenDTO;
import com.course.resources.exceptions.JWTAuthenticationException;
import com.course.security.JWTUtil;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTUtil jwtUtil;

	@Transactional(readOnly = true)
	public TokenDTO authenticate(CredentialsDTO dto) {
		try {
			Authentication authToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
			authenticationManager.authenticate(authToken);
			String token = jwtUtil.generateToken(dto.getEmail());
			return new TokenDTO(dto.getEmail(), token);
		} catch (AuthenticationException e) {
			throw new JWTAuthenticationException("Bad credentials");
		}

	}
}
