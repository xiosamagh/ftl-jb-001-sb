package com.bashilya.blog.photo.controller;

import com.bashilya.blog.album.exception.AlbumNotExistException;
import com.bashilya.blog.base.api.request.SearchRequest;
import com.bashilya.blog.base.api.response.OkResponse;
import com.bashilya.blog.base.api.response.SearchResponse;
import com.bashilya.blog.photo.api.request.PhotoRequest;
import com.bashilya.blog.photo.api.request.PhotoSearchRequest;
import com.bashilya.blog.photo.api.response.PhotoResponse;
import com.bashilya.blog.photo.exception.PhotoExistException;
import com.bashilya.blog.photo.exception.PhotoNotExistException;
import com.bashilya.blog.photo.mapping.PhotoMapping;
import com.bashilya.blog.photo.model.PhotoDoc;
import com.bashilya.blog.photo.routes.PhotoApiRoutes;
import com.bashilya.blog.photo.service.PhotoApiService;
import com.bashilya.blog.user.exception.UserNotExistException;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor

@Api(value = "Photo API")
public class PhotoApiController {
    private final PhotoApiService photoApiService;



    @GetMapping(PhotoApiRoutes.BY_ID)
    @ApiOperation(value = "Find photo by ID", notes = "Use this when you need full info about photo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    public OkResponse<PhotoResponse> byId(@ApiParam(value = "Photo id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(PhotoMapping.getInstance().getResponseMapping().convert(photoApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)));
    }

    @GetMapping(PhotoApiRoutes.ROOT)
    @ApiOperation(value = "Search photo", notes = "Use this when you need find photo by ?????")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Photo not found")
    })
    public OkResponse<SearchResponse<PhotoResponse>> search(
            @ModelAttribute PhotoSearchRequest request
            ) {

        return OkResponse.of(PhotoMapping.getInstance().getSearchMapping().convert(
                photoApiService.search(request)
        ));
    }

    @PutMapping(PhotoApiRoutes.BY_ID)
    @ApiOperation(value = "Update photo", notes = "Use this when you need update photo info")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Photo ID Invalid")
    })
    public OkResponse<PhotoResponse> updateById(
            @ApiParam(value = "Photo id") @PathVariable String id,
            @RequestBody PhotoRequest photoRequest
            ) throws PhotoNotExistException {
        return OkResponse.of(PhotoMapping.getInstance().getResponseMapping().convert(
                photoApiService.update(photoRequest)
        ));

    }

    @DeleteMapping(PhotoApiRoutes.BY_ID)
    @ApiOperation(value = "Delete photo", notes = "Use this when you need delete photo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    public OkResponse<String> deleteById(

            @ApiParam(value = "Photo id") @PathVariable ObjectId id
    ) {
         photoApiService.delete(id);
         return OkResponse.of(HttpStatus.OK.toString());
    }


}
