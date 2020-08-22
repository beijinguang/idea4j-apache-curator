package com.idea4j.apache.curator;

public class Config {

    private String path = "/";

    private String hostPort =  "127.0.0.1:2181";

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHostPort() {
        return hostPort;
    }

    public void setHostPort(String hostPort) {
        this.hostPort = hostPort;
    }
}
