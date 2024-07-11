package project.springsecurity.dto;

public record FeedItemDto
        (
                long tweetId,
                String content,
                String username
        ) {
}
