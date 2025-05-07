package com.example.demo.controllers;

import com.example.demo.models.Post;
import com.example.demo.security.PersonDetails;
import com.example.demo.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.List;


@Controller
@RequestMapping("/post")
public class PostController {
    private final EntityManager entityManager;
    private final PostService postService;
    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Post> MAPPER =
            BeanPropertyRowMapper.newInstance(Post.class);

    @Autowired
    public PostController(EntityManager entityManager, PostService postService, JdbcTemplate jdbcTemplate, RowMapper rowMapper) {
        this.entityManager = entityManager;
        this.postService = postService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/info")
    public String postsInfo(Model model) {
        model.addAttribute("posts", postService.getPosts());
        return "posts/posts";
    }

    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable("id") int id, Model model) {
        model.addAttribute("post", postService.getPost(id));
        return "posts/postEdit";
    }

    @PatchMapping("/{id}")
    public String editPost(@PathVariable("id") int id, @ModelAttribute Post post) {
        postService.update(id, post);
        return "redirect:/post/info";
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable("id") int id) {
        postService.delete(id);
        return "redirect:/post/info";
    }

    @GetMapping("/new")
    public String postNew(@ModelAttribute Post post) {
        return "posts/newPost";
    }

    @PostMapping("/new")
    public String producing(@ModelAttribute Post post) {
        postService.save(post);
        return "redirect:/post/info";
    }

    @GetMapping("/search")
    public String search(@RequestParam String q,
                         Model model,
                         @AuthenticationPrincipal PersonDetails user) {
//        String sql = ""
//                + "SELECT * FROM post "
//                + "WHERE person_id = " + user.getPerson()
//                +   " AND (title LIKE '%" + q + "%' "
//                +        "OR text  LIKE '%" + q + "%')";
        String sql =
                "SELECT * FROM post " +
                        "WHERE person_id = ? " +
                        "  AND (title ILIKE ? OR text ILIKE ?)";

        List<Post> posts = jdbcTemplate.query(
                sql,
                MAPPER,
                user.getPerson().getId(),
                '%' + q + '%',
                '%' + q + '%'
        );
        model.addAttribute("q", q);
        model.addAttribute("posts", posts);
        return "posts/sql/search";
    }
}
//        String sql = ""
//                + "SELECT * FROM post "
//                + "WHERE person_id = " + userId
//                +   " AND (title LIKE '%" + q + "%' "
//                +        "OR text  LIKE '%" + q + "%')";

//String sql =
//        "SELECT * FROM post " +
//                "WHERE person_id = ? " +
//                "  AND (title ILIKE ? OR text ILIKE ?)";
