package com.bashilya.blog.article.controller;

import com.bashilya.blog.base.api.request.SearchRequest;
import com.bashilya.blog.base.api.response.OkResponse;
import com.bashilya.blog.base.api.response.SearchResponse;
import com.bashilya.blog.article.api.request.ArticleRequest;
import com.bashilya.blog.article.api.response.ArticleResponse;
import com.bashilya.blog.article.exception.ArticleExistException;
import com.bashilya.blog.article.exception.ArticleNotExistException;
import com.bashilya.blog.article.mapping.ArticleMapping;
import com.bashilya.blog.article.model.ArticleDoc;
import com.bashilya.blog.article.routes.ArticleApiRoutes;
import com.bashilya.blog.article.service.ArticleApiService;
import com.bashilya.blog.user.exception.UserNotExistException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "Article API")
public class ArticleApiController {
    private final ArticleApiService articleApiService;

    @PutMapping(ArticleApiRoutes.BY_ID)
    @ApiOperation(value = "Update article", notes = "Use this when you need update article info")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Article ID Invalid")
    })
    public OkResponse<ArticleResponse> updateById(
            @ApiParam(value = "Article id") @PathVariable String id,
            @RequestBody ArticleRequest articleRequest
    ) throws ArticleNotExistException {
        return OkResponse.of(ArticleMapping.getInstance().getResponseMapping().convert(
                articleApiService.update(articleRequest)
        ));

    }

    @PostMapping(ArticleApiRoutes.ROOT)
    @ApiOperation(value = "Create", notes = "Use this when you need create new article")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Article already exist")
    })
    public OkResponse<ArticleResponse> create(@RequestBody ArticleRequest request) throws ArticleExistException, UserNotExistException {

        return OkResponse.of(ArticleMapping.getInstance().getResponseMapping().convert(articleApiService.create(request)));
    }

    @GetMapping(ArticleApiRoutes.BY_ID)
    @ApiOperation(value = "Find article by ID", notes = "Use this when you need full info about article")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    public OkResponse<ArticleResponse> byId(@ApiParam(value = "Article id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(ArticleMapping.getInstance().getResponseMapping().convert(articleApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)));
    }

    @GetMapping(ArticleApiRoutes.ROOT)
    @ApiOperation(value = "Search article", notes = "Use this when you need find article by title or body")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Article not found")
    })
    public OkResponse<SearchResponse<ArticleResponse>> search(
            @ModelAttribute SearchRequest request
            ) {

        return OkResponse.of(ArticleMapping.getInstance().getSearchMapping().convert(
                articleApiService.search(request)
        ));
    }



    @DeleteMapping(ArticleApiRoutes.BY_ID)
    @ApiOperation(value = "Delete article", notes = "Use this when you need delete article")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    public OkResponse<String> deleteById(

            @ApiParam(value = "Article id") @PathVariable ObjectId id
    ) {
         articleApiService.delete(id);
         return OkResponse.of(HttpStatus.OK.toString());
    }


}
