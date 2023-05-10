package edu.hitsz.ranking;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class InitList {
    //新建一个list.dat文件，用于存储排行榜信息
    public static void main(String[] args) {
        try {
            FileOutputStream fos = new FileOutputStream("list.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            RankingList rankingList = new RankingList(new ArrayList<Score>());
            oos.writeObject(rankingList);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
