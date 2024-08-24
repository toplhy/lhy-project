package top.lhy.plugin.onnx.core.support;

import top.lhy.plugin.onnx.core.encoder.OnnxBertBiEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModelLoader {

    public static OnnxBertBiEncoder loadFromJar(String modelFileName, String tokenizerFileName) {
        InputStream model = Thread.currentThread().getContextClassLoader().getResourceAsStream(modelFileName);
        InputStream tokenizer = Thread.currentThread().getContextClassLoader().getResourceAsStream(tokenizerFileName);
        return new OnnxBertBiEncoder(model, tokenizer);
    }

    public static OnnxBertBiEncoder loadFromFileSystem(Path pathToModel, Path pathToTokenizer) {
        try {
            return new OnnxBertBiEncoder(Files.newInputStream(pathToModel), Files.newInputStream(pathToTokenizer));
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
    }
}
