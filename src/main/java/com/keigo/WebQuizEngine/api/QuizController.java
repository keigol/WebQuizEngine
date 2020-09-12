package com.keigo.WebQuizEngine.api;

import com.keigo.WebQuizEngine.QuizzesRepository;
import com.keigo.WebQuizEngine.UserRepository;
import com.keigo.WebQuizEngine.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;

@RestController()
@RequestMapping(path = "/api")
public class QuizController {

    @Autowired
    private QuizzesRepository quizzesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/quizzes")
    public ResponseEntity<List<Quiz>> getListOfQuiz() {
        return ResponseEntity.ok((List<Quiz>) quizzesRepository.findAll());
    }

    @GetMapping("/me/quizzes")
    public ResponseEntity<List<Quiz>> getUserListOfQuiz(Principal principal) {
        User principalUser = userRepository.findByUsername(principal.getName()).get();
        List<Quiz> quizzes = quizzesRepository.findByUser(principalUser);
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/quizzes/{id}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable long id) {
        return ResponseEntity.ok(quizzesRepository.findById(id).orElseThrow());
    }

    @PostMapping(path = "/quizzes", consumes = "application/json")
    public ResponseEntity<Quiz> createQuiz(@Valid @RequestBody Quiz quiz, Principal principal) {
        if (quiz.getAnswer() == null) quiz.setAnswer(new HashSet<>());
        User user = userRepository.findByUsername(principal.getName()).get();
        user.addQuiz(quiz);

        return ResponseEntity.ok(quizzesRepository.save(quiz));
    }

    @PostMapping("/quizzes/{id}/solve")
    public ResponseEntity<Answer> solveQuiz(@PathVariable long id, @RequestBody Quiz quizAnswer) {
        if (quizzesRepository.findById(id).orElseThrow().isCorrectAnswer(quizAnswer.getAnswer())) {
            return ResponseEntity.ok(new Answer(true, "Congratulations, you're right!"));
        } else {
            return ResponseEntity.ok(new Answer(false, "Wrong answer! Please, try again."));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) throws ResponseStatusException {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRoles("USER");

        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("quizzes/{id}")
    public ResponseEntity deleteQuiz(@PathVariable long id, Principal principal) {
        User principalUser = userRepository.findByUsername(principal.getName()).get();
        Quiz quiz = quizzesRepository.findById(id).orElseThrow();
        if (quiz.getUser().getId() == principalUser.getId()) {
            quizzesRepository.delete(quiz);
            return ResponseEntity.noContent().build();
        } else {
            // TODO THROW ERROR INSTEAD
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"message\":\"Unable to delete quiz\"}");
        }
    }

    // TODO
    @PatchMapping
    public ResponseEntity patchQuiz() {
        return null;
    }
}
