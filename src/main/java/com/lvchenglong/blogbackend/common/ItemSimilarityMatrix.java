package com.lvchenglong.blogbackend.common;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ItemSimilarityMatrix {
    private Map<Integer, Map<Integer, Double>> matrix;

    public Map<Integer, Map<Integer, Double>> getMatrix() {
        return matrix;
    }

    public void setMatrix(Map<Integer, Map<Integer, Double>> matrix) {
        this.matrix = matrix;
    }

    /**
     * 获取指定博客ID的相似度信息，如果不存在，则返回空Map
     * @param blogId 指定的博客ID
     * @return 该博客的相似度信息，如果不存在则返回空Map
     */
    public Map<Integer, Double> getSimilarityForBlog(Integer blogId) {
        return matrix.getOrDefault(blogId, new HashMap<>());
    }

    /**
     * 获取指定博客ID的相似度信息，如果不存在，则返回默认值
     * @param blogId 指定的博客ID
     * @param defaultValue 默认值
     * @return 该博客的相似度信息，如果不存在则返回默认值
     */
    public Map<Integer, Double> getOrDefault(Integer blogId, Map<Integer, Double> defaultValue) {
        // 检查 matrix 是否为 null
        if (matrix == null) {
            return defaultValue; // 返回默认值或者其他处理方式
        }
        // 获取对应博客ID的相似度信息
        Map<Integer, Double> similarityMap = matrix.getOrDefault(blogId, defaultValue);
        return similarityMap;
    }
}
