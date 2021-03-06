package cn.wawi.model.sys.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDictitem<M extends BaseDictitem<M>> extends Model<M> implements IBean {

	public String state="closed";
	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setCode(java.lang.String code) {
		set("code", code);
	}

	public java.lang.String getCode() {
		return get("code");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setParentId(java.lang.Integer parentId) {
		set("parentId", parentId);
	}
	@JsonProperty("_parentId")
	public java.lang.Integer getParentId() {
		return get("parentId");
	}

	public void setLevel(java.lang.Integer level) {
		set("level", level);
	}

	public java.lang.Integer getLevel() {
		return get("level");
	}

	public void setOrder(java.lang.Integer order) {
		set("order", order);
	}

	public java.lang.Integer getOrder() {
		return get("order");
	}

	public void setEnName(java.lang.String enName) {
		set("enName", enName);
	}

	public java.lang.String getEnName() {
		return get("enName");
	}

	public void setShortName(java.lang.String shortName) {
		set("shortName", shortName);
	}

	public java.lang.String getShortName() {
		return get("shortName");
	}

}
