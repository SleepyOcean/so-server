package com.sleepy.blog.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.sleepy.blog.dto.CommonDTO;
import com.sleepy.blog.vo.LoggerVO;
import com.sleepy.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * 日志控制器
 *
 * @author gehoubao
 * @create 2019-08-29 14:24
 **/
@RestController
@CrossOrigin
@RequestMapping("/logger")
@Slf4j
public class LoggerController {

    @PostMapping("/level")
    public CommonDTO<String> logLevelAdjust(@RequestBody LoggerVO vo) throws Exception {
        CommonDTO<String> result = new CommonDTO<>();
        if (vo.getLoggers().size() > 0 && !StringUtil.isNullOrEmpty(vo.getLevel())) {
            Level level = getLoggerLevel(vo.getLevel());
            for (String s : vo.getLoggers()) {
                LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
                Logger logger = loggerContext.getLogger(s);
                ((Logger) logger).setLevel(level);
            }
            log.info("==================== 修改日志{}等级为{}级别", vo.getLoggers(), vo.getLevel());
            result.setResult("修改成功");
        } else {
            result.setResult("指定日志对象为空");
        }
        return result;
    }

    private Level getLoggerLevel(String level) throws Exception {
        if (level.equalsIgnoreCase(Level.ALL.toString())) {
            return Level.ALL;
        } else if (level.equalsIgnoreCase(Level.TRACE.toString())) {
            return Level.TRACE;
        } else if (level.equalsIgnoreCase(Level.DEBUG.toString())) {
            return Level.DEBUG;
        } else if (level.equalsIgnoreCase(Level.INFO.toString())) {
            return Level.INFO;
        } else if (level.equalsIgnoreCase(Level.WARN.toString())) {
            return Level.WARN;
        } else if (level.equalsIgnoreCase(Level.ERROR.toString())) {
            return Level.ERROR;
        } else if (level.equalsIgnoreCase(Level.OFF.toString())) {
            return Level.OFF;
        } else {
            throw new Exception("未被识别的日志级别：" + level);
        }
    }
}