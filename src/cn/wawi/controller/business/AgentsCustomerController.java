package cn.wawi.controller.business;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import cn.wawi.controller.BaseController;
import cn.wawi.model.business.AgentsCustomer;
import cn.wawi.model.sys.Privilege;
import cn.wawi.utils.MD5Util;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.render.JsonRender;

@ControllerBind(controllerKey="/agents_customer")
public class AgentsCustomerController extends BaseController<AgentsCustomer>{
	
	  /**
     * 添加子账户
     */
	public void addManage(){
		Model<?> m=getModel(AgentsCustomer.class);
		m.set("password",MD5Util.MD5(getRequest().getParameter("password")));
		m.remove("id");
		if(!m.save()){
			json.setResMsg("添加失败!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}
	
	/**
	 * 删除子账户
	 */
	public void deleteManage(){
		String ids=getPara("ids");
		String parent_id = getPara("agentsId");
		if(Db.update("update agents_customer  where  parent_id= ? id in ("+ids+")",parent_id)<=0){
			json.setResMsg("批量删除失败!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}
	
	
	
	/**
	 * 修改子账户信息
	 * 
	 */
	
	public void updateManage(){
		Model<?> m=getModel(AgentsCustomer.class);
		m.set("password",MD5Util.MD5(getRequest().getParameter("password")));
		if(!m.save()){
			json.setResMsg("添加失败!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}
	
	
	/**
	 * 我的员工,子帐号
	 */
	public void findMyMember(){
		AgentsCustomer user=getSessionAttr("loginAgentsCustomer");
        String memberid = "";
        memberid =  (user!=null? String.valueOf(user.getId()):getRequest().getParameter("agentsId"));
		Page<AgentsCustomer> list=getModel(AgentsCustomer.class).paginateMysql(toPageIndex(), toPageSize()," select * from  agents_customer where deleted!=1 and  parent_id = "+memberid);
		json.getResData().put("rows",list.getList());
		json.getResData().put("total", list.getTotalRow());
		json.setRows(list.getList());
		json.setTotal(list.getTotalRow()+0L);
        render(new JsonRender(json).forIE());
	}
	
	
	public void findServiceCategoryTree(){
		
		
		
		
	}
	
	
	public  String getSql(){
		String sql=null;
		boolean flag=true;
		Map<String,Object> map=new HashMap<String,Object>();
		Enumeration<String> names=getParaNames();
		while(names.hasMoreElements()){
		   String name=(String) names.nextElement();
		   String value=getPara(name);
		   if(null!=value&&!"".equals(value.trim())){//过滤空值或空字符串
			   map.put(name, value.trim());
		   }
		   if("page".equals(name)||"rows".equals(name)){
		   }else{
			   flag=false;
		   }
		}
		if(flag){
			sql="select * from agents_customer WHERE deleted!=1 and parent_id is null";
		}else{
			
		}
		return sql;
	}
	
	
	 
	
	
	
	public void mobilelogin(){
		AgentsCustomer user=getSessionAttr("loginAgentsCustomer");
    	if(user==null){
        	user=AgentsCustomer.dao.login(getPara("username"), MD5Util.MD5(getPara("password")));
        	if(user==null){
        		json.setResMsg("用户名或者密码不对!");
    			json.setResCode(0);
        	}else{
        		setSessionAttr("loginAgentsCustomer", user);
        		json.setResMsg("登录成功!");
    			json.setResCode(1);
        	}
    	}else{
    		json.setResMsg("登录成功!");
			json.setResCode(1);
    	}
		render(new JsonRender(json).forIE());
	}
	
	
	
	
	
	public void login() {
		AgentsCustomer user=getSessionAttr("loginAgentsCustomer");
    	if(user==null){
        	user=AgentsCustomer.dao.login(getPara("username"), MD5Util.MD5(getPara("password")));
        	if(user==null){
            	setAttr("msg", "用户名或密码不对!");
            	renderFreeMarker("/agentlogin.html");
            	return ;
        	}
    	}
		setSessionAttr("loginAgentsCustomer", user);
		setAttr("permissions", Privilege.dao.findUserPermission(11,"F"));
		renderFreeMarker("/index.html");
	}
	
	/*
	 * 获取商户已经开通的服务
	 */
	public void getServices(){
		render(new JsonRender(AgentsCustomer.dao.getAgentsServices(getPara("id"))).forIE());
	}
	
	/*
	 * 保存角色对应的权限
	 */
	@Before(Tx.class)
	public void saveAgentsService(){

		String roleId=getPara("agenstId");
		Db.update("delete from agentsservice where agentsid=?", roleId);
		String privilegeIds=getPara("privilegeIds");
		if(privilegeIds!=null&&!"".equals(privilegeIds.trim())){
			String[] privileges=privilegeIds.split(",");
			Object[][] o=new Object[privileges.length][2];
			for (int i = 0; i < o.length; i++) {
				o[i][0]=roleId;
				o[i][1]=privileges[i];
			}
			Db.batch("insert into agentsservice(agentsid,serviceid) values(?,?)",o, 1000);
		}
		render(new JsonRender(json).forIE());
	}

}
