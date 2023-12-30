package com.mushroomapp.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.firebase.auth.FirebaseAuth;
import com.mushroomapp.app.controller.format.request.CommentCreationRequest;
import com.mushroomapp.app.controller.format.request.UserCreationRequest;
import com.mushroomapp.app.controller.format.response.*;
import com.mushroomapp.app.model.content.Post;
import com.mushroomapp.app.model.interaction.Comment;
import com.mushroomapp.app.model.interaction.InteractionId;
import com.mushroomapp.app.model.interaction.Like;
import com.mushroomapp.app.model.profile.User;
import com.mushroomapp.app.repository.LikeRepository;
import com.mushroomapp.app.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PostControllerTests {

    @Autowired
    private UserService userService;

    @Autowired
    private FirebaseAuth firebaseAuth;

    private MultipartBodyBuilder multipartBodyBuilder;

    @Autowired
    private WebTestClient webClient;

    private UserCreationResponse userCreationResponse1;
    private UserCreationResponse userCreationResponse2;

//    @BeforeAll
//    public void initializeFiles() {
//
//        this.multipartBodyBuilder = new MultipartBodyBuilder();
//
//        Resource image1 = new FileSystemResource("C:\\Users\\ratbo\\Pictures\\black-chicken-1.jpg");
//        Resource image2 = new FileSystemResource("C:\\Users\\ratbo\\Pictures\\black-chicken-2.jpg");
//
//        this.multipartBodyBuilder
//                .part("file", image1);
//        this.multipartBodyBuilder
//                .part("file", image2);
//        this.multipartBodyBuilder
//                .part("caption", "I hope this works!!");
//    }

    String userToken1 = "QYFAZ0bIrAfMsxvD4oIXaTtXWS12";
    String userToken2 = "19024j01892j409j213412";

    String username1 = "test123";
    String username2 = "testXYZ";

    private PostCreationResponse postResponse;
    private CommentCreationResponse commentResponse;
    private LikeCreationResponse likeResponse;

    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

    private UserCreationResponse createUser(WebTestClient webClient, UserCreationRequest userCreationRequest, String token) {
        return webClient
                .post()
                .uri("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(userCreationRequest))
                .header("Authorization", token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UserCreationResponse.class)
                .returnResult()
                .getResponseBody();
    }

    private PostCreationResponse createPost(WebTestClient webClient, MultipartBodyBuilder multipartBodyBuilder, String token) {
        return webClient
                .post()
                .uri("/v1/posts/")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", token)
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PostCreationResponse.class)
                .returnResult()
                .getResponseBody();
    }

    private List<Post> getUsersPosts(WebTestClient webClient, UUID id) {
        return webClient
                .get()
                .uri("v1/posts/user/"+id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Post.class)
                .returnResult()
                .getResponseBody();
    }

    private LikeCreationResponse likePost(WebTestClient webClient, UUID postId, String token) {
        return webClient
                .post()
                .uri("v1/posts/likes/" + postId)
                .header("Authorization", token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(LikeCreationResponse.class)
                .returnResult()
                .getResponseBody();
    }

    private List<Like> getLikesOnPost(WebTestClient webClient, UUID postId) {
        return webClient
                .get()
                .uri("v1/posts/likes/post/"+postId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Like.class)
                .returnResult()
                .getResponseBody();
    }

    private Post getPost(WebTestClient webClient, UUID postId) {
        return webClient
                .get()
                .uri("v1/posts/" + postId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Post.class)
                .returnResult()
                .getResponseBody();
    }

    private PostDeletionResponse deletePost(WebTestClient webClient, UUID postId) {
        return webClient
                .delete()
                .uri("v1/posts/" + postId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PostDeletionResponse.class)
                .returnResult()
                .getResponseBody();
    }

    private void assertPostDoesNotExist(WebTestClient webClient, UUID postId) {
        webClient
                .get()
                .uri("v1/posts/" + postId)
                .exchange()
                .expectStatus()
                .isNotFound();

        webClient
                .get()
                .uri("v1/posts/likes/post/"+postId)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    private List<Like> getUsersLikes(WebTestClient webClient, String token) {
        return webClient
                .get()
                .uri("v1/posts/likes/user")
                .header("Authorization", token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Like.class)
                .returnResult()
                .getResponseBody();
    }

    private void createMultipartBodyBuilder() {
        this.multipartBodyBuilder = new MultipartBodyBuilder();

        Resource image1 = new FileSystemResource("C:\\Users\\ratbo\\Pictures\\black-chicken-1.jpg");
        Resource image2 = new FileSystemResource("C:\\Users\\ratbo\\Pictures\\black-chicken-2.jpg");

        this.multipartBodyBuilder
                .part("file", image1);
        this.multipartBodyBuilder
                .part("file", image2);
        this.multipartBodyBuilder
                .part("caption", "I hope this works!!");
    }

    private List<Comment> getAllCommentsOnPost(WebTestClient webClient, UUID postId) {
        return webClient
                .get()
                .uri("v1/posts/comments/post/"+postId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Comment.class)
                .returnResult()
                .getResponseBody();
    }

    private CommentCreationResponse postComment(WebTestClient webClient, CommentCreationRequest request, String token) {
        return webClient
                .post()
                .uri("v1/posts/comments")
                .header("Authorization", token)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CommentCreationResponse.class)
                .returnResult()
                .getResponseBody();
    }

    private List<Comment> getUsersComments(WebTestClient webClient, String token) {
        return webClient
                .get()
                .uri("v1/posts/comments/user")
                .header("Authorization", token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Comment.class)
                .returnResult()
                .getResponseBody();
    }

    private CommentDeletionResponse deleteComment(WebTestClient webClient, UUID commentId, String token) {
        return webClient
                .delete()
                .uri("v1/posts/comments/" + commentId)
                .header("Authorization", token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CommentDeletionResponse.class)
                .returnResult()
                .getResponseBody();
    }

    private FollowUserResponse followUser(WebTestClient webClient, UUID userId, String userToken) {
        return webClient
                .post()
                .uri("v1/users/follow/" + userId)
                .header("Authorization", userToken)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(FollowUserResponse.class)
                .returnResult()
                .getResponseBody();
    }

    private UnfollowUserResponse unfollowUser(WebTestClient webClient, UUID userId, String userToken) {
        return webClient
                .delete()
                .uri("v1/users/follow/" + userId)
                .header("Authorization", userToken)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UnfollowUserResponse.class)
                .returnResult()
                .getResponseBody();
    }

    private List<User> getFollowersOfUser(WebTestClient webClient, UUID userId, String userToken) {
        return webClient
                .get()
                .uri("v1/users/followers/" + userId)
                .header("Authorization", userToken)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(User.class)
                .returnResult()
                .getResponseBody();
    }

    private List<User> getFollowedByUser(WebTestClient webClient, UUID userId, String userToken) {
        return webClient
                .get()
                .uri("v1/users/following/" + userId)
                .header("Authorization", userToken)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(User.class)
                .returnResult()
                .getResponseBody();
    }


    @BeforeEach
    public void postWith2Images_thenLikePost_thenDeletePost(@Autowired WebTestClient webClient) throws JsonProcessingException {

        createMultipartBodyBuilder();

        UserCreationRequest request1 = new UserCreationRequest();
        request1.username = username1;

        UserCreationRequest request2 = new UserCreationRequest();
        request2.username = username2;

        this.userCreationResponse1 = createUser(webClient, request1, userToken1);
        this.userCreationResponse2 = createUser(webClient, request2, userToken2);

        assert userCreationResponse1 != null;
        assert userCreationResponse2 != null;

        List<User> followedByUser1 = getFollowedByUser(webClient, userCreationResponse1.userId, userToken1);
        List<User> followersOfUser1 = getFollowersOfUser(webClient, userCreationResponse1.userId, userToken1);

        List<User> followedByUser2 = getFollowedByUser(webClient, userCreationResponse2.userId, userToken2);
        List<User> followersOfUser2 = getFollowersOfUser(webClient, userCreationResponse2.userId, userToken2);

        assertThat(followedByUser1).isEmpty();
        assertThat(followersOfUser1).isEmpty();
        assertThat(followedByUser2).isEmpty();
        assertThat(followersOfUser2).isEmpty();

        FollowUserResponse followResponse1follows2 = followUser(webClient, userCreationResponse2.userId, userToken1);

        List<User> followedByUser1Before = getFollowedByUser(webClient, userCreationResponse1.userId, userToken1);
        List<User> followersOfUser1Before = getFollowersOfUser(webClient, userCreationResponse1.userId, userToken1);

        List<User> followedByUser2Before = getFollowedByUser(webClient, userCreationResponse2.userId, userToken2);
        List<User> followersOfUser2Before = getFollowersOfUser(webClient, userCreationResponse2.userId, userToken2);

        assertThat(followedByUser1Before).size().isEqualTo(1);
        assertThat(followedByUser1Before).map(User::getId).containsOnly(userCreationResponse2.userId);
        assertThat(followersOfUser2Before).size().isEqualTo(1);
        assertThat(followersOfUser2Before).map(User::getId).containsOnly(userCreationResponse1.userId);

        assertThat(followersOfUser1Before).isEmpty();
        assertThat(followedByUser2Before).isEmpty();

        FollowUserResponse followResponse2follows1 = followUser(webClient, userCreationResponse1.userId, userToken2);

        List<User> followedByUser1After = getFollowedByUser(webClient, userCreationResponse1.userId, userToken1);
        List<User> followersOfUser1After = getFollowersOfUser(webClient, userCreationResponse1.userId, userToken1);

        List<User> followedByUser2After = getFollowedByUser(webClient, userCreationResponse2.userId, userToken2);
        List<User> followersOfUser2After = getFollowersOfUser(webClient, userCreationResponse2.userId, userToken2);

        assertThat(followedByUser1After).size().isEqualTo(1);
        assertThat(followedByUser1After).map(User::getId).containsOnly(userCreationResponse2.userId);
        assertThat(followersOfUser2After).size().isEqualTo(1);
        assertThat(followersOfUser2After).map(User::getId).containsOnly(userCreationResponse1.userId);

        assertThat(followedByUser2After).size().isEqualTo(1);
        assertThat(followedByUser2After).map(User::getId).containsOnly(userCreationResponse1.userId);
        assertThat(followersOfUser1After).size().isEqualTo(1);
        assertThat(followersOfUser1After).map(User::getId).containsOnly(userCreationResponse2.userId);

        this.postResponse = createPost(webClient, multipartBodyBuilder, userToken1);
        assert postResponse != null;

        List<Post> posts = getUsersPosts(webClient,userCreationResponse1.userId);
        assert posts != null;

        assertThat(posts).map(Post::getUser).map(User::getId).containsOnly(userCreationResponse1.userId); // The user's posts all have the user as their assigned user
        assertThat(posts).map(Post::getId).contains(postResponse.postId); // The user's post list contains a post with the id of the post made previously in the test

        this.likeResponse = likePost(webClient, postResponse.postId, userToken2);
        assert likeResponse != null;

        assertThat(likeResponse.postId).isEqualByComparingTo(postResponse.postId);
        assertThat(likeResponse.userId).isEqualByComparingTo(userCreationResponse2.userId);

        List<Like> likesOnPost = getLikesOnPost(webClient, postResponse.postId);
        assert likesOnPost != null;

        assertThat(likesOnPost.size()).isEqualTo(1);
        assertThat(likesOnPost).map(Like::getId).map(InteractionId::getUserId).containsOnly(userCreationResponse2.userId);

        Post postAfterLike = getPost(webClient, postResponse.postId);
        assert postAfterLike != null;

        assertThat(postAfterLike.getLikes()).map(Like::getId).map(InteractionId::getUserId).containsOnly(userCreationResponse2.userId);
        assertThat(postAfterLike.getLikes().size()).isEqualTo(1);

        List<Like> usersLikesAfterPost = getUsersLikes(webClient, userToken2);

        assertThat(usersLikesAfterPost).size().isEqualTo(1);
        assertThat(usersLikesAfterPost).map(Like::getId).map(InteractionId::getUserId).containsOnly(userCreationResponse2.userId);
        assertThat(usersLikesAfterPost).map(Like::getId).map(InteractionId::getPostId).containsOnly(postResponse.postId);

        List<Comment> allCommentsOnPost = getAllCommentsOnPost(webClient, postResponse.postId);

        assertThat(allCommentsOnPost).isEmpty();

        CommentCreationRequest commentRequest = new CommentCreationRequest();
        commentRequest.postId = postResponse.postId;
        commentRequest.respondedToId = null;
        commentRequest.content = "This is a test comment!!";

        this.commentResponse = postComment(webClient, commentRequest, userToken2);

        List<Comment> usersCommentsAfterCommenting = getUsersComments(webClient, userToken2);
        System.out.println("comment response: " + this.commentResponse.comment.getId());

        assertThat(usersCommentsAfterCommenting).size().isEqualTo(1);
        assertThat(usersCommentsAfterCommenting).map(Comment::getRespondedTo).containsOnlyNulls();

        List<Comment> allCommentsOnPostAfterCommenting = getAllCommentsOnPost(webClient, postResponse.postId);

        assertThat(allCommentsOnPostAfterCommenting).size().isEqualTo(1);
        assertThat(allCommentsOnPostAfterCommenting).map(Comment::getId).containsExactlyElementsOf(usersCommentsAfterCommenting.stream().map(Comment::getId).collect(Collectors.toList()));

    }

    @Test
    public void deletePost() {
        PostDeletionResponse postDeletionResponse = deletePost(webClient, postResponse.postId);
        assertPostDoesNotExist(webClient, postResponse.postId);

        List<Post> postsAfterDeletion = getUsersPosts(webClient,userCreationResponse1.userId);
        assert postsAfterDeletion != null;

        assertThat(postsAfterDeletion).isEmpty();

        System.out.println("posts after deletion: " + Arrays.deepToString(postsAfterDeletion.toArray()));

        List<Like> usersLikesAfterPostDeletion = getUsersLikes(webClient, userToken2);

        assertThat(usersLikesAfterPostDeletion).isEmpty();

        List<Comment> usersCommentsAfterPostDeletion = getUsersComments(webClient, userToken2);

        assertThat(usersCommentsAfterPostDeletion).isEmpty();
    }

    @Test
    public void deleteComment() {
        System.out.println("comment response in delete comment: " + this.commentResponse.getComment().getId());
        CommentDeletionResponse commentDeletion = deleteComment(webClient, commentResponse.getComment().getId(), userToken2);

        List<Comment> usersCommentsAfterCommenting = getUsersComments(webClient, userToken2);

        assertThat(usersCommentsAfterCommenting).isEmpty();

        List<Comment> allCommentsOnPostAfterCommenting = getAllCommentsOnPost(webClient, postResponse.postId);

        assertThat(allCommentsOnPostAfterCommenting).isEmpty();
    }

    @Test
    public void unfollowUser() {
        UnfollowUserResponse unfollowResponse1Unfollows2 = unfollowUser(webClient, userCreationResponse2.userId, userToken1);

        List<User> followedByUser1Before = getFollowedByUser(webClient, userCreationResponse1.userId, userToken1);
        List<User> followersOfUser1Before = getFollowersOfUser(webClient, userCreationResponse1.userId, userToken1);

        List<User> followedByUser2Before = getFollowedByUser(webClient, userCreationResponse2.userId, userToken2);
        List<User> followersOfUser2Before = getFollowersOfUser(webClient, userCreationResponse2.userId, userToken2);


        assertThat(followedByUser2Before).size().isEqualTo(1);
        assertThat(followedByUser2Before).map(User::getId).containsOnly(userCreationResponse1.userId);
        assertThat(followersOfUser1Before).size().isEqualTo(1);
        assertThat(followersOfUser1Before).map(User::getId).containsOnly(userCreationResponse2.userId);

        assertThat(followersOfUser2Before).isEmpty();
        assertThat(followedByUser1Before).isEmpty();

        UnfollowUserResponse unfollowResponse2Unfollows1 = unfollowUser(webClient, userCreationResponse1.userId, userToken2);

        List<User> followedByUser1 = getFollowedByUser(webClient, userCreationResponse1.userId, userToken1);
        List<User> followersOfUser1 = getFollowersOfUser(webClient, userCreationResponse1.userId, userToken1);

        List<User> followedByUser2 = getFollowedByUser(webClient, userCreationResponse2.userId, userToken2);
        List<User> followersOfUser2 = getFollowersOfUser(webClient, userCreationResponse2.userId, userToken2);

        assertThat(followedByUser1).isEmpty();
        assertThat(followersOfUser1).isEmpty();
        assertThat(followedByUser2).isEmpty();
        assertThat(followersOfUser2).isEmpty();
    }

}
