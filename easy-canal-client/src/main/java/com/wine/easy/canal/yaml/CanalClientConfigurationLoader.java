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
     * @param path configuration canal-client
     * @return configuration of canal-client
     * @throws IOException IO exception
     */
    public static CanalClientConfig load() throws IOException {
        CanalClientConfig serverConfig = loadServerConfiguration(getResourceFile(CANAL_CONFIG_FILE));
        return serverConfig;
    }

    public static ELKConfig loadElkConfig() throws IOException {
        ELKConfig elkConfig = loadElkConfig(getResourceFile(CANAL_ELK_CONFIG_FILE));
        return elkConfig;
    }

    private static File getResourceFile(final String path) throws IOException {
        try (InputStream input = CanalClientConfigurationLoader.class.getClassLoader().getResourceAsStream(path)) {
            if (null != input) {
                String fileName=path.substring(path.lastIndexOf("/")+1);
                logger.info("临时fileName:{}",fileName);
                File file = new File(fileName);
                //将读取到的类容存储到临时文件中，后面就可以用这个临时文件访问了
                FileUtils.copyInputStreamToFile(input, file);
                return file;
            }
            return new File(path);
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
