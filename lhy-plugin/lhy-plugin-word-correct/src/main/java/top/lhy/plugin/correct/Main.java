package top.lhy.plugin.correct;

import top.lhy.plugin.correct.model.MacBert4CscCorrectModel;

public class Main {

    public static void main(String[] args) {
        MacBert4CscCorrectModel model = new MacBert4CscCorrectModel();
        String str = model.correct("<span>四法解释</span>  今天很高行").getData().toString();
        System.out.println(str);
    }
}
