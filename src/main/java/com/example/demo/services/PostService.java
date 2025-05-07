package com.example.demo.services;

import com.example.demo.models.Post;
import com.example.demo.repositories.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getPosts() {
        List<Post> all = postRepository.findAll();
        System.out.println(all);
        return all;
    }

    public Post getPost(int id) {
        Optional<Post> post = postRepository.findById(id);
        return post.orElse(null);
    }

    @Transactional
    public void save(Post post) {
        postRepository.save(post);
    }

    @Transactional
    public void update(int id, Post updatedPost) {
        updatedPost.setId(id);
        postRepository.save(updatedPost);
    }

    @Transactional
    public void delete(int id) {
        postRepository.deleteById(id);
    }
}
