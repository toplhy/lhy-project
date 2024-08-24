package top.lhy.plugin.onnx.core.encoder;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OrtSession.Result;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    public Result encode(String text) throws OrtException {

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

    public String decode(long[] ids) {
        return tokenizer.decode(ids, true);
    }

    public OrtEnvironment getEnvironment() {
        return environment;
    }

    public OrtSession getSession() {
        return session;
    }

    public Set<String> getExpectedInputs() {
        return expectedInputs;
    }

    public HuggingFaceTokenizer getTokenizer() {
        return tokenizer;
    }
}
