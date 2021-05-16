package cn.wawi.model.sys.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseServiceitem<M extends BaseServiceitem<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setStatus(java.lang.Integer status) {
		set("status", status);
	}

	public java.lang.Integer getStatus() {
		return get("status");
	}

	public void setPayway(java.lang.String payway) {
		set("payway", payway);
	}

	public java.lang.String getPayway() {
		return get("payway");
	}

	public void setCreateTime(java.util.Date createTime) {
		
		set("createTime", createTime);
	}

	public java.util.Date getCreateTime() {
		return get("createTime");
	}

	public void setUpdateTime(java.util.Date updateTime) {
		set("updateTime", updateTime);
	}

	public java.util.Date getUpdateTime() {
		return get("updateTime");
	}

	public void setCategory(java.lang.Integer category) {
		set("category", category);
	}

	public java.lang.Integer getCategory() {
		return get("category");
	}
    
	public java.math.BigDecimal getSingleprice(){
		return get("singleprice");
	}
	
	public void setSingleprice(java.math.BigDecimal singleprice){
		set("singleprice",singleprice);
	}
	
	public void setDeleted(java.lang.Boolean deleted){
		set("deleted",deleted);
	}
	
	public java.lang.Boolean getDeleted(){
		return get("deleted");
	}
	
}
