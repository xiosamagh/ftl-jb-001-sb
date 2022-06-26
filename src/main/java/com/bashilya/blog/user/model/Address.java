package com.bashilya.blog.user.model;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.context.annotation.Bean;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Address",description = "User or company address")
public class Address {
    private String city;
    private String street;
    private String suite;
    private String zipcode;
    private Point point;
}
