### ONNX模型运行
onnx模型运行

#### 已有功能
##### 1. 文本纠错
+ 添加依赖
```xml
<dependency>
    <groupId>top.lhy</groupId>
    <artifactId>onnx*-correct</artifactId>
    <version>1.0.0</version>
</dependency>
```
+ 使用示例
```java
public class Main {

    public static void main(String[] args) {
        String originText = "我觉得西胡很美";
        CorrectOnnxTextModel model = new CorrectOnnxTextModel();
        CorrectDetail detail = model.reasoning(originText).getData();
        System.out.println(detail);
        // 输出结果
        //CorrectDetail(resultCorrect=true, originText=我觉得西胡很美, correctText=我觉得西湖很美, details=[{correctChar=湖, originChar=胡, index=4}])
    }
}
```
+ 其他说明
  1. onnx模型过大，需自行下载后放置于resources目录下。
  2. 模型下载地址：https://hf-mirror.com/shibing624/macbert4csc-base-chinese/resolve/main/onnx/model.onnx?download=true


