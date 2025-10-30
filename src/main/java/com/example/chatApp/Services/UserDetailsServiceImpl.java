package com.example.chatApp.Services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.chatApp.Models.Students;
import com.example.chatApp.Repository.AuthRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final AuthRepo authrepo;
	UserDetailsServiceImpl(AuthRepo authrepo){
		this.authrepo = authrepo;
		
	}
	@Override
	public boolean existByName(String name) {
		return this.authrepo.existsByName(name);
	}

	@Override
	public Optional<Students> findByName(String name) {
		// TODO Auto-generated method stub
		if(!existByName(name)) {
			return Optional.empty();
		}
		return this.authrepo.findOneByName(name);
	}

}
