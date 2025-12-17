package com.dev.blog.services.impl;

import com.dev.blog.domain.CreatePostRequest;
import com.dev.blog.domain.PostStatus;
import com.dev.blog.domain.dtos.PostDto;
import com.dev.blog.domain.entities.*;
import com.dev.blog.repositories.PostRepository;
import com.dev.blog.services.CategoryService;
import com.dev.blog.services.PostService;
import com.dev.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {


    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final static int WORDS_PER_MINUTE=200;

    @Override
    @Transactional
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if(categoryId!=null && tagId!=null)
        {
            Category category= categoryService.getCategoryById(categoryId);
            Tag tag=tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(
                    PostStatus.PUBLISHED,
                    category,
                    tag
            );
        }
        if(categoryId!=null)
        {
            Category category=categoryService.getCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(
                    PostStatus.PUBLISHED,
                    category
            );
        }

        if(tagId!=null)
        {
            Tag tag=tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(
                    PostStatus.PUBLISHED,
                    tag
            );
        }

        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(user,PostStatus.DRAFT);
    }

    @Override
    @Transactional
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post newPost=new Post();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        newPost.setStatus(createPostRequest.getStatus());
        newPost.setAuthor(user);
        newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));

        Category categoryId=categoryService.getCategoryById(createPostRequest.getCategoryId());
        newPost.setCategory(categoryId);

        Set<UUID> tagsIds=createPostRequest.getTagsIds();
        List<Tag> tags=tagService.getTagByIds(tagsIds);
        newPost.setTags(new HashSet<>(tags));

        return postRepository.save(newPost);
    }

    @Override
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest) {
        Post existingPost=postRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Post does not exist with id"+id));
        existingPost.setTitle(updatePostRequest.getTitle());
        existingPost.setContent(updatePostRequest.getContent());
        existingPost.setStatus(updatePostRequest.getPostStatus());
        existingPost.setReadingTime(calculateReadingTime(updatePostRequest.getContent()));

        UUID updatePostRequestCategoryId=updatePostRequest.getCategoryId();
        if(!existingPost.getCategory().getId().equals(updatePostRequestCategoryId))
        {
            Category newCategory=categoryService.getCategoryById(updatePostRequestCategoryId);
            existingPost.setCategory(newCategory);
        }

        Set<UUID> existingTagIds=existingPost.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        Set<UUID> updatePostRequestTagIds=updatePostRequest.getTagIds();
        if(!existingTagIds.equals(updatePostRequestTagIds))
        {
            List<Tag> newTags=tagService.getTagByIds(updatePostRequestTagIds);
            existingPost.setTags(new HashSet<>(newTags));
        }
        return postRepository.save(existingPost);
    }

    @Override
    public Post getPosts(UUID id) {
        return postRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Post doesn't exist with id"+id));
    }

    @Override
    public void deletePost(UUID id) {
        Post post=getPosts(id);
        postRepository.delete(post);
    }

    private Integer calculateReadingTime(String content){
        if(content==null || content.isEmpty())
        {
            return 0;
        }

        int wordcount=content.trim().split("\\s+").length;
        return (int) Math.ceil((double) wordcount/WORDS_PER_MINUTE);
    }
}
