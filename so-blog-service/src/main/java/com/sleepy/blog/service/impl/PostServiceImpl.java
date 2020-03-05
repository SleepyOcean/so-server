package com.sleepy.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.sleepy.blog.common.Constant;
import com.sleepy.blog.dto.CommonDTO;
import com.sleepy.blog.entity.ArticleEntity;
import com.sleepy.blog.entity.TagEntity;
import com.sleepy.blog.repository.ArticleRepository;
import com.sleepy.blog.repository.TagRepository;
import com.sleepy.blog.service.CacheService;
import com.sleepy.blog.service.ImgService;
import com.sleepy.blog.service.PostService;
import com.sleepy.blog.vo.ImgVO;
import com.sleepy.blog.vo.PostVO;
import com.sleepy.common.tools.DateTools;
import com.sleepy.common.tools.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

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
        List<String> articleIds = tagRepository.findArticleIdsByTags(vo.getTags().split(","));
        CommonDTO<ArticleEntity> result = new CommonDTO<>();
        List<ArticleEntity> resultList = articleRepository.findAllByIds(articleIds);
        result.setResultList(resultList);
        return result;
    }

    @Override
    public CommonDTO<String> saveArticle(PostVO vo) throws Exception {
        CommonDTO<String> result = new CommonDTO<>();
        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(vo.getTitle());
        entity.setContent(vo.getContent());
        entity.setSummary(vo.getSummary());
        if (!StringTools.isNullOrEmpty(vo.getCoverImg())) {
            entity.setCoverImg(getImgUrl(vo.getCoverImg(), vo.getTitle()));
        }
        entity.setCreateTime(DateTools.toDate(vo.getDate(), DateTools.DEFAULT_DATETIME_PATTERN));
        entity.setTags(vo.getTags());
        if (!StringTools.isNullOrEmpty(vo.getId())) {
            entity.setId(vo.getId());
            articleRepository.saveAndFlush(entity);
        } else {
            articleRepository.save(entity);
        }

        // 存储文章标签
        String[] tags = vo.getTags().split(",");
        for (int i = 0; i < tags.length; i++) {
            TagEntity tag = new TagEntity();
            tag.setTagName(tags[i]);
            tag.setArticleId(entity.getId());
            tagRepository.save(tag);
        }
        result.setResult("success");

        return result;
    }

    private String getImgUrl(String base64Str, String title) throws IOException {
        ImgVO vo = new ImgVO();
        vo.setImgOfBase64(base64Str);
        vo.setAlias("《" + title + "》封面");
        vo.setType(Constant.IMG_TYPE_COVER);
        vo.setTags("文章封面");
        vo.setAssociateAttribute("文章封面:" + title);
        return JSON.parseObject(imgService.upload(vo)).getString("url");
    }

    @Override
    public CommonDTO<ArticleEntity> searchArticle(PostVO vo) throws IOException {
        CommonDTO<ArticleEntity> result = new CommonDTO<>();
        Set<ArticleEntity> resultList = new HashSet<>();
        String[] titles = vo.getKeyword().split("\\s");
        for (String title : titles) {
            resultList.addAll(articleRepository.findAllByTitleLike("%" + title + "%"));
        }

        resultList.forEach(article -> {
            for (String title : titles) {
                article.setTitle(article.getTitle().replaceAll(title, "<span style='font-weight: bold; color: #5D82ED'>" + title + "</span>"));
            }
        });
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
            List<ArticleEntity> sets = articleRepository.findAllByTitleLike("%" + vo.getTitle() + "%");
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
        result.setResultList(tagRepository.findAllTag());
        return result;
    }

}
