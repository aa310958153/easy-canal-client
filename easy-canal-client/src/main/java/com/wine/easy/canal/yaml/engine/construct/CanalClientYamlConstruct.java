package com.wine.easy.canal.yaml.engine.construct;

import org.yaml.snakeyaml.constructor.Construct;

/**
 * @Project easy-canal-client-parent
 * @PackageName com.wine.easy.canal.yaml.config
 * @ClassName CanalClientYamlConstruct
 * @Author qiang.li
 * @Date 2021/5/31 9:41 上午
 * @Description TODO
 */
public interface CanalClientYamlConstruct extends Construct {

    /**
     * Get type.
     *
     * @return type
     */
    Class<?> getType();
}