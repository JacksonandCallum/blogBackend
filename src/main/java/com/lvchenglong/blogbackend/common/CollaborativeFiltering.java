package com.lvchenglong.blogbackend.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CollaborativeFiltering {
    private final Map<Integer, Map<Integer, Integer>> userBlogMatrix;

    public CollaborativeFiltering(Map<Integer, Map<Integer, Integer>> userBlogMatrix) {
        this.userBlogMatrix = userBlogMatrix;
    }

    public Map<Integer, Double> generateRecommendations(Integer blogId) {
        Map<Integer, Double> recommendations = new HashMap<>();

        // 获取与当前博客有交互的用户列表
        Set<Integer> interactingUserIds = userBlogMatrix.keySet();

        // 遍历每个用户
        for (Integer userId : interactingUserIds) {
            Map<Integer, Integer> blogInteractionMap = userBlogMatrix.get(userId);

            // 如果当前用户与当前博客有交互，则计算其相似度并添加到推荐列表中
            if (blogInteractionMap.containsKey(blogId)) {
                double similarity = calculateSimilarity(blogInteractionMap, blogId);
                recommendations.put(blogId, similarity);  // 将博客的 ID 放入推荐列表中
            }
        }

        return recommendations;
    }

    private double calculateSimilarity(Map<Integer, Integer> blogInteractionMap, Integer targetBlogId) {
        double similarity = 0.0;

        // 计算与目标博客的交互次数之和
        int interactionSum = blogInteractionMap.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        // 计算与目标博客的交互次数
        Integer targetInteraction = blogInteractionMap.get(targetBlogId);

        // 如果存在与目标博客的交互记录，则计算相似度
        if (targetInteraction != null && interactionSum != 0) {
            similarity = (double) targetInteraction / interactionSum;
        }

        return similarity;
    }
}
