package com.bashilya.blog.todoTask.service;

import com.bashilya.blog.auth.exceptions.AuthException;
import com.bashilya.blog.auth.exceptions.NotAccessException;
import com.bashilya.blog.auth.service.AuthService;
import com.bashilya.blog.base.api.request.SearchRequest;
import com.bashilya.blog.base.api.response.SearchResponse;
import com.bashilya.blog.base.service.CheckAccess;
import com.bashilya.blog.todoTask.api.request.TodoTaskRequest;
import com.bashilya.blog.todoTask.api.request.TodoTaskSearchRequest;
import com.bashilya.blog.todoTask.mapping.TodoTaskMapping;
import com.bashilya.blog.todoTask.exception.TodoTaskExistException;
import com.bashilya.blog.todoTask.exception.TodoTaskNotExistException;
import com.bashilya.blog.todoTask.model.TodoTaskDoc;
import com.bashilya.blog.todoTask.repository.TodoTaskRepository;
import com.bashilya.blog.user.exception.UserNotExistException;
import com.bashilya.blog.user.model.UserDoc;
import com.bashilya.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;


import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoTaskApiService extends CheckAccess<TodoTaskDoc> {
    private final TodoTaskRepository todoTaskRepository;
    private  final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;
    private final AuthService authService;

    public TodoTaskDoc create(TodoTaskRequest request) throws AuthException {

        UserDoc userDoc = authService.currentUser();

        TodoTaskDoc todoTaskDoc = TodoTaskMapping.getInstance().getRequestMapping().convert(request,userDoc.getId());

        todoTaskRepository.save(todoTaskDoc);

        return todoTaskDoc;
    }

    public Optional<TodoTaskDoc> findById(ObjectId id) {
        return todoTaskRepository.findById(id);
    }

    public SearchResponse<TodoTaskDoc> search(TodoTaskSearchRequest request) throws AuthException {

        UserDoc userDoc = authService.currentUser();

        Criteria criteria = Criteria.where("ownerId").is(userDoc.getId());
        if (request.getQuery() != null && request.getQuery() != "") {
            criteria = criteria.orOperator(

                    // TODO : Add Criteria
                    Criteria.where("title").regex(request.getQuery(), "i")
//                    Criteria.where("lastName").regex(request.getQuery(), "i"),
//                    Criteria.where("email").regex(request.getQuery(), "i")
            );
        }

        Query query = new Query(criteria);

        Long count = mongoTemplate.count(query,TodoTaskDoc.class);

        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<TodoTaskDoc> todoTaskDocs = mongoTemplate.find(query, TodoTaskDoc.class);
        return SearchResponse.of(todoTaskDocs,count);
    }


    public TodoTaskDoc update(TodoTaskRequest request) throws TodoTaskNotExistException, NotAccessException, AuthException {
        Optional<TodoTaskDoc> todoTaskDocOptional = todoTaskRepository.findById(request.getId());
        if (todoTaskDocOptional == null) {
            throw new TodoTaskNotExistException();
        }

        TodoTaskDoc oldDoc = todoTaskDocOptional.get();
        UserDoc userDoc = checkAccess(oldDoc);

        TodoTaskDoc todoTaskDoc = TodoTaskMapping.getInstance().getRequestMapping().convert(request,userDoc.getId());
        todoTaskDoc.setId(request.getId());
        todoTaskDoc.setOwnerId(oldDoc.getOwnerId());
        todoTaskRepository.save(todoTaskDoc);

        return todoTaskDoc;
    }

    public void delete(ObjectId id) throws NotAccessException, AuthException, ChangeSetPersister.NotFoundException {
        checkAccess(todoTaskRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new));
        todoTaskRepository.deleteById(id);
    }

    @Override
    protected ObjectId getOwnerFromEntity(TodoTaskDoc entity) {
        return entity.getOwnerId();
    }

    @Override
    protected AuthService authService() {
        return authService;
    }
}
