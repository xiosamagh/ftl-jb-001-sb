package com.bashilya.blog.user.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class RegistrationRequest {
    private String email;
    private String password;
}
