package edu.hitsz.ranking;

import java.io.Serializable;
import java.util.List;

public class RankingList implements Serializable {
    private List<Score> scores;

    public RankingList(List<Score> scores) {
        this.scores = scores;
    }

    public List<Score> getScores() {
        return scores;
    }

}
