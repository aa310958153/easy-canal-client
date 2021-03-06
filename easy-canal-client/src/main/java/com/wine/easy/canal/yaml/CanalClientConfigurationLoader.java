package com.wine.easy.canal.yaml;

import com.google.common.base.Preconditions;
import com.wine.easy.canal.config.CanalClientConfig;
import com.wine.easy.canal.config.ELKConfig;
import com.wine.easy.canal.core.CanalListenerWorker;
import com.wine.easy.canal.yaml.engine.YamlEngine;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Project easy-canal-client-parent
 * @PackageName com.wine.easy.canal.yaml
 * @ClassName CanalClientYmlConfigurationLoad
 * @Author qiang.li
 * @Date 2021/5/31 9:54 上午
 * @Description TODO
 */
public class CanalClientConfigurationLoader {

    private static final String CANAL_CONFIG_FILE = "/canal/canal.yml";
    private static final String CANAL_ELK_CONFIG_FILE = "/canal/elk.yml";
    private static final Logger logger = LoggerFactory.getLogger(CanalClientConfigurationLoader.class);

    /**
     * Load configuration of
     *
     * @return configuration of canal-client
     * @throws IOException IO exception
     */
    public static CanalClientConfig load() throws IOException {
        File file=  getResourceFile(CANAL_CONFIG_FILE);
        CanalClientConfig serverConfig = loadServerConfiguration(file);
        return serverConfig;
    }

    public static ELKConfig loadElkConfig() throws IOException {
        File file=getResourceFile(CANAL_ELK_CONFIG_FILE);
        ELKConfig elkConfig = loadElkConfig(file);
        return elkConfig;
    }

    private static File getResourceFile(final String path) throws IOException {
        //针对java -jar 形式启动 读取资源路径
        try (InputStream input = CanalClientConfigurationLoader.class.getClassLoader().getResourceAsStream(path)) {
            if (null != input) {
                String fileName=path.substring(path.lastIndexOf("/")+1);
                File file = new File(fileName);
                //将读取到的类容存储到临时文件中，后面就可以用这个临时文件访问了
                FileUtils.copyInputStreamToFile(input, file);
                if(!file.exists()){
                    throw new FileNotFoundException(String.format("notFund file path:%s",path));
                }
                return file;
            }else{
                throw new FileNotFoundException(String.format("notFund file path:%s",path));
            }
        }catch (FileNotFoundException e){
            //尝试从classPath下读取
           URL url= CanalClientConfigurationLoader.class.getResource(path);
           if(url!=null) {
               return new File(url.getFile());
           }
           throw e;
        }
    }

    private static CanalClientConfig loadServerConfiguration(final File yamlFile) throws IOException {
        CanalClientConfig result = YamlEngine.unmarshal(yamlFile, CanalClientConfig.class);
        Preconditions.checkNotNull(result, "Server configuration file `%s` is invalid.", yamlFile.getName());
        return result;
    }

    private static ELKConfig loadElkConfig(final File yamlFile) throws IOException {
        ELKConfig result = YamlEngine.unmarshal(yamlFile, ELKConfig.class);
        Preconditions.checkNotNull(result, "Server configuration file `%s` is invalid.", yamlFile.getName());
        return result;
    }


}
