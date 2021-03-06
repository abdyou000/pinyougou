package com.pinyougou.pojo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * tb_type_template
 *
 * @author
 */
public class TbTypeTemplate implements Serializable {
    private Long id;

    /**
     * 模板名称
     */
    @NotBlank(message = "模板名不能为空")
    @Size(min = 1,max = 20,message = "模板名长度必须在1-20之间")
    private String name;

    /**
     * 关联规格
     */
    @NotBlank(message = "关联规格不能为空")
    private String specIds;

    /**
     * 关联品牌
     */
    @NotBlank(message = "关联品牌不能为空")
    private String brandIds;

    /**
     * 自定义属性
     */
    private String customAttributeItems;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecIds() {
        return specIds;
    }

    public void setSpecIds(String specIds) {
        this.specIds = specIds;
    }

    public String getBrandIds() {
        return brandIds;
    }

    public void setBrandIds(String brandIds) {
        this.brandIds = brandIds;
    }

    public String getCustomAttributeItems() {
        return customAttributeItems;
    }

    public void setCustomAttributeItems(String customAttributeItems) {
        this.customAttributeItems = customAttributeItems;
    }

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
        TbTypeTemplate other = (TbTypeTemplate) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getSpecIds() == null ? other.getSpecIds() == null : this.getSpecIds().equals(other.getSpecIds()))
                && (this.getBrandIds() == null ? other.getBrandIds() == null : this.getBrandIds().equals(other.getBrandIds()))
                && (this.getCustomAttributeItems() == null ? other.getCustomAttributeItems() == null : this.getCustomAttributeItems().equals(other.getCustomAttributeItems()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getSpecIds() == null) ? 0 : getSpecIds().hashCode());
        result = prime * result + ((getBrandIds() == null) ? 0 : getBrandIds().hashCode());
        result = prime * result + ((getCustomAttributeItems() == null) ? 0 : getCustomAttributeItems().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", specIds=").append(specIds);
        sb.append(", brandIds=").append(brandIds);
        sb.append(", customAttributeItems=").append(customAttributeItems);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}