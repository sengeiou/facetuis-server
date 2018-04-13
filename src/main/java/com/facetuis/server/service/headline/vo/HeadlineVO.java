package com.facetuis.server.service.headline.vo;

import java.io.Serializable;
import java.util.List;

public class HeadlineVO implements Serializable{

    public class HeadlineContent{
        private String img;
        private String txt;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }
    }

    private String id;
    private String title;
    private List<HeadlineContent> content;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<HeadlineContent> getContent() {
        return content;
    }

    public void setContent(List<HeadlineContent> content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

