package org.konkuk.klab.mtot.controller;


import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.dto.response.PostCreateResponse;
import org.konkuk.klab.mtot.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Posts")
public class PostController {

    private final PostService postService;
    @PostMapping("/members/{member}/journies/{journeyId}/titles/{title}/articles/{article}")
    public ResponseEntity<PostCreateResponse> createPost(@PathVariable("member") String member, @PathVariable("journeyId") Long journeyId, @PathVariable("title") String title, @PathVariable("article") String article){
        PostCreateResponse postCreateResponse = postService.createPost(member, journeyId, title, article);
        return ResponseEntity.ok(postCreateResponse);
    }
}
