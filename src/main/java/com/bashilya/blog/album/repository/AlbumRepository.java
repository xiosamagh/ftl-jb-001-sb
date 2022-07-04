package com.bashilya.blog.album.repository;

import com.bashilya.blog.album.model.AlbumDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlbumRepository extends MongoRepository<AlbumDoc, ObjectId> {


}
