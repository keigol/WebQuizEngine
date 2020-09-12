package com.keigo.WebQuizEngine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.AbstractSet;
import java.util.List;
import java.util.Set;

@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // alternatively: @JsonView
    private long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Text is required")
    private String text;

    @ElementCollection
    @CollectionTable(name = "quiz_options", joinColumns = @JoinColumn(name = "quiz_id"))
    @Size(min = 2, message = "At least 2 options required")
    @NotNull
    private List<String> options;

    @ElementCollection
    @CollectionTable(name = "quiz_answers", joinColumns = @JoinColumn(name = "quiz_id"))
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Integer> answer;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    protected Quiz() {
    }

    public Quiz(long id, String title, String text, List<String> options, Set<Integer> answer, User user) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
        this.user = user;
    }

    // TODO refactor somewhere else
    public boolean isCorrectAnswer(Set<Integer> attempt) {
        return answer.equals(attempt);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public Set<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(AbstractSet<Integer> answer) {
        this.answer = answer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
