package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {
    Integer id;
    String name;
    String description;
    LocalDate releaseDate;
    int duration;
    Set<Long> likes;
    public Integer getLikesCount(){
        return likes.size();
    }
}
