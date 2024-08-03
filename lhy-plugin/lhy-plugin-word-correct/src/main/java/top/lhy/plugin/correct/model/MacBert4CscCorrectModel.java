package top.lhy.plugin.correct.model;

import top.lhy.plugin.correct.onnx.OnnxBertBiEncoder;

public class MacBert4CscCorrectModel extends OnnxCorrectModel {

    private static final String MODEL_NAME = "macbert4csc";
    private static final OnnxBertBiEncoder MODEL;

    static {
        MODEL = OnnxCorrectModel.loadFromJar("macbert4csc.onnx", "macbert4csc-tokenizer.json");
    }

    public MacBert4CscCorrectModel() {

    }

    @Override
    public String name() {
        return MODEL_NAME;
    }

    @Override
    public OnnxBertBiEncoder model() {
        return MODEL;
    }

}
