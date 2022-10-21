package net.mcson.supportbot;

public class Keyword {
    private String keyword;
    private String response;

    public Keyword(String keyword, String response) {
        this.keyword = keyword;
        this.response = response;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "{ \"keyword\":\"" + keyword + "\", \"response\":\"" + response + "\" }";
    }
}
