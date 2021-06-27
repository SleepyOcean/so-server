package com.sleepy.media.theater.controller;

import com.sleepy.media.theater.service.SoMetaInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author SleepyOcean
 * @create 2020-12-30 16:44:25
 */
@RestController
@CrossOrigin
@RequestMapping("")
public class SoMetaInfoController {
    @Autowired
    SoMetaInfoService soMetaInfoService;
}
