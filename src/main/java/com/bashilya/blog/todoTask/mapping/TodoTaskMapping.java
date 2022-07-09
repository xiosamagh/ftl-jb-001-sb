package com.bashilya.blog.todoTask.mapping;

import com.bashilya.blog.base.api.response.SearchResponse;
import com.bashilya.blog.base.mapping.BaseMapping;
import com.bashilya.blog.todoTask.api.request.TodoTaskRequest;
import com.bashilya.blog.todoTask.api.response.TodoTaskResponse;
import com.bashilya.blog.todoTask.model.TodoTaskDoc;
import lombok.Getter;
import org.bson.types.ObjectId;


import java.util.stream.Collectors;

@Getter
public class TodoTaskMapping {

    public static class RequestMapping {

        public TodoTaskDoc convert(TodoTaskRequest todoTaskRequest,ObjectId ownerId) {
            return TodoTaskDoc.builder()

                    .id(todoTaskRequest.getId())
                    .title(todoTaskRequest.getTitle())
                    .ownerId(ownerId)
                    .completed(todoTaskRequest.getCompleted())
                    .files(todoTaskRequest.getFiles())
                    .build();
        }



    }


    public static class ResponseMapping extends BaseMapping<TodoTaskDoc, TodoTaskResponse> {
        @Override
        public TodoTaskResponse convert(TodoTaskDoc todoTaskDoc) {
            return TodoTaskResponse.builder()
        .id(todoTaskDoc.getId().toString())
        .title(todoTaskDoc.getTitle())
        .ownerId(todoTaskDoc.getOwnerId().toString())
        .completed(todoTaskDoc.getCompleted())
        .files(todoTaskDoc.getFiles().stream().map(ObjectId::toString).collect(Collectors.toList()))
                    .build();
        }

        @Override
        public TodoTaskDoc unmapping(TodoTaskResponse todoTaskResponse) {
            throw new RuntimeException("dont use this");
        }
    }




    public static class SearchMapping extends BaseMapping<SearchResponse<TodoTaskDoc>, SearchResponse<TodoTaskResponse>> {
        private ResponseMapping responseMapping = new ResponseMapping();
        @Override
        public SearchResponse<TodoTaskResponse> convert(SearchResponse<TodoTaskDoc> searchResponse) {
            return SearchResponse.of(searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount());

        }

        @Override
        public SearchResponse<TodoTaskDoc> unmapping(SearchResponse<TodoTaskResponse> todoTaskResponses) {
            throw new RuntimeException("dont use this");
        }
    }

    private final RequestMapping requestMapping = new RequestMapping();
    private final ResponseMapping responseMapping = new ResponseMapping();
    private final SearchMapping searchMapping = new SearchMapping();

    public static TodoTaskMapping getInstance() {
        return new TodoTaskMapping();
    }
}

