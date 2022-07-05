package com.bashilya.blog.comment.api.request;

import com.bashilya.blog.base.api.request.SearchRequest;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommentSearchRequest extends SearchRequest {
    @ApiParam(name = "articleId",value = "Search by article", required = false)
    private ObjectId articleId;
    @ApiParam(name = "articleId",value = "Search by user", required = false)
    private ObjectId userId;
}
