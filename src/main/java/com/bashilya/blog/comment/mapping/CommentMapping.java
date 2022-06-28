package com.bashilya.blog.comment.mapping;

import com.bashilya.blog.base.api.response.SearchResponse;
import com.bashilya.blog.base.mapping.BaseMapping;
import com.bashilya.blog.comment.api.request.CommentRequest;
import com.bashilya.blog.comment.api.response.CommentResponse;
import com.bashilya.blog.comment.model.CommentDoc;
import lombok.Getter;


import java.util.stream.Collectors;

@Getter
public class CommentMapping {

    public static class RequestMapping extends BaseMapping<CommentRequest, CommentDoc> {
        @Override
        public CommentDoc convert(CommentRequest commentRequest) {
            return CommentDoc.builder()

                    .id(commentRequest.getId())
                    .articleId(commentRequest.getArticleId())
                    .userId(commentRequest.getUserId())
                    .message(commentRequest.getMessage())
                    .build();
        }

        @Override
        public CommentRequest unmapping(CommentDoc commentDoc) {
            throw new RuntimeException("dont use this");
        }
    }



    public static class ResponseMapping extends BaseMapping<CommentDoc, CommentResponse> {
        @Override
        public CommentResponse convert(CommentDoc commentDoc) {
            return CommentResponse.builder()
        .id(commentDoc.getId().toString())
        .articleId(commentDoc.getArticleId().toString())
        .userId(commentDoc.getUserId().toString())
        .message(commentDoc.getMessage())
                    .build();
        }

        @Override
        public CommentDoc unmapping(CommentResponse commentResponse) {
            throw new RuntimeException("dont use this");
        }
    }




    public static class SearchMapping extends BaseMapping<SearchResponse<CommentDoc>, SearchResponse<CommentResponse>> {
        private ResponseMapping responseMapping = new ResponseMapping();
        @Override
        public SearchResponse<CommentResponse> convert(SearchResponse<CommentDoc> searchResponse) {
            return SearchResponse.of(searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount());

        }

        @Override
        public SearchResponse<CommentDoc> unmapping(SearchResponse<CommentResponse> commentResponses) {
            throw new RuntimeException("dont use this");
        }
    }

    private final RequestMapping requestMapping = new RequestMapping();
    private final ResponseMapping responseMapping = new ResponseMapping();
    private final SearchMapping searchMapping = new SearchMapping();

    public static CommentMapping getInstance() {
        return new CommentMapping();
    }
}

