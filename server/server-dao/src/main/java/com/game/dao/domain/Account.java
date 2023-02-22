package com.game.dao.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName account
 */
@TableName(value ="account")
@Data
public class Account implements Serializable {
    /**
     * 主键

     */
    @TableId
    private String id;

    /**
     * 
     */
    private String accountName;

    /**
     * 
     */
    private String password;

    /**
     * 1:微信;2.facebook;3:tw;ins
     */
    private Integer thirdPlat;

    /**
     * 第三方平台ID
     */
    private String thirdId;

    /**
     * 其它扩展信息
     */
    private String extraInfo;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date lastUpdate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Account other = (Account) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getAccountName() == null ? other.getAccountName() == null : this.getAccountName().equals(other.getAccountName()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getThirdPlat() == null ? other.getThirdPlat() == null : this.getThirdPlat().equals(other.getThirdPlat()))
            && (this.getThirdId() == null ? other.getThirdId() == null : this.getThirdId().equals(other.getThirdId()))
            && (this.getExtraInfo() == null ? other.getExtraInfo() == null : this.getExtraInfo().equals(other.getExtraInfo()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getLastUpdate() == null ? other.getLastUpdate() == null : this.getLastUpdate().equals(other.getLastUpdate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAccountName() == null) ? 0 : getAccountName().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getThirdPlat() == null) ? 0 : getThirdPlat().hashCode());
        result = prime * result + ((getThirdId() == null) ? 0 : getThirdId().hashCode());
        result = prime * result + ((getExtraInfo() == null) ? 0 : getExtraInfo().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getLastUpdate() == null) ? 0 : getLastUpdate().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", accountName=").append(accountName);
        sb.append(", password=").append(password);
        sb.append(", thirdPlat=").append(thirdPlat);
        sb.append(", thirdId=").append(thirdId);
        sb.append(", extraInfo=").append(extraInfo);
        sb.append(", createTime=").append(createTime);
        sb.append(", lastUpdate=").append(lastUpdate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}