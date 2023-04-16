package com.njnu.picshare.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @Authod oruizn
 * @date 2021年11月2021/11/29 0029日下午 22:18
 */
@Data
public class JsonResult implements Serializable {
    private static final long serialVersionUID = -6559362101721248596L;
    private Object data;
    private String msg;
    private String url;
}
