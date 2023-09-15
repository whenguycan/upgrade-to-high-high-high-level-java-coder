package com.ruoyi.framework.web.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class TreeSelectVO{
    /** 节点ID */
    private Long id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelectVO> children;
    /**
     * 切换场库是否集团端,0-否；1-是
     */
    private String isAdmin;
}
