package com.example.chatApp.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chatApp.Models.Students;

public interface AuthRepo extends JpaRepository<Students,Integer>{
	boolean existsByName(String name);
	Optional<Students> findOneByName(String name);

    List<Students> findIdByName(String name);
}
