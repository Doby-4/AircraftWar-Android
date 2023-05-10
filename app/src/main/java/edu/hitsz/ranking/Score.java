package edu.hitsz.ranking;

import java.io.Serializable;
import java.util.Date;

public class Score implements Serializable {
    private int score;
    private String name;
    private Date date;

    public Score(int score, String name) {
        this.score = score;
        this.name = name;
        this.date = new Date();
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Score{" + "score=" + score + ", name='" + name + '\'' + ", date=" + date + '}';
    }
}
