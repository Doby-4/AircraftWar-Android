package edu.hitsz.ranking;

import java.io.*;
import java.util.List;

public class ScoreDAOImpl implements ScoreDAO {
    private List<Score> scores;

    public ScoreDAOImpl() {
        try {
            FileInputStream fis = new FileInputStream("list.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            RankingList rankingList = (RankingList) ois.readObject();
            scores = rankingList.getScores();
            ois.close();
            fis.close();
            System.out.println("List read from file successfully: ");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addScore(Score score) {
        scores.add(score);
        System.out.println("Score added: " + score.getName() + " " + score.getScore() + " " + score.getDate());
    }

    @Override
    public void deleteScore(int row) {
        scores.remove(row);
        System.out.println("Score deleted: " + row);
    }

    @Override
    public void updateScore(Score score) {
        // TODO

    }

    @Override
    public void findScore(String name) {
        // TODO

    }

    @Override
    public List<Score> getAllScores() {
        // TODO Auto-generated method stub
        return scores;
    }

    @Override
    public void saveScore() {
        // TODO Auto-generated method stub
        try {
            FileOutputStream fos = new FileOutputStream("list.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new RankingList(scores));
            oos.close();
            fos.close();
            System.out.println("List saved to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sortScore() {
        for (int i = 0; i < scores.size(); i++) {
            for (int j = i + 1; j < scores.size(); j++) {
                if (scores.get(i).getScore() < scores.get(j).getScore()) {
                    Score temp = scores.get(i);
                    scores.set(i, scores.get(j));
                    scores.set(j, temp);
                }
            }
        }

    }
}
