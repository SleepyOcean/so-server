package com.sleepy.media.theater.pojo;

import lombok.Getter;

/**
 * 电影DTO
 *
 * @author gehoubao
 * @create 2020-10-21 20:35
 **/
@Getter
public class MovieDTO {
    private String id;
    private String uuid;
    private String name;
    private String originalName;
    private String chineseName;
    private String publishYear;
    private String date;
    private String country;
    private String linkDouban;
    private String scoreDouban;
    private String scoreIMDB;
    private String score;
    private String runningTime;
    private String alias;
    private String director;
    private String scriptwriter;
    private String actor;
    private String type;
    private String tags;
    private String series;
    private String intro;
    private String postUrl;
    private String postUrlVertical;
    private String postUrlHorizon;
    private String captureUrls;
    private String trailerUrls;
    private String updateTime;
    private String note;
    private String downloadLinks;
    private String localPath;

    public static MovieDTO newBuilder() {
        return new MovieDTO();
    }

    public MovieDTO setId(String id) {
        this.id = id;
        return this;
    }

    public MovieDTO setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public MovieDTO setName(String name) {
        this.name = name;
        return this;
    }

    public MovieDTO setOriginalName(String originalName) {
        this.originalName = originalName;
        return this;
    }

    public MovieDTO setChineseName(String chineseName) {
        this.chineseName = chineseName;
        return this;
    }

    public MovieDTO setPublishYear(String publishYear) {
        this.publishYear = publishYear;
        return this;
    }

    public MovieDTO setDate(String date) {
        this.date = date;
        return this;
    }

    public MovieDTO setCountry(String country) {
        this.country = country;
        return this;
    }

    public MovieDTO setLinkDouban(String linkDouban) {
        this.linkDouban = linkDouban;
        return this;
    }

    public MovieDTO setScoreDouban(String scoreDouban) {
        this.scoreDouban = scoreDouban;
        return this;
    }

    public MovieDTO setScoreIMDB(String scoreIMDB) {
        this.scoreIMDB = scoreIMDB;
        return this;
    }

    public MovieDTO setScore(String score) {
        this.score = score;
        return this;
    }

    public MovieDTO setRunningTime(String runningTime) {
        this.runningTime = runningTime;
        return this;
    }

    public MovieDTO setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public MovieDTO setDirector(String director) {
        this.director = director;
        return this;
    }

    public MovieDTO setScriptwriter(String scriptwriter) {
        this.scriptwriter = scriptwriter;
        return this;
    }

    public MovieDTO setActor(String actor) {
        this.actor = actor;
        return this;
    }

    public MovieDTO setType(String type) {
        this.type = type;
        return this;
    }

    public MovieDTO setTags(String tags) {
        this.tags = tags;
        return this;
    }

    public MovieDTO setSeries(String series) {
        this.series = series;
        return this;
    }

    public MovieDTO setIntro(String intro) {
        this.intro = intro;
        return this;
    }

    public MovieDTO setPostUrl(String postUrl) {
        this.postUrl = postUrl;
        return this;
    }

    public MovieDTO setPostUrlVertical(String postUrlVertical) {
        this.postUrlVertical = postUrlVertical;
        return this;
    }

    public MovieDTO setPostUrlHorizon(String postUrlHorizon) {
        this.postUrlHorizon = postUrlHorizon;
        return this;
    }

    public MovieDTO setCaptureUrls(String captureUrls) {
        this.captureUrls = captureUrls;
        return this;
    }

    public MovieDTO setTrailerUrls(String trailerUrls) {
        this.trailerUrls = trailerUrls;
        return this;
    }

    public MovieDTO setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public MovieDTO setNote(String note) {
        this.note = note;
        return this;
    }

    public MovieDTO setDownloadLinks(String downloadLinks) {
        this.downloadLinks = downloadLinks;
        return this;
    }

    public MovieDTO setLocalPath(String localPath) {
        this.localPath = localPath;
        return this;
    }
}