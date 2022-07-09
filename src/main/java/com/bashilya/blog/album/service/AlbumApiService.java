package com.bashilya.blog.album.service;

import com.bashilya.blog.auth.exceptions.AuthException;
import com.bashilya.blog.auth.exceptions.NotAccessException;
import com.bashilya.blog.auth.service.AuthService;
import com.bashilya.blog.base.api.request.SearchRequest;
import com.bashilya.blog.base.api.response.SearchResponse;
import com.bashilya.blog.album.api.request.AlbumRequest;
import com.bashilya.blog.album.mapping.AlbumMapping;
import com.bashilya.blog.album.exception.AlbumExistException;
import com.bashilya.blog.album.exception.AlbumNotExistException;
import com.bashilya.blog.album.model.AlbumDoc;
import com.bashilya.blog.album.repository.AlbumRepository;
import com.bashilya.blog.base.service.CheckAccess;
import com.bashilya.blog.photo.api.request.PhotoSearchRequest;
import com.bashilya.blog.photo.model.PhotoDoc;
import com.bashilya.blog.photo.repository.PhotoRepository;
import com.bashilya.blog.photo.service.PhotoApiService;
import com.bashilya.blog.user.exception.UserNotExistException;
import com.bashilya.blog.user.model.UserDoc;
import com.bashilya.blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlbumApiService extends CheckAccess<AlbumDoc> {
    private final AlbumRepository albumRepository;
    private  final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;
    private final PhotoApiService photoApiService;
    private final AuthService authService;

    public AlbumDoc create(AlbumRequest request) throws AlbumExistException, UserNotExistException, AuthException {

        UserDoc userDoc = authService.currentUser();

        AlbumDoc albumDoc = AlbumMapping.getInstance().getRequestMapping().convert(request, userDoc.getId());

        albumRepository.save(albumDoc);

        return albumDoc;
    }

    public Optional<AlbumDoc> findById(ObjectId id) {
        return albumRepository.findById(id);
    }

    public SearchResponse<AlbumDoc> search(SearchRequest request) {

        Criteria criteria = new Criteria();
        if (request.getQuery() != null && request.getQuery() != "") {
            criteria = criteria.orOperator(

                    // TODO : Add Criteria
                    Criteria.where("title").regex(request.getQuery(), "i")
//                    Criteria.where("lastName").regex(request.getQuery(), "i"),
//                    Criteria.where("email").regex(request.getQuery(), "i")
            );
        }

        Query query = new Query(criteria);

        Long count = mongoTemplate.count(query,AlbumDoc.class);

        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<AlbumDoc> albumDocs = mongoTemplate.find(query, AlbumDoc.class);
        return SearchResponse.of(albumDocs,count);
    }


    public AlbumDoc update(AlbumRequest request) throws AlbumNotExistException, AuthException, NotAccessException {
        Optional<AlbumDoc> albumDocOptional = albumRepository.findById(request.getId());
        if (!albumDocOptional.isPresent()) {
            throw new AlbumNotExistException();
        }

        AlbumDoc oldDoc = albumDocOptional.get();

        UserDoc owner = checkAccess(oldDoc);


        AlbumDoc albumDoc = AlbumMapping.getInstance().getRequestMapping().convert(request,owner.getId());
        albumDoc.setId(request.getId());
        albumDoc.setOwnerId(oldDoc.getOwnerId());
        albumRepository.save(albumDoc);

        return albumDoc;
    }

    public void delete(ObjectId id) throws AuthException, NotAccessException, ChangeSetPersister.NotFoundException {

        checkAccess(albumRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new));

        List<PhotoDoc> photoDocs = photoApiService.search(PhotoSearchRequest.builder().albumId(id).size(1000).build()).getList();

        for (PhotoDoc photoDoc : photoDocs) {
            photoApiService.delete(photoDoc.getId());
        }
        albumRepository.deleteById(id);
        // TODO delete photos

    }



    @Override
    protected ObjectId getOwnerFromEntity(AlbumDoc entity) {
        return entity.getOwnerId();
    }

    @Override
    protected AuthService authService() {
        return this.authService;
    }
}
