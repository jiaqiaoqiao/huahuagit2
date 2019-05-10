package com.huahua.article.pojo;


import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
public class CommentMongoDB implements Serializable {

    private static final long serialVersionUID = -8728547194559381445L;
    @Id
    private String _id;

    private String articleid;

    private String content;

    private String userid;

    private String parentid;

    private Date publishdate;


}