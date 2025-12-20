package com.dev.blog.controllers;

import com.dev.blog.domain.CreatePostRequest;
import com.dev.blog.domain.dtos.CreatePostRequestDto;
import com.dev.blog.domain.dtos.PostDto;
import com.dev.blog.domain.dtos.UpdatePostRequestDto;
import com.dev.blog.domain.entities.Post;
import com.dev.blog.domain.UpdatePostRequest;
import com.dev.blog.domain.entities.User;
import com.dev.blog.mappers.PostMapper;
import com.dev.blog.services.PostService;
import com.dev.blog.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostDto>> listposts(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID tagId
            )
    {
        List<Post> posts=postService.getAllPosts(categoryId,tagId);
        List<PostDto> postDtos=posts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping(path="/drafts")
    public ResponseEntity<List<PostDto>> getDrafts(@RequestAttribute UUID userId)
    {
        User loggedInUser= userService.getUserById(userId);
        List<Post> drafts= postService.getDraftPosts(loggedInUser);
        List<PostDto> postDtos=drafts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @RequestBody CreatePostRequestDto createPostRequestDto,
            @Valid @RequestAttribute UUID userId)
    {
        User loggedInUser=userService.getUserById(userId);
        CreatePostRequest createPostRequest=postMapper.toCreatePostRequest(createPostRequestDto);
        Post createPost=postService.createPost(loggedInUser,createPostRequest);
        PostDto createdPostDto=postMapper.toDto(createPost);
        return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable UUID id,
            @RequestBody @Valid UpdatePostRequestDto updatePostRequestDto
            )
    {
        UpdatePostRequest updatePostRequest=postMapper.toUpdatePostRequest(updatePostRequestDto);
        Post updatedPost=postService.updatePost(id,updatePostRequest);
        PostDto updatedPostDto=postMapper.toDto(updatedPost);
        return ResponseEntity.ok(updatedPostDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPost(
            @PathVariable UUID id
    )
    {
        Post post=postService.getPosts(id);
        PostDto postDto=postMapper.toDto(post);
        return ResponseEntity.ok(postDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletepost(@PathVariable UUID id)
    {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
