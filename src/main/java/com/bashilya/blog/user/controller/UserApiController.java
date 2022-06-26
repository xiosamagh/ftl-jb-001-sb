package com.bashilya.blog.user.controller;

import com.bashilya.blog.base.api.request.SearchRequest;
import com.bashilya.blog.base.api.response.OkResponse;
import com.bashilya.blog.base.api.response.SearchResponse;
import com.bashilya.blog.user.api.request.RegistrationRequest;
import com.bashilya.blog.user.api.request.UserRequest;
import com.bashilya.blog.user.api.response.UserFullResponse;
import com.bashilya.blog.user.api.response.UserResponse;
import com.bashilya.blog.user.exception.UserExistException;
import com.bashilya.blog.user.exception.UserNotExistException;
import com.bashilya.blog.user.mapping.UserMapping;
import com.bashilya.blog.user.model.UserDoc;
import com.bashilya.blog.user.routes.UserApiRoutes;
import com.bashilya.blog.user.service.UserApiService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "User API")
public class UserApiController {
    private final UserApiService userApiService;

    @PostMapping(UserApiRoutes.ROOT)
    @ApiOperation(value = "Register", notes = "Use this when you need register and create new user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "User already exist")
    })
    public OkResponse<UserFullResponse> registration(@RequestBody RegistrationRequest request) throws UserExistException {

        return OkResponse.of(UserMapping.getInstance().getResponseFullMapping().convert(userApiService.registration(request)));
    }

    @GetMapping(UserApiRoutes.BY_ID)
    @ApiOperation(value = "Find user by ID", notes = "Use this when you need full info about user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    public OkResponse<UserFullResponse> byId(@ApiParam(value = "User id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(UserMapping.getInstance().getResponseFullMapping().convert(userApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)));
    }

    @GetMapping(UserApiRoutes.ROOT)
    @ApiOperation(value = "Search user", notes = "Use this when you need find user by last name, first name or email")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "User not found")
    })
    public OkResponse<SearchResponse<UserResponse>> search(
            @ModelAttribute SearchRequest request
            ) {

        return OkResponse.of(UserMapping.getInstance().getSearchMapping().convert(
                userApiService.search(request)
        ));
    }

    @PutMapping(UserApiRoutes.BY_ID)
    @ApiOperation(value = "Update user", notes = "Use this when you need update user info")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "User ID Invalid")
    })
    public OkResponse<UserFullResponse> updateById(
            @ApiParam(value = "User id") @PathVariable String id,
            @RequestBody UserRequest userRequest
            ) throws UserNotExistException {
        return OkResponse.of(UserMapping.getInstance().getResponseFullMapping().convert(
                userApiService.update(userRequest)
        ));

    }

    @DeleteMapping(UserApiRoutes.BY_ID)
    @ApiOperation(value = "Delete user", notes = "Use this when you need delete user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    public OkResponse<String> deleteById(

            @ApiParam(value = "User id") @PathVariable ObjectId id
    ) {
         userApiService.delete(id);
         return OkResponse.of(HttpStatus.OK.toString());
    }


}
