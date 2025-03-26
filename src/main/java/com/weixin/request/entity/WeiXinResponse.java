package com.weixin.request.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author weiyao_tian 2025/3/15
 */
@Data
@ToString
public class WeiXinResponse {

private String openId;
private String unionId;
private String userId;

private Integer isPerson;



}
