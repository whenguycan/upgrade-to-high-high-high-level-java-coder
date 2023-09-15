package com.ruoyi.project.parking.domain.param;

import lombok.Data;

/**
 * 会员对象
 * 
 * @author fangch
 */
@Data
public class MemberUserParam {

    /** 手机号 */
    private String phonenumber;

    /** 会员类型 */
    private String memberType;

    /** 页码 */
    private Integer pageNum;

    /** 页大小 */
    private Integer pageSize;

}
