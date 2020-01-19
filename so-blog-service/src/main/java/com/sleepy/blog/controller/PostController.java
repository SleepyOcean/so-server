package com.sleepy.blog.controller;

import com.sleepy.blog.dto.CommonDTO;
import com.sleepy.blog.entity.ArticleEntity;
import com.sleepy.blog.service.PostService;
import com.sleepy.blog.vo.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 文章发布控制器
 *
 * @author Captain
 * @create 2019-04-20 13:25
 */
@RestController
@CrossOrigin
@RequestMapping("/article")
public class PostController {
    @Autowired
    PostService postService;

    @PostMapping("/hot-articles")
    public CommonDTO<ArticleEntity> getHotArticle(@RequestBody PostVO vo) throws IOException {
        return postService.getHotArticle(vo);
    }

    @PostMapping("/related-articles")
    public CommonDTO<ArticleEntity> getRelatedArticle(@RequestBody PostVO vo) throws IOException {
        return postService.getRelatedArticle(vo);
    }

    @PostMapping("/save")
    public CommonDTO<String> save(@RequestBody PostVO vo) throws Exception {
        return postService.saveArticle(vo);
    }

    @PostMapping("/search")
    public CommonDTO<ArticleEntity> search(@RequestBody PostVO vo) throws IOException {
        return postService.searchArticle(vo);
    }

    @PostMapping("/get")
    public CommonDTO<ArticleEntity> get(@RequestBody PostVO vo) {
        return postService.getArticle(vo);
    }

    @PostMapping("/delete")
    public CommonDTO<ArticleEntity> delete(@RequestBody PostVO vo) {
        return postService.deleteArticle(vo);
    }

    @PostMapping("/tags")
    public CommonDTO<String> getTags(@RequestBody PostVO vo) {
        return postService.getTags(vo);
    }
}
