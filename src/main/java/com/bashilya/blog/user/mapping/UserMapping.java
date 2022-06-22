package com.bashilya.blog.user.mapping;

import com.bashilya.blog.user.api.response.UserResponse;
import com.bashilya.blog.user.model.UserDoc;
import lombok.Getter;

@Getter
public class UserMapping {
    public static class ResponseMapping{
        public UserResponse convert(UserDoc userDoc) {
            return UserResponse.builder()
                    .id(userDoc.getId().toString())
                    .firstName(userDoc.getFirstName())
                    .lastName(userDoc.getLastName())
                    .email(userDoc.getEmail())
                    .build();
        }
    }

    private final ResponseMapping responseMapping = new ResponseMapping();

    public static UserMapping getInstance() {
        return new UserMapping();
    }
}
