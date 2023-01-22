package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Integer rate;
    @Builder.Default
    private Set<Long> likes = new HashSet<>();
    @Builder.Default
    private Set<Genre> genres = new HashSet<>();
    private Mpa mpa;
    public Integer getLikesCount(){
        return likes.size();
    }

}
