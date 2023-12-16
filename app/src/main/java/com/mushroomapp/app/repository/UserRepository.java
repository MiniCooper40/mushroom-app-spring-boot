package com.mushroomapp.app.repository;

import com.mushroomapp.app.model.profile.User;
import org.hibernate.annotations.SQLInsert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query(value = "SELECT u FROM User u WHERE u.token = :token")
    User getReferenceByToken(@Param("token") String token);
}
