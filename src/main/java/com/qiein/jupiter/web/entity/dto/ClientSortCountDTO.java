package com.qiein.jupiter.web.entity.dto;

/**
 * FileName: ClientSortCountDTO
 *
 * @author: yyx
 * @Date: 2018-6-22 16:05
 */
public class ClientSortCountDTO {
    /**
     * 错误个数
     * */
    private int wrongCount;
    /**
     * 正常个数
     * */
    private int rightCount;
    /**
     * 重复个数
     * */
    private int repeats;

    public int getWrongCount() {
        return wrongCount;
    }

    public void setWrongCount(int wrongCount) {
        this.wrongCount = wrongCount;
    }

    public int getRightCount() {
        return rightCount;
    }

    public void setRightCount(int rightCount) {
        this.rightCount = rightCount;
    }

    public int getRepeats() {
        return repeats;
    }

    public void setRepeats(int repeats) {
        this.repeats = repeats;
    }
}
