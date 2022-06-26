package com.bashilya.blog.base.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "OkResponse",description = "Template for success response")
public class OkResponse<T> {
    protected enum Status{
        SUCCESS, ERROR
    }
    @ApiModelProperty(value = "Response entity" )
    private T result;
    @ApiModelProperty(value = "Status")
    protected Status status;

    public static <T> OkResponse of(T t) {
        OkResponse okResponse = new OkResponse();
        okResponse.setStatus(Status.SUCCESS);
        okResponse.setResult(t);
        return okResponse;
    }
}
