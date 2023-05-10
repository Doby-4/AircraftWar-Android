//package edu.hitsz.ranking;
//
//import javax.swing.table.DefaultTableModel;
//import java.util.List;
//
//public class Scores2SwingTable {
//    private List<Score> scores;
//    public DefaultTableModel tableModel;
//
//    public Scores2SwingTable(List<Score> scores) {
//        this.scores = scores;
//    }
//
//    String[] columnNames = {"Ranking", "Name", "Score", "Date"};
//    String[][] data;
//
//    //将Scores 中的数据转换为Swing Table所需要的数据
//    public void convert() {
//        data = new String[scores.size()][4];
//        for (int i = 0; i < scores.size(); i++) {
//            data[i][0] = String.valueOf(i + 1);
//            data[i][1] = scores.get(i).getName();
//            data[i][2] = String.valueOf(scores.get(i).getScore());
//            data[i][3] = String.valueOf(scores.get(i).getDate());
//        }
//        tableModel = new DefaultTableModel(data, columnNames);
//    }
//
//
//    public DefaultTableModel getTableModel() {
//        return tableModel;
//    }
//
//
//}
