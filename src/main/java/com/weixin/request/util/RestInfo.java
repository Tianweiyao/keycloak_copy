package com.weixin.request.util;

import lombok.Data;
import lombok.ToString;

/**
 * @author weiyao_tian 2025/3/16
 */
@Data
@ToString
public class RestInfo {

    private Object data;
    private String message;
    private String code;


}
