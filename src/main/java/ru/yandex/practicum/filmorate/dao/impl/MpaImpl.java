package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Qualifier("dbRealisation")
public class MpaImpl implements MpaDao {

    private final HashMap<Integer, Mpa> mpaMap;
    private final Logger log = LoggerFactory.getLogger(MpaImpl.class);
    private final JdbcTemplate jdbcTemplate;


    public MpaImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        mpaMap = new HashMap<>();
        try {
            checkMpa();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Mpa getMpa(Integer mpaId) {
        if (mpaMap.get(mpaId) == null){
            validateMpa(Mpa.builder().id(mpaId).build());
        }
        return mpaMap.get(mpaId);
    }

    @Override
    public void validateMpa(Mpa mpa) {
        Integer mpaId = mpa.getId();
        if (mpaId <0 || mpaId > 5) {
            throw new MpaNotFoundException("Не найден соответствующий рейтинг!");
        }
        if (mpa.getName() == null || mpa.getName().isEmpty()){
            mpa.setName(getMpaName(mpaId));
        }
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(validateMpaText(), mpaId);
        if (!mpaRows.next()) {
            jdbcTemplate.update(createMpaText(), mpaId, getMpaName(mpaId));
        }

        mpaMap.putIfAbsent(mpaId, mpa);
    }

    @Override
    public List<Mpa> getMpa() {
        List<Mpa> mpaList = getMpas();
        for (Mpa mpa: mpaList){
            mpaMap.putIfAbsent(mpa.getId(),mpa);
        }
        return mpaList;
    }

    private void checkMpa(){
        for (int i = 1; i < 6; i++){
            Mpa mpa = getMpa(i);
        }
    }

    public List<Mpa> getMpas(){
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(getMpaText());
        return mpasFromRowSet(filmRows);
    }

    public List<Mpa> mpasFromRowSet(SqlRowSet sqlRowSet) {

        List<Mpa> mpaList = new ArrayList<>();

        while (sqlRowSet.next()) {
            Mpa mpa = Mpa.builder()
                    .id(sqlRowSet.getInt("id"))
                    .name(sqlRowSet.getString("name"))
                    .build();

            log.info("Найден mpa: {} {}", mpa.getId(), mpa.getName());
            mpaList.add(mpa);
        }
        return mpaList;
    }

    private String getMpaName(Integer mpaId) {
        String name = "unknown";
        switch (mpaId) {
            case 1:
                name = "G";
                break;
            case 2:
                name = "PG";
                break;
            case 3:
                name = "PG-13";
                break;
            case 4:
                name = "R";
                break;
            case 5:
                name = "NC-17";
                break;
        }

        return name;
    }

    private String createMpaText() {
        return "INSERT INTO RATINGS (ID, NAME) " +
                "VALUES \n" +
                "(?,?)";
    }

    private String validateMpaText() {
        return "SELECT * FROM RATINGS WHERE ID = ?";
    }

    private String getMpaText() {
        return "SELECT * FROM RATINGS";
    }

}
