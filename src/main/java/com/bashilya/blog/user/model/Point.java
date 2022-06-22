package com.bashilya.blog.user.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Point {
    private Double lat;
    private Double lng;
}
