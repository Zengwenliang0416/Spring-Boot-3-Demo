package org.example.fileupload.entity;

public class FileInfo {
    private String name;
    private long size;

    public FileInfo(String name, long size) {
        this.name = name;
        this.size = size;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
