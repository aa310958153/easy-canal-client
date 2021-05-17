package com.easy.canal.test;

/**
 * 状态枚举
 *
 * @author david-liu
 * @date 2016年12月14日
 * @reviewer
 * @see
 */
public enum IStatus{
    DELETE(0), NORMAL(1);

    IStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private int value;
}
