package com.wine.easy.canal.core;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.google.common.collect.Lists;
import io.netty.util.internal.StringUtil;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project easy-canal-parent
 * @PackageName com.wine.easy.canal.core
 * @ClassName ConnectionFactory
 * @Author qiang.li
 * @Date 2021/3/29 1:35 下午
 * @Description TODO
 */
public class ConnectionFactory {
    public static CanalConnector create(CanalInfoConfig canalInfoConfig){
        if(!StringUtil.isNullOrEmpty(canalInfoConfig.getHosts())){
            List<InetSocketAddress> inetSocketAddressList=new ArrayList<>();
            String[] hosts=canalInfoConfig.getHosts().split(",");
            for (String hostInfo:
            hosts) {
               String[] hostAndPort= hostInfo.split(":");
                inetSocketAddressList.add(new InetSocketAddress(hostAndPort[0], Integer.valueOf(hostAndPort[1])));
            }
            return CanalConnectors.newClusterConnector(inetSocketAddressList,canalInfoConfig.getDestination(), canalInfoConfig.getUsername(),canalInfoConfig.getPassword());
        }else{
           return CanalConnectors.newClusterConnector(canalInfoConfig.getZkHosts(), canalInfoConfig.getDestination(), canalInfoConfig.getUsername(), canalInfoConfig.getPassword());
        }

    }
}
