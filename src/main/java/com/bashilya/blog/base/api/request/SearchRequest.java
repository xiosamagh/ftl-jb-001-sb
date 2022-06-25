package com.bashilya.blog.base.api.request;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class SearchRequest {
     protected String query = null;
     protected Integer size = 100;
     protected Long skip = 0l;
}
