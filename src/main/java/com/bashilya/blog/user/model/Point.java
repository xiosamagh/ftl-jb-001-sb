package com.bashilya.blog.user.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(value = "Point", description = "Coordinates")
public class Point {
    private Double lat;
    private Double lng;
}
