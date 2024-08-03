package top.lhy.plugin.correct.model;

import top.lhy.plugin.correct.domain.CorrectDetail;
import top.lhy.plugin.correct.onnx.OnnxBertBiEncoder;
import top.lhy.plugin.correct.support.Response;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class OnnxCorrectModel implements CorrectModel {

    protected abstract OnnxBertBiEncoder model();

    static OnnxBertBiEncoder loadFromJar(String modelFileName, String tokenizerFileName) {
        InputStream model = Thread.currentThread().getContextClassLoader().getResourceAsStream(modelFileName);
        InputStream tokenizer = Thread.currentThread().getContextClassLoader().getResourceAsStream(tokenizerFileName);
        return new OnnxBertBiEncoder(model, tokenizer);
    }

    static OnnxBertBiEncoder loadFromFileSystem(Path pathToModel, Path pathToTokenizer) {
        try {
            return new OnnxBertBiEncoder(Files.newInputStream(pathToModel), Files.newInputStream(pathToTokenizer));
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
    }

    @Override
    public Response<CorrectDetail> correct(String text) {
        CorrectDetail result = model().correct(text);
        return Response.ok(result);
    }
}
