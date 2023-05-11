package com.danny.backend.usersapp.repositories;

import com.danny.backend.usersapp.models.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
