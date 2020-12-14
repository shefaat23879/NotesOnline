package com.example.simplenoteonline;

public class NoteModel {

    public String title;
    public String content;
    public Long timestamp;



    NoteModel() {
    }

    public NoteModel(String title, Long timestamp) {
        this.title = title;
        this.timestamp = timestamp;
    }

    public NoteModel(String noteTitle, String noteTime , Long timestamp) {
        this.title = noteTitle;
        this.content = noteTime;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
