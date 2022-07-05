package com.bashilya.blog.photo.routes;

import com.bashilya.blog.base.routers.BaseApiRoutes;

public class PhotoApiRoutes {
    public static final String ROOT = BaseApiRoutes.V1 + "/photo";
    public static final String BY_ID = ROOT + "/{id}";

    public static final String DOWNLOAD = "/photos/{id}";
}
