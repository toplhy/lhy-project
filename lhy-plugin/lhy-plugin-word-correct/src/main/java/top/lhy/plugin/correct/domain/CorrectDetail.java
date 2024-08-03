package top.lhy.plugin.correct.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CorrectDetail {

    private Boolean resultCorrect;

    private String originText;

    private String correctText;

    // index、originChar、correctChar
    private List<Map<String, Object>> details;
}
