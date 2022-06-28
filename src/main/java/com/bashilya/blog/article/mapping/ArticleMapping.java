package com.bashilya.blog.article.mapping;

import com.bashilya.blog.base.api.response.SearchResponse;
import com.bashilya.blog.base.mapping.BaseMapping;
import com.bashilya.blog.article.api.request.ArticleRequest;
import com.bashilya.blog.article.api.response.ArticleResponse;
import com.bashilya.blog.article.model.ArticleDoc;
import lombok.Getter;


import java.util.stream.Collectors;

@Getter
public class ArticleMapping {

    public static class RequestMapping extends BaseMapping<ArticleRequest, ArticleDoc> {
        @Override
        public ArticleDoc convert(ArticleRequest articleRequest) {
            return ArticleDoc.builder()

                    .id(articleRequest.getId())
                    .title(articleRequest.getTitle())
                    .body(articleRequest.getBody())
                    .ownerId(articleRequest.getOwnerId())
                    .build();
        }

        @Override
        public ArticleRequest unmapping(ArticleDoc articleDoc) {
            throw new RuntimeException("dont use this");
        }
    }



    public static class ResponseMapping extends BaseMapping<ArticleDoc, ArticleResponse> {
        @Override
        public ArticleResponse convert(ArticleDoc articleDoc) {
            return ArticleResponse.builder()
        .id(articleDoc.getId().toString())
        .title(articleDoc.getTitle())
        .body(articleDoc.getBody())
        .ownerId(articleDoc.getOwnerId())
                    .build();
        }

        @Override
        public ArticleDoc unmapping(ArticleResponse articleResponse) {
            throw new RuntimeException("dont use this");
        }
    }




    public static class SearchMapping extends BaseMapping<SearchResponse<ArticleDoc>, SearchResponse<ArticleResponse>> {
        private ResponseMapping responseMapping = new ResponseMapping();
        @Override
        public SearchResponse<ArticleResponse> convert(SearchResponse<ArticleDoc> searchResponse) {
            return SearchResponse.of(searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount());

        }

        @Override
        public SearchResponse<ArticleDoc> unmapping(SearchResponse<ArticleResponse> articleResponses) {
            throw new RuntimeException("dont use this");
        }
    }

    private final RequestMapping requestMapping = new RequestMapping();
    private final ResponseMapping responseMapping = new ResponseMapping();
    private final SearchMapping searchMapping = new SearchMapping();

    public static ArticleMapping getInstance() {
        return new ArticleMapping();
    }
}

