package com.dev.blog.controllers;

import com.dev.blog.domain.dtos.PostDto;
import com.dev.blog.domain.entities.Post;
import com.dev.blog.domain.entities.User;
import com.dev.blog.mappers.PostMapper;
import com.dev.blog.services.PostService;
import com.dev.blog.services.UserService;
import lombok.RequiredArgsConstructor;
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
}
