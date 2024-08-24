package top.lhy.plugin.onnx.correct.model;

import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import top.lhy.plugin.onnx.core.encoder.OnnxBertBiEncoder;
import top.lhy.plugin.onnx.core.model.OnnxTextModel;
import top.lhy.plugin.onnx.core.support.ModelLoader;
import top.lhy.plugin.onnx.core.support.Response;
import top.lhy.plugin.onnx.correct.domain.CorrectDetail;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CorrectOnnxTextModel extends OnnxTextModel<CorrectDetail> {

    private static final String MODEL_NAME = "macbert4csc";
    private static final OnnxBertBiEncoder MODEL;

    static {
        MODEL = ModelLoader.loadFromJar("macbert4csc.onnx", "macbert4csc-tokenizer.json");
    }

    @Override
    public String name() {
        return MODEL_NAME;
    }

    @Override
    public Response<CorrectDetail> reasoning(String text) {
        String originText = stringHandle(text);
        String correctText = "";
        // 调用模型进行结纠错
        try (OrtSession.Result result = MODEL.encode(originText)) {
            float[][] vectors = ((float[][][]) result.get(0).getValue())[0];
            INDArray indArrayLabels = Nd4j.create(vectors);
            INDArray index = Nd4j.argMax(indArrayLabels, -1);
            long[] predIndex = index.toLongVector();
            correctText = stringHandle(MODEL.decode(predIndex));
        } catch (OrtException ignore) {
            correctText = originText;
        }
        // 封装纠错结果
        return Response.ok(buildCorrectDetail(originText, correctText));
    }

    private String stringHandle(String text) {
        return Pattern.compile("\\s+").splitAsStream(text).collect(Collectors.joining());
    }

    /**
     * 封装纠错结果
     * @param originText
     * @param correctText
     * @return
     */
    private CorrectDetail buildCorrectDetail(String originText, String correctText) {
        CorrectDetail.CorrectDetailBuilder builder = CorrectDetail.builder();
        builder.originText(originText).correctText(correctText);
        builder.resultCorrect(!originText.equals(correctText));
        List<Map<String, Object>> details = new ArrayList<>();
        for (int i = 0; i < originText.length(); i++) {
            String oriChar = originText.substring(i, i+1);
            String corChar = correctText.substring(i, i+1);
            if(!oriChar.equalsIgnoreCase(corChar)) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("index", i);
                detail.put("originChar", oriChar);
                detail.put("correctChar", corChar);
                details.add(detail);
            }
        }
        builder.details(details);
        return builder.build();
    }

}
