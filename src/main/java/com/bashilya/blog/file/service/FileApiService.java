package com.bashilya.blog.file.service;

import com.bashilya.blog.base.api.request.SearchRequest;
import com.bashilya.blog.base.api.response.SearchResponse;
import com.bashilya.blog.file.mapping.FileMapping;
import com.bashilya.blog.file.exception.FileExistException;
import com.bashilya.blog.file.exception.FileNotExistException;
import com.bashilya.blog.file.model.FileDoc;
import com.bashilya.blog.file.repository.FileRepository;
import com.bashilya.blog.user.exception.UserNotExistException;
import com.bashilya.blog.user.repository.UserRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileApiService {
    private final FileRepository fileRepository;
    private  final MongoTemplate mongoTemplate;
    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations operations;
    private final UserRepository userRepository;

    public FileDoc create(MultipartFile file, ObjectId ownerId) throws FileExistException, IOException, UserNotExistException {
        if (userRepository.findById(ownerId).isEmpty()) {
            throw new UserNotExistException();
        }

        DBObject metaData = new BasicDBObject();
        metaData.put("type", file.getContentType());
        metaData.put("title",file.getOriginalFilename());

        ObjectId id = gridFsTemplate.store(
                file.getInputStream(), file.getOriginalFilename(), file.getContentType(), metaData
        );

        FileDoc fileDoc = FileDoc.builder()
                .id(id)
                .title(file.getOriginalFilename())
                .ownerId(ownerId)
                .build();




        fileRepository.save(fileDoc);

        return fileDoc;
    }

    public Optional<FileDoc> findById(ObjectId id) {
        return fileRepository.findById(id);
    }

    public InputStream downloadById(ObjectId id) throws ChangeSetPersister.NotFoundException, IOException {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        if (file == null) {
            throw new ChangeSetPersister.NotFoundException();
        }
        return operations.getResource(file).getInputStream();
    }

    public SearchResponse<FileDoc> search(SearchRequest request) {

        Criteria criteria = new Criteria();
        if (request.getQuery() != null && request.getQuery() != "") {
            criteria = criteria.orOperator(
                    Criteria.where("title").regex(request.getQuery(),"i")

                    // TODO : Add Criteria
//                    Criteria.where("firstName").regex(request.getQuery(), "i"),
//                    Criteria.where("lastName").regex(request.getQuery(), "i"),
//                    Criteria.where("email").regex(request.getQuery(), "i")
            );
        }

        Query query = new Query(criteria);

        Long count = mongoTemplate.count(query,FileDoc.class);

        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<FileDoc> fileDocs = mongoTemplate.find(query, FileDoc.class);
        return SearchResponse.of(fileDocs,count);
    }




    public void delete(ObjectId id) {
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(id)));
        fileRepository.deleteById(id);
    }
}
