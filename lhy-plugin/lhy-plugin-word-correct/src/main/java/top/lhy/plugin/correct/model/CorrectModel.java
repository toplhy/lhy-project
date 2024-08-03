package top.lhy.plugin.correct.model;

import top.lhy.plugin.correct.domain.CorrectDetail;
import top.lhy.plugin.correct.support.Response;

public interface CorrectModel {

    String name();

    Response<CorrectDetail> correct(String text);

}
