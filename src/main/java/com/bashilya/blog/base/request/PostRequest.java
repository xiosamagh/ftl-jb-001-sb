package com.bashilya.blog.base.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class PostRequest {
    @Getter
    @Setter
    @ToString
    public static class Inner{
        private String param;
    }

    private String param;
    private Integer paramInt;
    private Double paramDouble;
    private Inner paramInner;
}
