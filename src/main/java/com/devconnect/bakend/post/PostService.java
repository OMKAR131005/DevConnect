package com.devconnect.bakend.post;

import com.devconnect.bakend.exceptions.NotValidUser;
import com.devconnect.bakend.exceptions.ResourceNotFoundException;
import com.devconnect.bakend.post.dto.CommentRequest;
import com.devconnect.bakend.post.dto.CommentResponse;
import com.devconnect.bakend.post.dto.PostRequest;
import com.devconnect.bakend.post.dto.PostResponse;
import com.devconnect.bakend.profile.Profile;
import com.devconnect.bakend.profile.ProfileRepository;
import com.devconnect.bakend.user.User;
import com.devconnect.bakend.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;



@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    public PostResponse createPost(PostRequest request){
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user not found"));
        Post post = Post.builder().
                tags(request.getTags()).
                description(request.getDescription()).
                title(request.getTitle()).
                visibility(request.getVisibility()).user(user)
                .build();

        postRepository.save(post);
        Profile profile=profileRepository.findByUser(user);
       return PostResponse.builder().commentCount(0).likeCount(0).profilePicture(profile.getProfilePicture()).
                tags(post.getTags()).title(post.getTitle()).description(post.getDescription()).
                imageUrl(post.getImageUrl()).createdAt(post.getCreatedAt()).visibility(request.getVisibility())
               .updatedAt(post.getUpdatedAt()).postId(post.getId()).isLikedByMe(false).viewCount(0).username(user.getUsername()).build();

    }
    public PostResponse updatePost(PostRequest request,Long id){
        Long userId1 = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post not found"));
        User user=post.getUser();
        Long userId2 = post.getUser().getUserId();
        if(userId1.equals(userId2)){
            if(request.getTitle()!=null){
                post.setTitle(request.getTitle());
            }
            if(request.getDescription()!=null){
                post.setDescription(request.getDescription());
            }
            if (request.getVisibility()!=null){
                post.setVisibility(request.getVisibility());
            }
            if (request.getTags()!=null){
                post.setTags(request.getTags());
            }
            postRepository.save(post);
        }else {
            throw new NotValidUser("user not able to update post");
        }
        Profile profile=profileRepository.findByUser(user);
        return PostResponse.builder().commentCount((int)commentRepository.countByPost(post)).likeCount((int)postLikeRepository.countByPost(post)).profilePicture(profile.getProfilePicture()).
                tags(post.getTags()).title(post.getTitle()).description(post.getDescription()).
                imageUrl(post.getImageUrl()).createdAt(post.getCreatedAt()).visibility(post.getVisibility())
                .updatedAt(post.getUpdatedAt()).postId(post.getId()).isLikedByMe(false).viewCount(post.getViewCount()).username(user.getUsername()).build();
    }

    @Transactional
    public String deletePost(Long id) {
        Long userId1 = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post not found"));
        Long userId2 = post.getUser().getUserId();
        if (userId1.equals(userId2)) {
            postLikeRepository.deleteByPost(post);
            commentRepository.deleteByPost(post);
            postRepository.delete(post);
            return "Deleted";
        } else {
            throw new NotValidUser("user not able to delete post");
        }
    }
    public boolean toggleLikePost(Long id){
        Long userId1 = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user=userRepository.findById(userId1).orElseThrow(() -> new ResourceNotFoundException("user not found"));
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post not found"));
        if(postLikeRepository.existsByUserAndPost(user,post)){
            postLikeRepository.deleteByUserAndPost(user,post);
            return false;
        }
        else{
            postLikeRepository.save(PostLike.builder().post(post).user(user).build());
            return true;
        }

    }
    public CommentResponse addComment(CommentRequest request,long postId){
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post not found"));
        User user=userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));
        Comment comment=Comment.builder().post(post).user(user).commentText(request.getCommentText()).build();
        commentRepository.save(comment);
        Profile profile = profileRepository.findByUser(user);
        return CommentResponse.builder().commentId(comment.getCommentId())
                .username(user.getUsername())
                .profilePicture(profile.getProfilePicture()).
                commentText(request.getCommentText()).
                createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt()).build();
    }
    public String deleteComment(Long id){
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("comment not found"));
        if(comment.getUser().getUserId().equals(userId)){
            commentRepository.delete(comment);
            return "Deleted";
        }else{
            throw new NotValidUser("user not able to delete comment");
        }
    }
    public PostResponse getPost(Long id){
        Post post =postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post not found"));
        post.increaseViewCount();
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user=userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));
        User user1=post.getUser();
       Profile profile=profileRepository.findByUser(user1);
       postRepository.save(post);
        return PostResponse.builder().commentCount((int)commentRepository.countByPost(post)).likeCount((int)postLikeRepository.countByPost(post)).profilePicture(profile.getProfilePicture()).
                tags(post.getTags()).title(post.getTitle()).description(post.getDescription()).
                imageUrl(post.getImageUrl()).createdAt(post.getCreatedAt()).visibility(post.getVisibility())
                .updatedAt(post.getUpdatedAt()).postId(post.getId()).isLikedByMe(postLikeRepository.existsByUserAndPost(user,post)).viewCount(post.getViewCount()).username(user1.getUsername()).build();
    }
    public Page<CommentResponse> getPosts(Long idPost,Pageable pageable){
        Post post=postRepository.findById(idPost).orElseThrow(() -> new ResourceNotFoundException("post not found"));
        return commentRepository.findCommentsByPost(post,pageable);
    }
    public Page<PostResponse> getUserPosts(Pageable pageable,String username){
        User user=userRepository.findByUsername(username);
        return postRepository.findPostsByUser(user,pageable);
    }
    public Page<PostResponse> getFeed(Pageable pageable){
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user=userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));
       return postRepository.getFeedPosts(user,pageable);

    }
}
