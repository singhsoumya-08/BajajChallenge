package com.bajaj.health_challenge;

import java.util.List;

public class ApiResponse {
    private String webhook;
    private String accessToken;
    private Data data;

    public String getWebhook() {
        return webhook;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Data getData() {
        return data;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private UsersContainer users; // Matches the "users" key in JSON

        public UsersContainer getUsers() {
            return users;
        }

        public void setUsers(UsersContainer users) {
            this.users = users;
        }
    }

    public static class UsersContainer {
        private int findId;
        private int n;
        private List<User> users;

        public int getFindId() {
            return findId;
        }

        public void setFindId(int findId) {
            this.findId = findId;
        }

        public int getN() {
            return n;
        }

        public void setN(int n) {
            this.n = n;
        }

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }
    }

    public static class User {
        private int id;
        private String name;
        private List<Integer> follows;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Integer> getFollows() {
            return follows;
        }

        public void setFollows(List<Integer> follows) {
            this.follows = follows;
        }
    }
}