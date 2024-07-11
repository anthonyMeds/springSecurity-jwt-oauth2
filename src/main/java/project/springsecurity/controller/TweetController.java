package project.springsecurity.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import project.springsecurity.dto.CreateTweetDto;
import project.springsecurity.entity.Role;
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
    public ResponseEntity<Void> createTweet
            (
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

    @DeleteMapping("/tweets/{id}")
    public ResponseEntity<Void> deleteTweet
            (
                    @PathVariable("id") Long tweetId,
                    JwtAuthenticationToken token
            ) {

        var user = userRepository.findById(UUID.fromString(token.getName()));

        var tweet = tweetRepository.findById(tweetId)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var isAdmin = user.get().getRoles()
                .stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        if (isAdmin || tweet.getUser().getUserId().equals(UUID.fromString(token.getName()))) {

            tweetRepository.delete(tweet);

        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok().build();
    }

}
