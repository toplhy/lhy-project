package top.lhy.plugin.correct.support;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response<T> {

    private final Boolean result;
    private final T data;

    public static <T> Response<T> ok(T data) {
        return new Response(true, data);
    }

    public static <T> Response<T> fail(T data) {
        return new Response(false, data);
    }

    public static <T> Response<T> from(Boolean result, T data) {
        return new Response(result, data);
    }

    @Override
    public String toString() {
        return "Response{" +
                "result=" + result +
                ", data=" + data +
                '}';
    }
}
