package com.ruoyi.project.system.domain;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门表 sys_dept
 *
 * @author ruoyi
 */
@ApiModel(value = "场库信息",description = "")
public class SysDept extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 场库id */
    private Long deptId;
    /** 父场库id */
    @Excel(name = "父场库id")
    @ApiModelProperty(value = "父场库id")
    private Long parentId;
    /** 祖级列表 */
    @Excel(name = "祖级列表")
    @ApiModelProperty(value = "祖级列表")
    private String ancestors;
    /** 场库名称 */
    @Excel(name = "场库名称")
    @ApiModelProperty(value = "场库名称")
    private String deptName;
    /** 显示顺序 */
    @Excel(name = "显示顺序")
    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;
    /** 负责人 */
    @Excel(name = "负责人")
    @ApiModelProperty(value = "负责人")
    private String leader;
    /** 联系电话 */
    @Excel(name = "联系电话")
    @ApiModelProperty(value = "联系电话")
    private String phone;
    /** 邮箱 */
    @Excel(name = "邮箱")
    @ApiModelProperty(value = "邮箱")
    private String email;
    /** 部门状态（0正常 1停用） */
    @Excel(name = "部门状态", readConverterExp = "0=正常,1=停用")
    @ApiModelProperty(value = "部门状态")
    private String status;
    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;
    /** 编号【规则：p+yyyyMMddHHmmss】 */
    @Excel(name = "编号【规则：p+yyyyMMddHHmmss】")
    @ApiModelProperty(value = "编号【规则：p+yyyyMMddHHmmss】")
    private String parkNo;
    /** 停车场地址 */
    @Excel(name = "停车场地址")
    @ApiModelProperty(value = "停车场地址")
    private String address;
    /** 银行名称 */
    @Excel(name = "银行名称")
    @ApiModelProperty(value = "银行名称")
    private String bank;
    /** 银行卡号 */
    @Excel(name = "银行卡号")
    @ApiModelProperty(value = "银行卡号")
    private String bankCard;
    /** 授权码【uuid】 */
    @Excel(name = "授权码【uuid】")
    @ApiModelProperty(value = "授权码【uuid】")
    private String authCode;
    /** 纬度 */
    @Excel(name = "纬度")
    @ApiModelProperty(value = "纬度")
    private String latitude;
    /** 经度 */
    @Excel(name = "经度")
    @ApiModelProperty(value = "经度")
    private String longitude;


//    /** 部门ID */
//    private Long deptId;
//
//    /** 父部门ID */
//    private Long parentId;
//
//    /** 祖级列表 */
//    private String ancestors;
//
//    /** 部门名称 */
//    private String deptName;
//
//    /** 显示顺序 */
//    private Integer orderNum;
//
//    /** 负责人 */
//    private String leader;
//
//    /** 联系电话 */
//    private String phone;
//
//    /** 邮箱 */
//    private String email;
//
//    /** 部门状态:0正常,1停用 */
//    private String status;
//
//    /** 删除标志（0代表存在 2代表删除） */
//    private String delFlag;

    /** 父部门名称 */
    private String parentName;

    /** 子部门 */
    private List<SysDept> children = new ArrayList<SysDept>();

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public String getAncestors()
    {
        return ancestors;
    }

    public void setAncestors(String ancestors)
    {
        this.ancestors = ancestors;
    }

    @NotBlank(message = "部门名称不能为空")
    @Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
    public String getDeptName()
    {
        return deptName;
    }


    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    public String getLeader()
    {
        return leader;
    }

    public void setLeader(String leader)
    {
        this.leader = leader;
    }

    @Size(min = 0, max = 11, message = "联系电话长度不能超过11个字符")
    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public List<SysDept> getChildren()
    {
        return children;
    }

    public void setChildren(List<SysDept> children)
    {
        this.children = children;
    }

    public String getParkNo() {
        return parkNo;
    }

    public void setParkNo(String parkNo) {
        this.parkNo = parkNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deptId", getDeptId())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("deptName", getDeptName())
            .append("orderNum", getOrderNum())
            .append("leader", getLeader())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
