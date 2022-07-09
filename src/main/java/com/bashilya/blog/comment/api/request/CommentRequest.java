package com.bashilya.blog.comment.api.request;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ApiModel(value = "CommentRequest",description = "Model for update comment")
public class CommentRequest {


            private ObjectId id;
            private ObjectId articleId;
            private String message;

}
