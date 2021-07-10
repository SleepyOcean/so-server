package com.sleepy.common.constant;

/**
 * http请求返回状态码定义
 *
 * @author gehoubao
 * @create 2020-03-05 15:14
 **/
public enum HttpStatus {
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    NOT_AUTHORITATIVE(203),
    NO_CONTENT(204),
    RESET(205),
    PARTIAL(206),
    MULT_CHOICE(300),
    MOVED_PERM(301),
    MOVED_TEMP(302),
    SEE_OTHER(303),
    NOT_MODIFIED(304),
    USE_PROXY(305),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    PAYMENT_REQUIRED(402),
    FORBIDDEN(403),
    NOT_FOUND(404),
    BAD_METHOD(405),
    NOT_ACCEPTABLE(406),
    PROXY_AUTH(407),
    CLIENT_TIMEOUT(408),
    CONFLICT(409),
    GONE(410),
    LENGTH_REQUIRED(411),
    PRECON_FAILED(412),
    ENTITY_TOO_LARGE(413),
    REQ_TOO_LONG(414),
    UNSUPPORTED_TYPE(415),
    INTERNAL_ERROR(500),
    NOT_IMPLEMENTED(501),
    BAD_GATEWAY(502),
    UNAVAILABLE(503),
    GATEWAY_TIMEOUT(504),
    VERSION(505);

    private int code;

    HttpStatus(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}