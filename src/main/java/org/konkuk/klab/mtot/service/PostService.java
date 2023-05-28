package org.konkuk.klab.mtot.service;

import lombok.RequiredArgsConstructor;
import org.konkuk.klab.mtot.domain.Journey;
import org.konkuk.klab.mtot.domain.Post;
import org.konkuk.klab.mtot.dto.response.CreatePostResponse;
import org.konkuk.klab.mtot.repository.JourneyRepository;
import org.konkuk.klab.mtot.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final JourneyRepository journeyRepository;

    @Transactional
    public CreatePostResponse createPost(String memberEmail, Long journeyId, String title, String article){
        Journey journey = journeyRepository.findById(journeyId)
                .orElseThrow(() -> new RuntimeException("여정이 존재하지 않습니다."));

        journey.getTeam().getMemberTeams()
                .stream()
                .filter(memberTeam -> memberTeam.getMember().getEmail().equals(memberEmail))
                .findAny()
                .orElseThrow(()-> new RuntimeException("멤버가 그룹에 속하지 않습니다."));

        postRepository.findByJourneyId(journeyId)
                .ifPresent(foundJourney-> new RuntimeException("이미 포스트가 존재합니다."));

        Post post = new Post(journey, title, article);
        Long postId = postRepository.save(post).getId();
        return new CreatePostResponse(postId);
    }
}
