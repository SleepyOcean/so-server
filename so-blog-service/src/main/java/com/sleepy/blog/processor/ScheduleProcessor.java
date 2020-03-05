package com.sleepy.blog.processor;

import com.sleepy.common.tools.CommandTools;
import com.sleepy.common.tools.DateTools;
import com.sleepy.common.tools.FileTools;
import com.sleepy.common.tools.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * 定时任务处理器
 *
 * @author gehoubao
 * @create 2019-09-30 14:11
 **/
@Component
@Slf4j
public class ScheduleProcessor {

    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    public static void main(String[] args) throws IOException, InterruptedException {
        ScheduleProcessor pass = new ScheduleProcessor();
        System.out.println(pass.username + " , " + pass.password);
//        new ScheduleProcessor().backupData("test", "so_project", "SoProject");
    }

    public void backupSoProject() {
        try {
            backupData("test", "so_project", "SoProject");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void backupSoSetting() {
        try {
            backupData("test", "so_setting", "SoSetting");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void backupSoImg() {
        try {
            backupData("test", "so_img", "SoImg");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 备份数据
     *
     * @param dbName         需要备份的数据库名称
     * @param tableName      需要备份的表名称
     * @param tableClassName 定义备份表对应的类名称
     * @throws IOException
     * @throws InterruptedException
     */
    private void backupData(String dbName, String tableName, String tableClassName) throws IOException, InterruptedException {
        String now = DateTools.dateFormat(new Date(), DateTools.FLYWAY_SQL_FILE_NAME_PATTERN);
        String path = getStoreSqlPath() + "V" + now + StringTools.getRandomNumString(2) + "__" + tableClassName;
        String command = "mysqldump -h localhost -u" + username + " -p" + password + " --databases " + dbName + " --tables " + tableName + " -r " + path + ".sql";
        String result = CommandTools.execute(command);
        System.out.println(result);
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