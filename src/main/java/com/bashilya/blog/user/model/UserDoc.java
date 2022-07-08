package com.bashilya.blog.user.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Document
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDoc {

    @Id
    private ObjectId id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private Company company = new Company();
    private Address address = new Address();

    private Integer failLogin = 0;

    public static String hexPassword(String clearPassword) {
        return DigestUtils.md5DigestAsHex(clearPassword.getBytes(StandardCharsets.UTF_8));
    }




}
