package cn.wawi.controller.business;

import java.util.Arrays;
import java.util.List;

import cn.wawi.common.annotation.Permission;
import cn.wawi.controller.BaseController;
import cn.wawi.model.business.Serviceitem;
import cn.wawi.utils.DbUtil;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.render.JsonRender;

@ControllerBind(controllerKey="/serviceitem")
public class ServiceItemController extends BaseController<Serviceitem>{
	
	/*
	 * 获取菜单树
	 */
	public void getCategory(){
		render(new JsonRender(DbUtil.findTree(Db.find("select * from servicecategory where deleted !=1 "))).forIE());
	}
	
	
	
	
	
	
	
	public void addOne(){
		Model<?> m=getModel(Serviceitem.class);
		StringBuffer sb = new StringBuffer(Arrays.toString(getRequest().getParameterValues("payway")));
		sb.deleteCharAt(0);
		sb.deleteCharAt(sb.length()-1);
		m.set("payway", sb.toString());
		if(!m.save()){
			json.setResMsg("添加失败!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}
	
	
	/**
	 * 通用修改
	 */
	
	public void updateOne(){
		Model<?> m=getModel(Serviceitem.class);
		StringBuffer sb = new StringBuffer(Arrays.toString(getRequest().getParameterValues("payway")));
		sb.deleteCharAt(0);
		sb.deleteCharAt(sb.length()-1);
		m.set("payway", sb.toString());
		if(!m.update()){
			json.setResMsg("修改失败!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}
	
	

}
