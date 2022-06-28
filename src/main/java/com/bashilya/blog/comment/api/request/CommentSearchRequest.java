package com.bashilya.blog.comment.api.request;

import com.bashilya.blog.base.api.request.SearchRequest;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class CommentSearchRequest extends SearchRequest {
    @ApiParam(name = "articleId",value = "Search by article", required = false)
    private ObjectId articleId;
    @ApiParam(name = "articleId",value = "Search by user", required = false)
    private ObjectId userId;
}
