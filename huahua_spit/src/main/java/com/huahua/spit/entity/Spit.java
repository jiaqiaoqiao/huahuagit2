package com.huahua.spit.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

@Data
public class Spit implements Serializable {

    private static final long serialVersionUID = 923854195842574538L;
    @Id
    private String _id;

    private String content;

    private Date publishtime;

    private String nickname;

    private Integer visits;

    private Integer thumbup;

    private Integer share;

    private Integer comment;

    private String state;

    private String parentid;

    private String userid;
}
