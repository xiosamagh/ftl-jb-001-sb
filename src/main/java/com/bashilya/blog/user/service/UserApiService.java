package com.bashilya.blog.user.service;

import com.bashilya.blog.user.api.request.RegistrationRequest;
import com.bashilya.blog.user.exception.UserExistException;
import com.bashilya.blog.user.model.UserDoc;
import com.bashilya.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class UserApiService {
    private final UserRepository userRepository;
    private  final MongoTemplate mongoTemplate;

    public UserDoc registration(RegistrationRequest request) throws UserExistException {

        if (userRepository.findByEmail(request.getEmail()).isPresent() == true) {
            throw new UserExistException();
        }

        UserDoc userDoc = new UserDoc();
        userDoc.setEmail(request.getEmail());
        userDoc.setPassword(DigestUtils.md5DigestAsHex(request.getPassword().getBytes(StandardCharsets.UTF_8)));

        userDoc = userRepository.save(userDoc);

        return userDoc;
    }
}
