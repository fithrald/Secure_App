package com.example.demo.repositories;

import com.example.demo.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT p FROM Post p WHERE person.id = :uid "
            + "AND (p.title LIKE %:q% OR p.text LIKE %:q%)")
    List<Post> searchByUserAndText(@Param("uid") int uid, @Param("q") String q);
}
