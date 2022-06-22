package com.bashilya.blog.user.routes;

import com.bashilya.blog.base.routers.BaseApiRoutes;

public class UserApiRoutes {
    public static final String ROOT = BaseApiRoutes.V1 + "/user";
    public static final String BY_ID = ROOT + "/{id}";
}
