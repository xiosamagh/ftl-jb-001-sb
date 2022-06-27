package com.bashilya.blog.user.mapping;

import com.bashilya.blog.base.api.response.SearchResponse;
import com.bashilya.blog.base.mapping.BaseMapping;
import com.bashilya.blog.user.api.request.UserRequest;
import com.bashilya.blog.user.api.response.UserFullResponse;
import com.bashilya.blog.user.api.response.UserResponse;
import com.bashilya.blog.user.model.UserDoc;
import lombok.Getter;


import java.util.stream.Collectors;

@Getter
public class UserMapping {

    public static class RequestMapping extends BaseMapping<UserRequest, UserDoc> {
        @Override
        public UserDoc convert(UserRequest userRequest) {
            return UserDoc.builder()
                    .id(userRequest.getId())
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .email(userRequest.getEmail())
                    .build();
        }

        @Override
        public UserRequest unmapping(UserDoc userDoc) {
            throw new RuntimeException("dont use this");
        }
    }



    public static class ResponseMapping extends BaseMapping<UserDoc, UserResponse> {
        @Override
        public UserResponse convert(UserDoc userDoc) {
            return UserResponse.builder()
                    .id(userDoc.getId().toString())
                    .firstName(userDoc.getFirstName())
                    .lastName(userDoc.getLastName())
                    .email(userDoc.getEmail())
                    .build();
        }

        @Override
        public UserDoc unmapping(UserResponse userResponse) {
            throw new RuntimeException("dont use this");
        }
    }

    public static class ResponseFullMapping extends BaseMapping<UserDoc, UserFullResponse> {
        @Override
        public UserFullResponse convert(UserDoc userDoc) {
            return UserFullResponse.builder()
                    .id(userDoc.getId().toString())
                    .firstName(userDoc.getFirstName())
                    .lastName(userDoc.getLastName())
                    .email(userDoc.getEmail())
                    .address(userDoc.getAddress())
                    .company(userDoc.getCompany())
                    .build();
        }

        @Override
        public UserDoc unmapping(UserFullResponse userFullResponse) {
            throw new RuntimeException("dont use this");
        }


    }


    public static class SearchMapping extends BaseMapping<SearchResponse<UserDoc>, SearchResponse<UserResponse>> {
        private ResponseMapping responseMapping = new ResponseMapping();
        @Override
        public SearchResponse<UserResponse> convert(SearchResponse<UserDoc> searchResponse) {
            return SearchResponse.of(searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount());

        }

        @Override
        public SearchResponse<UserDoc> unmapping(SearchResponse<UserResponse> userResponses) {
            throw new RuntimeException("dont use this");
        }
    }

    private final RequestMapping requestMapping = new RequestMapping();
    private final ResponseMapping responseMapping = new ResponseMapping();
    private final ResponseFullMapping responseFullMapping = new ResponseFullMapping();
    private final SearchMapping searchMapping = new SearchMapping();

    public static UserMapping getInstance() {
        return new UserMapping();
    }
}
