package com.bashilya.blog.photo.controller;

import com.bashilya.blog.album.exception.AlbumNotExistException;
import com.bashilya.blog.base.api.response.OkResponse;
import com.bashilya.blog.file.exception.FileExistException;
import com.bashilya.blog.photo.api.response.PhotoResponse;
import com.bashilya.blog.photo.exception.PhotoExistException;
import com.bashilya.blog.photo.mapping.PhotoMapping;
import com.bashilya.blog.photo.model.PhotoDoc;
import com.bashilya.blog.photo.routes.PhotoApiRoutes;
import com.bashilya.blog.photo.service.PhotoApiService;
import com.bashilya.blog.user.exception.UserNotExistException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Api(value = "Photo API")
public class PhotoController {
    private final PhotoApiService photoApiService;

    @PostMapping(PhotoApiRoutes.ROOT)
    @ApiOperation(value = "Create", notes = "Use this when you need create new photo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Photo already exist")
    })
    public @ResponseBody OkResponse<PhotoResponse> create(@RequestParam MultipartFile file,
                                                          @RequestParam ObjectId ownerId,
                                                          @RequestParam ObjectId albumId
    ) throws  IOException, UserNotExistException, AlbumNotExistException, PhotoExistException {

        return OkResponse.of(PhotoMapping.getInstance().getResponseMapping().convert(photoApiService.create(file,ownerId,albumId)));
    }

    @GetMapping(PhotoApiRoutes.DOWNLOAD)
    @ApiOperation(value = "Find photo by ID", notes = "Use this when you need full info about photo")

    public void byId(@ApiParam(value = "Photo id")@PathVariable ObjectId id, HttpServletResponse response) throws ChangeSetPersister.NotFoundException, IOException {
        PhotoDoc photoDoc = photoApiService.findById(id).orElseThrow();
        response.addHeader("Content-Type", photoDoc.getContentType());
        response.addHeader("Content-Disposition", ": inline;  filename=\""+photoDoc.getTitle()+"\"");
        FileCopyUtils.copy(photoApiService.downloadById(id),response.getOutputStream());
    }


}
