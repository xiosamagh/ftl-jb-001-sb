package com.bashilya.blog.file.mapping;

import com.bashilya.blog.base.api.response.SearchResponse;
import com.bashilya.blog.base.mapping.BaseMapping;
import com.bashilya.blog.file.api.response.FileResponse;
import com.bashilya.blog.file.model.FileDoc;
import lombok.Getter;


import java.util.stream.Collectors;

@Getter
public class FileMapping {





    public static class ResponseMapping extends BaseMapping<FileDoc, FileResponse> {
        @Override
        public FileResponse convert(FileDoc fileDoc) {
            return FileResponse.builder()
        .id(fileDoc.getId().toString())
        .title(fileDoc.getTitle())
                    .contentType(fileDoc.getContentType())
        .ownerId(fileDoc.getOwnerId().toString())

                    .build();
        }

        @Override
        public FileDoc unmapping(FileResponse fileResponse) {
            throw new RuntimeException("dont use this");
        }
    }




    public static class SearchMapping extends BaseMapping<SearchResponse<FileDoc>, SearchResponse<FileResponse>> {
        private ResponseMapping responseMapping = new ResponseMapping();
        @Override
        public SearchResponse<FileResponse> convert(SearchResponse<FileDoc> searchResponse) {
            return SearchResponse.of(searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount());

        }

        @Override
        public SearchResponse<FileDoc> unmapping(SearchResponse<FileResponse> fileResponses) {
            throw new RuntimeException("dont use this");
        }
    }


    private final ResponseMapping responseMapping = new ResponseMapping();
    private final SearchMapping searchMapping = new SearchMapping();

    public static FileMapping getInstance() {
        return new FileMapping();
    }
}

