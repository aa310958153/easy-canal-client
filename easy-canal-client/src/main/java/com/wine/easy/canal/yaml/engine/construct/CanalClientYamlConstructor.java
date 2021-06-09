package com.wine.easy.canal.yaml.engine.construct;

import com.wine.easy.canal.yaml.config.CanalClientServiceLoader;
import com.wine.easy.canal.yaml.engine.construct.CanalClientYamlConstruct;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * @Project easy-canal-client-parent
 * @PackageName com.wine.easy.canal.yaml.config
 * @ClassName CanalYamlConstructor
 * @Author qiang.li
 * @Date 2021/5/31 9:34 上午
 * @Description TODO
 */
public class CanalClientYamlConstructor   extends Constructor {


    private final Map<Class, Construct> typeConstructs = new HashMap<>();
    public CanalClientYamlConstructor(final Class<?> rootClass) {
        super(rootClass);
        CanalClientServiceLoader.newServiceInstances(CanalClientYamlConstruct.class).forEach(each -> typeConstructs.put(each.getType(), each));
    }

    @Override
    protected Construct getConstructor(final Node node) {
        return typeConstructs.getOrDefault(node.getType(), super.getConstructor(node));
    }
}
