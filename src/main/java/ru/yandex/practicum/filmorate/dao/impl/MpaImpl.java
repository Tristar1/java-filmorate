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
import java.util.List;

@Component
@Qualifier("dbRealisation")
public class MpaImpl implements MpaDao {

    private final Logger log = LoggerFactory.getLogger(MpaImpl.class);
    private final JdbcTemplate jdbcTemplate;


    public MpaImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpa(Integer mpaId) {
        List<Mpa> mpaList = mpasFromRowSet(jdbcTemplate.queryForRowSet(getOneMpaText(),mpaId));
        if (mpaList.isEmpty()) {
            throw new MpaNotFoundException("Не найден рейтинг с ID " + mpaId);
        }
        return mpaList.get(0);
    }

    @Override
    public List<Mpa> getMpa() {
        return getMpas();
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

    private String getMpaText() {
        return "SELECT * FROM RATINGS";
    }

    private String getOneMpaText() {
        return "SELECT * FROM RATINGS WHERE ID = ?";
    }

}
