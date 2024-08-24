package top.lhy.plugin.onnx.core.model;

import top.lhy.plugin.onnx.core.support.Response;

public abstract class OnnxTextModel<T> {

    /**
     * 模型名称
     * @return
     */
    public abstract String name();

    /**
     * 推理
     * @param text
     * @return
     */
    public abstract Response<T> reasoning(String text);

}
