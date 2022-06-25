package com.bashilya.blog.user.controller;

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
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserApiService userApiService;

    @PostMapping(UserApiRoutes.ROOT)
    public UserFullResponse registration(@RequestBody RegistrationRequest request) throws UserExistException {
        return UserMapping.getInstance().getResponseFullMapping().convert(userApiService.registration(request));
    }

    @GetMapping(UserApiRoutes.BY_ID)
    public UserFullResponse byId(@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
        return UserMapping.getInstance().getResponseFullMapping().convert(userApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new));
    }

    @GetMapping(UserApiRoutes.ROOT)
    public List<UserResponse> search(
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "1") Integer size,
            @RequestParam(required = false, defaultValue = "0") Long skip
    ) {

        return UserMapping.getInstance().getSearchMapping().convert(
                userApiService.search(query,size,skip)
        );
    }

    @PutMapping(UserApiRoutes.BY_ID)
    public UserFullResponse updateById(
            @PathVariable String id,
            @RequestBody UserRequest userRequest
            ) throws UserNotExistException {
        return UserMapping.getInstance().getResponseFullMapping().convert(
                userApiService.update(userRequest)
        );

    }

    @DeleteMapping(UserApiRoutes.BY_ID)
    public String deleteById(
            @PathVariable ObjectId id
    ) {
         userApiService.delete(id);
         return HttpStatus.OK.toString();
    }


}
