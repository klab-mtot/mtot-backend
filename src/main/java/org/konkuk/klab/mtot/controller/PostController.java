package org.konkuk.klab.mtot.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.dto.request.CreatePostRequest;
import org.konkuk.klab.mtot.dto.request.EditPostRequest;
import org.konkuk.klab.mtot.dto.response.CreatePostResponse;
import org.konkuk.klab.mtot.dto.response.EditPostResponse;
import org.konkuk.klab.mtot.oauth.LoginMemberEmail;
import org.konkuk.klab.mtot.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    @PostMapping
    public ResponseEntity<CreatePostResponse> createPost(@LoginMemberEmail String email,
                                                         @RequestBody @Valid CreatePostRequest request){
        CreatePostResponse createPostResponse = postService.createPost(
                email,
                request.getJourneyId(),
                request.getTitle(),
                request.getArticle());
        return ResponseEntity.ok(createPostResponse);
    }

    @PostMapping("/edit")
    public ResponseEntity<EditPostResponse> editPost(@LoginMemberEmail String email,
                                                     @RequestBody @Valid EditPostRequest request){
        EditPostResponse response = postService.editPost(
                email,
                request.getJourneyId(),
                request.getTitle(),
                request.getArticle());
        return ResponseEntity.ok(response);
    }
}
