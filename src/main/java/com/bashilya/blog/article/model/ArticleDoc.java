package com.bashilya.blog.article.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ArticleDoc {

    @Id
            private ObjectId id;
            private String title;
            private String body;
            private ObjectId ownerId;




}
