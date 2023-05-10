package edu.hitsz.ranking;

import java.util.List;

public interface ScoreDAO {
    List<Score> getAllScores();

    void addScore(Score score);

    void deleteScore(int row);

    void updateScore(Score score);

    void findScore(String name);

    void saveScore();

    void sortScore();
}
