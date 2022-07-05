package com.bashilya.blog.photo.mapping;

import com.bashilya.blog.base.api.response.SearchResponse;
import com.bashilya.blog.base.mapping.BaseMapping;
import com.bashilya.blog.photo.api.request.PhotoRequest;
import com.bashilya.blog.photo.api.response.PhotoResponse;
import com.bashilya.blog.photo.model.PhotoDoc;
import lombok.Getter;


import java.util.stream.Collectors;

@Getter
public class PhotoMapping {

    public static class RequestMapping extends BaseMapping<PhotoRequest, PhotoDoc> {
        @Override
        public PhotoDoc convert(PhotoRequest photoRequest) {
            return PhotoDoc.builder()

                    .id(photoRequest.getId())
                    .title(photoRequest.getTitle())
                    .ownerId(photoRequest.getOwnerId())
                    .albumId(photoRequest.getAlbumId())
                    .contentType(photoRequest.getContentType())
                    .build();
        }

        @Override
        public PhotoRequest unmapping(PhotoDoc photoDoc) {
            throw new RuntimeException("dont use this");
        }
    }



    public static class ResponseMapping extends BaseMapping<PhotoDoc, PhotoResponse> {
        @Override
        public PhotoResponse convert(PhotoDoc photoDoc) {
            return PhotoResponse.builder()
        .id(photoDoc.getId().toString())
        .title(photoDoc.getTitle())
        .ownerId(photoDoc.getOwnerId().toString())
        .albumId(photoDoc.getAlbumId().toString())
                    .contentType(photoDoc.getContentType())
                    .build();
        }

        @Override
        public PhotoDoc unmapping(PhotoResponse photoResponse) {
            throw new RuntimeException("dont use this");
        }
    }




    public static class SearchMapping extends BaseMapping<SearchResponse<PhotoDoc>, SearchResponse<PhotoResponse>> {
        private ResponseMapping responseMapping = new ResponseMapping();
        @Override
        public SearchResponse<PhotoResponse> convert(SearchResponse<PhotoDoc> searchResponse) {
            return SearchResponse.of(searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount());

        }

        @Override
        public SearchResponse<PhotoDoc> unmapping(SearchResponse<PhotoResponse> photoResponses) {
            throw new RuntimeException("dont use this");
        }
    }

    private final RequestMapping requestMapping = new RequestMapping();
    private final ResponseMapping responseMapping = new ResponseMapping();
    private final SearchMapping searchMapping = new SearchMapping();

    public static PhotoMapping getInstance() {
        return new PhotoMapping();
    }
}

