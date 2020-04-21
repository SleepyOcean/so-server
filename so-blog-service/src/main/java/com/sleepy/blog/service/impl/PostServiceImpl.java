package com.sleepy.blog.service.impl;

import com.google.common.collect.Lists;
import com.sleepy.blog.dto.CommonDTO;
import com.sleepy.blog.entity.ArticleEntity;
import com.sleepy.blog.entity.TagEntity;
import com.sleepy.blog.repository.ArticleRepository;
import com.sleepy.blog.repository.TagRepository;
import com.sleepy.blog.service.CacheService;
import com.sleepy.blog.service.ImgService;
import com.sleepy.blog.service.PostService;
import com.sleepy.blog.vo.PostVO;
import com.sleepy.common.tools.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 文章发布服务接口实现
 *
 * @author Captain
 * @create 2019-04-20 13:26
 */
@Service
@Slf4j
public class PostServiceImpl implements PostService {
    public static final String INDEX_NAME = "so_blog";
    public static final String TYPE_NAME = "so_article";

    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    CacheService cacheService;
    @Autowired
    ImgService imgService;

    @Override
    public CommonDTO<ArticleEntity> getHotArticle(PostVO vo) throws IOException {
        CommonDTO<ArticleEntity> result = new CommonDTO<>();
        List<ArticleEntity> resultList = articleRepository.findHotArticleInfo();
        result.setResultList(resultList);
        return result;
    }

    @Override
    public CommonDTO<ArticleEntity> getRelatedArticle(PostVO vo) throws IOException {
        if (StringTools.isNotNullOrEmpty(vo.getTags())) {
            List<String> articleIds = tagRepository.findArticleIdsByTags(vo.getTags().split(","));
            CommonDTO<ArticleEntity> result = new CommonDTO<>();
            List<ArticleEntity> resultList = articleRepository.findAllByIds(articleIds);
            result.setResultList(resultList);
            return result;
        } else {
            return new CommonDTO<>();
        }
    }

    @Override
    public CommonDTO<String> saveArticle(PostVO vo) throws Exception {
        CommonDTO<String> result = new CommonDTO<>();
        ArticleEntity entity = new ArticleEntity();
        if (StringTools.isNotNullOrEmpty(vo.getId())) {
            entity = articleRepository.getOne(vo.getId());
        } else {
            entity.setCreateTime(new Date());
        }
        entity.setUpdateTime(new Date());
        entity.setTitle(vo.getTitle());
        entity.setContent(vo.getContent());
        entity.setSummary(vo.getSummary());
        if (!StringTools.isNullOrEmpty(vo.getCoverImg())) {
            entity.setCoverImg(vo.getCoverImg());
        }
        if (StringTools.isNotNullOrEmpty(vo.getTags())) {
            entity.setTags(vo.getTags());
        }
        String id = articleRepository.saveAndFlush(entity).getId();
        // 存储文章标签
        String[] tags = vo.getTags().split(",");
        for (int i = 0; i < tags.length; i++) {
            TagEntity tag = new TagEntity();
            tag.setTagName(tags[i]);
            tag.setArticleId(entity.getId());
            tagRepository.save(tag);
        }
        result.setResult(id);

        return result;
    }

    @Override
    public CommonDTO<ArticleEntity> searchArticle(PostVO vo) throws IOException {
        CommonDTO<ArticleEntity> result = new CommonDTO<>();
        List<ArticleEntity> resultList = articleRepository
                .findAllByTitleLike("%" + vo.getKeyword() + "%",
                        PageRequest.of(vo.getStart(), vo.getSize(), Sort.by(Sort.Direction.DESC, "createTime"))).getContent();
        result.setResultList(new ArrayList<>(resultList));
        return result;
    }

    @Override
    public CommonDTO<ArticleEntity> getArticle(PostVO vo) {
        CommonDTO<ArticleEntity> result = new CommonDTO<>();
        if (!StringTools.isNullOrEmpty(vo.getId())) {
            Optional<ArticleEntity> set = articleRepository.findById(vo.getId());
            set.get().setReadCount(set.get().getReadCount() + 1L);
            articleRepository.save(set.get());
            result.setResult(set.get());
        } else if (!StringTools.isNullOrEmpty(vo.getTitle())) {
            List<ArticleEntity> sets = articleRepository.findAllByTitleLike("%" + vo.getTitle() + "%", PageRequest.of(0, 10)).getContent();
            result.setResultList(sets);
            result.setTotal(Integer.valueOf(sets.size()).longValue());
        } else if (null != vo.getSize() && null != vo.getStart()) {
            Pageable pageable = PageRequest.of(vo.getStart(), vo.getSize(), Sort.by(Sort.Direction.DESC, "createTime"));
            Page<ArticleEntity> sets = articleRepository.findAll(pageable);
            result.setTotal(sets.getTotalElements());
            result.setResultList(sets.getContent());
        } else {
            Iterable<ArticleEntity> sets = articleRepository.findAll();
            result.setResultList(Lists.newArrayList(sets));
        }
        return result;
    }

    @Override
    public CommonDTO<ArticleEntity> deleteArticle(PostVO vo) {
        CommonDTO<ArticleEntity> result = new CommonDTO<>();
        articleRepository.deleteById(vo.getId());
        return result;
    }

    @Override
    public CommonDTO<String> getTags(PostVO vo) {
        CommonDTO<String> result = new CommonDTO<>();
        if (StringTools.isNotNullOrEmpty(vo.getKeyword())) {
            result.setResultList(tagRepository.findAllByTagLike(StringTools.getLikeSqlParams(vo.getKeyword())));
        } else {
            result.setResultList(tagRepository.findAllTag());
        }
        return result;
    }

}
