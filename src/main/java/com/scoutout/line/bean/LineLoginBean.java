package com.scoutout.line.bean;

import java.net.URL;

public class LineLoginBean {
    private String accessToken;
    private String refreshToken;
    private String scope;
    private String tokenType;
    private String jwtIss;
    private String lineUserId;
    private String channelId;
    private String name;
    private URL pictureUrl;
    private String uuid;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getJwtIss() {
        return jwtIss;
    }

    public void setJwtIss(String jwtIss) {
        this.jwtIss = jwtIss;
    }

    public String getLineUserId() {
        return lineUserId;
    }

    public void setLineUserId(String lineUserId) {
        this.lineUserId = lineUserId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(URL pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "LineLoginBean{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", scope='" + scope + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", jwtIss='" + jwtIss + '\'' +
                ", lineUserId='" + lineUserId + '\'' +
                ", channelId='" + channelId + '\'' +
                ", name='" + name + '\'' +
                ", pictureUrl=" + pictureUrl +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
