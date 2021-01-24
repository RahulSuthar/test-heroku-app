package com.smallbazaar.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smallbazaar.rest.model.UserDetails;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

}
