package com.sy.sysobackend.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 诺诺
 */
@Data
public class Picture implements Serializable {

    private String title;

    private String url;

    private static final long serialVersionUID = 1L;
}