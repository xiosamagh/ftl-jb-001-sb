package com.bashilya.blog.article.routes;

import com.bashilya.blog.base.routers.BaseApiRoutes;

public class ArticleApiRoutes {
    public static final String ROOT = BaseApiRoutes.V1 + "/article";
    public static final String BY_ID = ROOT + "/{id}";
}
