package project.springsecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.springsecurity.dto.CreateTweetDto;
import project.springsecurity.entity.Tweet;
import project.springsecurity.repository.TweetRepository;
import project.springsecurity.repository.UserRepository;

import java.util.UUID;

@RestController
public class TweetController {

    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    public TweetController(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/tweets")
    public ResponseEntity<Void> createTweet(
            @RequestBody CreateTweetDto createTweetDto,
            JwtAuthenticationToken token
            ) {

        var user = userRepository.findById(UUID.fromString(token.getName()));

        var tweet = new Tweet();
        tweet.setUser(user.get());
        tweet.setContent(createTweetDto.content());

        tweetRepository.save(tweet);

        return ResponseEntity.ok().build();

    }

}
