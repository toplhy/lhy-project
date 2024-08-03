package top.lhy.plugin.correct.onnx;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.djl.modality.nlp.bert.BertFullTokenizer;
import ai.onnxruntime.*;
import ai.onnxruntime.OrtSession.Result;
import org.apache.commons.lang3.StringUtils;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import top.lhy.plugin.correct.domain.CorrectDetail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ai.onnxruntime.OnnxTensor.createTensor;
import static java.nio.LongBuffer.wrap;
import static java.util.Collections.singletonMap;

public class OnnxBertBiEncoder {

    private final OrtEnvironment environment;
    private final OrtSession session;
    private final Set<String> expectedInputs;
    private final HuggingFaceTokenizer tokenizer;

    public OnnxBertBiEncoder(InputStream model, InputStream tokenizer) {
        try {
            this.environment = OrtEnvironment.getEnvironment();
            this.session = environment.createSession(loadModel(model));
            this.expectedInputs = session.getInputNames();
            this.tokenizer = HuggingFaceTokenizer.newInstance(tokenizer, singletonMap("padding", "false"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private byte[] loadModel(InputStream modelInputStream) {
        try (
                InputStream inputStream = modelInputStream;
                ByteArrayOutputStream buffer = new ByteArrayOutputStream()
        ) {
            int nRead;
            byte[] data = new byte[1024];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            return buffer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CorrectDetail correct(String text) {
        String originText = stringHandle(text);
        String correctText = "";
        // 调用模型进行结纠错
        try (Result result = encode(originText)) {
            float[][] vectors = ((float[][][]) result.get(0).getValue())[0];
            INDArray indArrayLabels = Nd4j.create(vectors);
            INDArray index = Nd4j.argMax(indArrayLabels, -1);
            long[] predIndex = index.toLongVector();
            correctText = stringHandle(tokenizer.decode(predIndex, true));
        } catch (OrtException ignore) {
            correctText = originText;
        }
        // 封装纠错结果
        return buildCorrectDetail(originText, correctText);
    }

    private String stringHandle(String text) {
        return Pattern.compile("\\s+").splitAsStream(text).collect(Collectors.joining());
    }

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


    private Result encode(String text) throws OrtException {

        Encoding encoding = tokenizer.encode(text, true, false);

        long[] inputIds = encoding.getIds();
        long[] attentionMask = encoding.getAttentionMask();
        long[] tokenTypeIds = encoding.getTypeIds();

        long[] shape = {1, inputIds.length};

        try (
                OnnxTensor inputIdsTensor = createTensor(environment, wrap(inputIds), shape);
                OnnxTensor attentionMaskTensor = createTensor(environment, wrap(attentionMask), shape);
                OnnxTensor tokenTypeIdsTensor = createTensor(environment, wrap(tokenTypeIds), shape)
        ) {
            Map<String, OnnxTensor> inputs = new HashMap<>();
            inputs.put("input_ids", inputIdsTensor);
            inputs.put("attention_mask", attentionMaskTensor);

            if (expectedInputs.contains("token_type_ids")) {
                inputs.put("token_type_ids", tokenTypeIdsTensor);
            }

            return session.run(inputs);
        }
    }
}
