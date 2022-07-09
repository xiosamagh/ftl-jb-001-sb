package com.bashilya.blog.todoTask.controller;

import com.bashilya.blog.auth.exceptions.AuthException;
import com.bashilya.blog.auth.exceptions.NotAccessException;
import com.bashilya.blog.base.api.request.SearchRequest;
import com.bashilya.blog.base.api.response.OkResponse;
import com.bashilya.blog.base.api.response.SearchResponse;
import com.bashilya.blog.todoTask.api.request.TodoTaskRequest;
import com.bashilya.blog.todoTask.api.request.TodoTaskSearchRequest;
import com.bashilya.blog.todoTask.api.response.TodoTaskResponse;
import com.bashilya.blog.todoTask.exception.TodoTaskExistException;
import com.bashilya.blog.todoTask.exception.TodoTaskNotExistException;
import com.bashilya.blog.todoTask.mapping.TodoTaskMapping;
import com.bashilya.blog.todoTask.model.TodoTaskDoc;
import com.bashilya.blog.todoTask.routes.TodoTaskApiRoutes;
import com.bashilya.blog.todoTask.service.TodoTaskApiService;
import com.bashilya.blog.user.exception.UserNotExistException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "TodoTask API")
public class TodoTaskApiController {
    private final TodoTaskApiService todoTaskApiService;

    @PostMapping(TodoTaskApiRoutes.ROOT)
    @ApiOperation(value = "Create", notes = "Use this when you need create new todoTask")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "TodoTask already exist")
    })
    public OkResponse<TodoTaskResponse> create(@RequestBody TodoTaskRequest request) throws AuthException {

        return OkResponse.of(TodoTaskMapping.getInstance().getResponseMapping().convert(todoTaskApiService.create(request)));
    }

    @GetMapping(TodoTaskApiRoutes.BY_ID)
    @ApiOperation(value = "Find todoTask by ID", notes = "Use this when you need full info about todoTask")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    public OkResponse<TodoTaskResponse> byId(@ApiParam(value = "TodoTask id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(TodoTaskMapping.getInstance().getResponseMapping().convert(todoTaskApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)));
    }

    @GetMapping(TodoTaskApiRoutes.ROOT)
    @ApiOperation(value = "Search todoTask", notes = "Use this when you need find todoTask by ?????")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "TodoTask not found")
    })
    public OkResponse<SearchResponse<TodoTaskResponse>> search(
            @ModelAttribute TodoTaskSearchRequest request
            ) throws ResponseStatusException, AuthException {

        return OkResponse.of(TodoTaskMapping.getInstance().getSearchMapping().convert(
                todoTaskApiService.search(request)
        ));
    }

    @PutMapping(TodoTaskApiRoutes.BY_ID)
    @ApiOperation(value = "Update todoTask", notes = "Use this when you need update todoTask info")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "TodoTask ID Invalid")
    })
    public OkResponse<TodoTaskResponse> updateById(
            @ApiParam(value = "TodoTask id") @PathVariable String id,
            @RequestBody TodoTaskRequest todoTaskRequest
            ) throws TodoTaskNotExistException, NotAccessException, AuthException {
        return OkResponse.of(TodoTaskMapping.getInstance().getResponseMapping().convert(
                todoTaskApiService.update(todoTaskRequest)
        ));

    }

    @DeleteMapping(TodoTaskApiRoutes.BY_ID)
    @ApiOperation(value = "Delete todoTask", notes = "Use this when you need delete todoTask")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    public OkResponse<String> deleteById(

            @ApiParam(value = "TodoTask id") @PathVariable ObjectId id
    ) throws NotAccessException, AuthException, ChangeSetPersister.NotFoundException {
         todoTaskApiService.delete(id);
         return OkResponse.of(HttpStatus.OK.toString());
    }


}
