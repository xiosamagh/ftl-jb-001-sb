package com.bashilya.blog.photo.api.request;

import com.bashilya.blog.base.api.request.SearchRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PhotoSearchRequest extends SearchRequest {

    private ObjectId albumId;

}
