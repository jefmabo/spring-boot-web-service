package com.course.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.course.dto.CredentialsDTO;
import com.course.dto.TokenDTO;
import com.course.entities.Order;
import com.course.entities.User;
import com.course.repositories.UserRepository;
import com.course.security.JWTUtil;
import com.course.services.exceptions.JWTAuthenticationException;
import com.course.services.exceptions.JWTAuthorizationException;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

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

	public User authenticated() {
		try {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			return userRepository.findByEmail(userDetails.getUsername());
		} catch (Exception e) {
			throw new JWTAuthorizationException("Access denied");
		}
	}

	public void validadeSelfOrAdmin(Long userId) {
		User user = authenticated();
		if (user == null || (!user.getId().equals(userId) && !user.hasRole("ROLE_ADMIN"))) {
			throw new JWTAuthorizationException("Access denied");
		}
	}

	public void validadeOwnOrderOrAdmin(Order order) {
		User user = authenticated();
		if (user == null || (!user.getId().equals(order.getClient().getId()) && !user.hasRole("ROLE_ADMIN"))) {
			throw new JWTAuthorizationException("Access denied");
		}
	}

	public TokenDTO refreshToken() {
		User user = authenticated();
		return new TokenDTO(user.getEmail(), jwtUtil.generateToken(user.getEmail()));
	}

}
