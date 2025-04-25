package com.bajaj.health_challenge;

import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class NthLevelFollowerService {
    public List<Integer> findNthLevelFollowers(int findId, int n, List<ApiResponse.User> users) {
        Map<Integer, List<Integer>> adjacencyList = new HashMap<>();
        for (ApiResponse.User user : users) {
            adjacencyList.put(user.getId(), user.getFollows());
        }

        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.add(findId);
        visited.add(findId);
        int currentLevel = 0;

        while (!queue.isEmpty() && currentLevel < n) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                int current = queue.poll();
                for (int neighbor : adjacencyList.getOrDefault(current, Collections.emptyList())) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
            currentLevel++;
        }

        return (currentLevel == n) ? new ArrayList<>(queue) : Collections.emptyList();
    }
}