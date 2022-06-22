package com.bashilya.blog.user.api.response;

import com.bashilya.blog.user.model.Address;
import com.bashilya.blog.user.model.Company;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UserFullResponse extends UserResponse {
    private Address address;
    private Company company;
}
