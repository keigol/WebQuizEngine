package com.keigo.WebQuizEngine;

import com.keigo.WebQuizEngine.model.Quiz;
import com.keigo.WebQuizEngine.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// TODO renamed to pass tests
@Repository
public interface QuizzesRepository extends CrudRepository<Quiz, Long> {
    List<Quiz> findByUser(User user);
}
