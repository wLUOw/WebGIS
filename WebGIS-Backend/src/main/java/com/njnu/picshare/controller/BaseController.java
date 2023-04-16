package com.njnu.picshare.controller;

import com.njnu.picshare.domain.JsonResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Authod oruizn
 * @date 2021年11月2021/11/29 0029日下午 22:17
 */
public abstract class BaseController {
    protected String msg;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected JsonResult json;

    public BaseController() {

    }

    @ModelAttribute
    protected void setRequestResponse(HttpServletResponse res, HttpServletRequest req) {
        this.request = req;
        this.response = res;
        this.json = new JsonResult();
        this.msg = "message";
    }
}
