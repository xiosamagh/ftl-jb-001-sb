package com.bashilya.blog.base.service;

import com.bashilya.blog.auth.exceptions.AuthException;
import com.bashilya.blog.auth.exceptions.NotAccessException;
import com.bashilya.blog.auth.service.AuthService;
import com.bashilya.blog.user.model.UserDoc;
import org.bson.types.ObjectId;

public abstract class CheckAccess<T> {



    protected abstract ObjectId getOwnerFromEntity(T entity);

    protected UserDoc checkAccess(T entity) throws AuthException, NotAccessException {
        ObjectId ownerId = getOwnerFromEntity(entity);

        UserDoc owner = authService().currentUser();

        if (owner.getId().equals(ownerId) == false) {
            throw new NotAccessException();
        }
        return owner;
    }

    protected abstract AuthService authService();

}
