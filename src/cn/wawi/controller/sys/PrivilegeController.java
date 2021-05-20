package cn.wawi.controller.sys;

import cn.wawi.common.annotation.Permission;
import cn.wawi.controller.BusinessController;
import cn.wawi.model.sys.Privilege;
import cn.wawi.utils.DbUtil;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.render.JsonRender;

@ControllerBind(controllerKey="/sys_privilege")
public class PrivilegeController extends BusinessController<Privilege>{

	/*
	 * 添加基本权限
	 */
	@Permission("sys:role:savePrivilege")
	public void addPermission(){
		int[] i=Db.batch("insert into sys_privilege(name,iconCls,permCode,description,parentId,sort,type) values(?,?,?,?,?,?,?)", new Object[][]{{"添加 ","icon-standard-add","add","拥有添加权限",getPara("pId"),"99","O"},{"删除 ","icon-standard-delete","delete","拥有删除权限",getPara("pId"),"99","O"},{"修改 ","icon-standard-table-edit","update","拥有修改权限",getPara("pId"),"99","O"},{"查看 ","icon-standard-application-view-list","view","拥有查看权限",getPara("pId"),"99","O"}}, 1000);
		if(i==null||i.length<0){
			json.setResMsg("添加基本权限失败!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}
	/*
	 * 获取菜单树
	 */
	public void getMenu(){
		render(new JsonRender(DbUtil.findTree(Db.find("select * from sys_privilege where type='F' and deleted != 1 "))).forIE());
	}
	/*
	 * 跳转到菜单页面
	 */
	@Permission("sys:privilege:menu:view")
	public void menu(){
		renderFreeMarker("/views/sys/Privilege/menu.html");
	}
}
