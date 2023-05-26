package org.konkuk.klab.mtot.service;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Post;
import org.konkuk.klab.mtot.dto.request.PostCreateRequest;
import org.konkuk.klab.mtot.dto.request.PostReadRequest;
import org.konkuk.klab.mtot.dto.request.PostUpdateRequest;
import org.konkuk.klab.mtot.dto.response.PostCreateResponse;
import org.konkuk.klab.mtot.dto.response.PostReadResponse;
import org.konkuk.klab.mtot.dto.response.PostUpdateResponse;
import org.konkuk.klab.mtot.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    //CREATE
    @Transactional
    public PostCreateResponse createPost(PostCreateRequest request){
        //request.getJourney_id()
        Post post = new Post();
        return new PostCreateResponse(post.getId());
    }
    //READ
    public PostReadResponse readPost(PostReadRequest request){
            Optional<Post> post = postRepository.findByPostId(request.getPost_id());
        return new PostReadResponse(post.get().getPostHtml());
    }
    //UPDATE
    @Transactional
    public PostUpdateResponse updatePost(PostUpdateRequest request){
        Optional<Post> post = postRepository.findByPostId(request.getId());
        post.get().setPostHtml(request.getHtml());
        return new PostUpdateResponse(0);
    }
    //DELETE 저니 삭제시 cascade하게 구현 필요한데 이건 나중에
}
