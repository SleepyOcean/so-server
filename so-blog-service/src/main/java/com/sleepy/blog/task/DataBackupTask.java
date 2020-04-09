package com.sleepy.blog.task;

import com.sleepy.common.tools.CommandTools;
import com.sleepy.common.tools.DateTools;
import com.sleepy.common.tools.FileTools;
import com.sleepy.common.tools.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Date;

/**
 * 数据备份任务
 *
 * @author gehoubao
 * @create 2020-04-09 11:22
 **/
@Slf4j
public class DataBackupTask {
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    /**
     * 备份mysql数据
     *
     * @param dbName         需要备份的数据库名称
     * @param tableName      需要备份的表名称
     * @param tableClassName 定义备份表对应的类名称
     * @param storePath      定义数据备份本地路径
     * @throws IOException
     * @throws InterruptedException
     */
    private void mysqlDataBackup(String dbName, String tableName, String tableClassName, String storePath) throws IOException, InterruptedException {
        if (StringTools.isNullOrEmpty(storePath)) {
            storePath = getStoreSqlPath();
        }
        String now = DateTools.dateFormat(new Date(), DateTools.FLYWAY_SQL_FILE_NAME_PATTERN);
        String path = storePath + "V" + now + StringTools.getRandomNumString(2) + "__" + tableClassName;
        String command = "mysqldump -h localhost -u" + username + " -p" + password + " --databases " + dbName + " --tables " + tableName + " -r " + path + ".sql";
        String result = CommandTools.execute(command);
        log.info("【MySql数据库备份任务】备份结束。表名：{}，路径：{}，输出：{}", tableName, storePath, result);
    }

    /**
     * 获取flyway的路径
     *
     * @return
     * @throws IOException
     */
    private String getStoreSqlPath() throws IOException {
        return FileTools.getProjectPath() + "\\src\\main\\resources\\db\\migration\\";
    }
}