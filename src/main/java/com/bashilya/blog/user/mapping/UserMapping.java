package com.bashilya.blog.user.mapping;

import com.bashilya.blog.base.mapping.BaseMapping;
import com.bashilya.blog.user.api.response.UserFullResponse;
import com.bashilya.blog.user.api.response.UserResponse;
import com.bashilya.blog.user.model.UserDoc;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserMapping {
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


    public static class SearchMapping extends BaseMapping<List<UserDoc>, List<UserResponse>> {
        private ResponseMapping responseMapping = new ResponseMapping();
        @Override
        public List<UserResponse> convert(List<UserDoc> userDocs) {
            return userDocs.stream().map(responseMapping::convert).collect(Collectors.toList());
        }

        @Override
        public List<UserDoc> unmapping(List<UserResponse> userResponses) {
            throw new RuntimeException("dont use this");
        }
    }
    private final ResponseMapping responseMapping = new ResponseMapping();
    private final ResponseFullMapping responseFullMapping = new ResponseFullMapping();
    private final SearchMapping searchMapping = new SearchMapping();

    public static UserMapping getInstance() {
        return new UserMapping();
    }
}
