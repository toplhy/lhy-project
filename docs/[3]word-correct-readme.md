### 文本纠错 word-correct
用于对字符串中的错别字识别和纠正。

#### 集成步骤
+ 添加依赖
```xml
<dependency>
    <groupId>top.lhy</groupId>
    <artifactId>lhy-plugin-word-correct</artifactId>
    <version>1.0.0</version>
</dependency>
```
+ 使用示例
```java
public class Main {

    public static void main(String[] args) {
        String originText = "我觉得西胡很美";
        MacBert4CscCorrectModel model = new MacBert4CscCorrectModel();
        CorrectDetail detail = model.correct(originText).getData();
        System.out.println(detail);
        // 输出结果
        //CorrectDetail(resultCorrect=true, originText=我觉得西胡很美, correctText=我觉得西湖很美, details=[{correctChar=湖, originChar=胡, index=4}])
    }
}
```
+ 其他说明
1. 模型下载地址：https://hf-mirror.com/shibing624/macbert4csc-base-chinese/resolve/main/onnx/model.onnx?download=true
2. 将下载的模型放置于resources目录下