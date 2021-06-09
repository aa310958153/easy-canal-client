package com.easy.canal.test.entity;

import java.io.Serializable;

/**
 * @Project easy-canal-parent
 * @PackageName com.easy.canal.test
 * @ClassName PaymentWayAndRelation
 * @Author qiang.li
 * @Date 2021/4/8 4:18 下午
 * @Description TODO
 */
public class PaymentWayAndRelation implements Serializable {
    private Long id;
    private String name;
    private String des;
    private String ways;
    //是否是快喝渠道
    private Boolean isB2CChannel;
    // 用于显示 支付类型配置
    private String types;

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getWays() {
        return ways;
    }

    public void setWays(String ways) {
        this.ways = ways;
    }

    public Boolean getB2CChannel() {
        return isB2CChannel;
    }

    public void setB2CChannel(Boolean b2CChannel) {
        isB2CChannel = b2CChannel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
