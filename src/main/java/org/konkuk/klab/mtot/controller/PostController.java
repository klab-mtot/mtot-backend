package org.konkuk.klab.mtot.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.dto.request.PostCreateRequest;
import org.konkuk.klab.mtot.dto.response.PostCreateResponse;
import org.konkuk.klab.mtot.oauth.LoginMemberEmail;
import org.konkuk.klab.mtot.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostCreateResponse> createPost(@LoginMemberEmail String email, @RequestBody @Valid PostCreateRequest postCreateRequest){
        PostCreateResponse postCreateResponse = postService.createPost(email, postCreateRequest.getJourneyId(), postCreateRequest.getTitle(), postCreateRequest.getArticle());
        return ResponseEntity.ok(postCreateResponse);
    }
}
