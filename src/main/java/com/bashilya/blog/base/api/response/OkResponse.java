package com.bashilya.blog.base.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OkResponse<T> {
    protected enum Status{
        SUCCESS, ERROR
    }
    private T result;

    protected Status status;

    public static <T> OkResponse of(T t) {
        OkResponse okResponse = new OkResponse();
        okResponse.setStatus(Status.SUCCESS);
        okResponse.setResult(t);
        return okResponse;
    }
}
