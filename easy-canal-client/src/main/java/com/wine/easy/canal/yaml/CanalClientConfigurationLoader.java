package com.wine.easy.canal.yaml;

import com.google.common.base.Preconditions;
import com.wine.easy.canal.config.CanalClientConfig;
import com.wine.easy.canal.config.ELKConfig;
import com.wine.easy.canal.yaml.engine.YamlEngine;

import java.io.File;
import java.io.IOException;
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

    private static  final String  CANAL_CONFIG_FILE="/canal/canal.yml";
    private static  final String  CANAL_ELK_CONFIG_FILE="/canal/elk.yml";
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

    private static File getResourceFile(final String path) {
        URL url = CanalClientConfigurationLoader.class.getResource(path);
        if (null != url) {
            return new File(url.getFile());
        }
        return new File(path);
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
