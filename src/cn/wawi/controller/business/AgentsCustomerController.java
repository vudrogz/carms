package cn.wawi.controller.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.wawi.common.annotation.Permission;
import cn.wawi.common.interceptor.AgentTokenInterceptor;
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
import com.mchange.v2.lang.StringUtils;

@ControllerBind(controllerKey="/agents_customer")
public class AgentsCustomerController extends BaseController<AgentsCustomer>{
	
	
	
	@Permission("add")
	public void addOne(){
     Model<?> m=getModel(AgentsCustomer.class);
		if(m.dbHasProp("createTime"))
		{
			m.set("createTime", new Date());
		}
		
		if(m.dbHasProp("password")&&StringUtils.nonEmptyString(m.getStr("password")))
		{
			m.set("password", MD5Util.MD5(m.getStr("password")));
		}
		if(!m.save()){
			json.setResMsg("添加失败!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}
	
	
	  /**
     * PC添加子账户
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
	 * 
	 */
	@Before(AgentTokenInterceptor.class)
	public void addSubAgent(){
		Model<?> m=getModel(AgentsCustomer.class);
		m.set("parent_id", getRequest().getParameter("agentId"));
		m.set("password",MD5Util.MD5(getRequest().getParameter("password")));
		m.remove("id");
		if(!m.save()){
			json.setResMsg("添加失败!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}
	
	
	@Before(AgentTokenInterceptor.class)
	public void loadSubAgent(){
		String memberid  =  getRequest().getParameter("agentId");
	    AgentsCustomer agentsCustomer = AgentsCustomer.dao.findById(memberid);
		AgentsCustomer record=getModel(AgentsCustomer.class).findById(getParaToInt("id"));
        record.setPassword("");
        record.setName(agentsCustomer.getName());
        record.setAddress(agentsCustomer.getAddress());
		List<AgentsCustomer> list=new ArrayList<AgentsCustomer>();
		list.add(record);
	    json.setRows(list);
		;
		json.setTotal(1L);
		render(new JsonRender(json).forIE());
	}
	
	/**
	 * 删除子账户
	 */
	@Before(AgentTokenInterceptor.class)
	public void updateSubAgent(){
		try{
		Model<?> m=getModel(AgentsCustomer.class);
		m.set("password",MD5Util.MD5(getRequest().getParameter("password")));
		if(!m.update()){
			json.setResMsg("修改失败!");
			json.setResCode(0);
		}
		}catch(Exception e)
		{
			json.setResMsg("修改失败!"+e.getMessage());
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}
	
	/**
	 * 删除子账户
	 */
	@Before(AgentTokenInterceptor.class)
	public void deleteSubAgent(){
		String ids=getPara("ids");
		String parent_id = getPara("agentId");
		if(Db.update("update agents_customer  where  parent_id= ? id in ("+ids+")",parent_id)<=0){
			json.setResMsg("批量删除失败!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}
	
	
	
	
	
	
	/**
	 * pc删除子账户
	 */
	public void deleteManage(){
		String ids=getPara("ids");
		String parent_id = getPara("agentId");
		if(Db.update("update agents_customer  where  parent_id= ? id in ("+ids+")",parent_id)<=0){
			json.setResMsg("批量删除失败!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}
	
	
	
	/**
	 * PC修改子账户信息
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
	
	
	//pcs
	public void findMyManager(){
        String memberid  =  getRequest().getParameter("agentId");
		Page<AgentsCustomer> list=getModel(AgentsCustomer.class).paginateMysql(toPageIndex(), toPageSize()," select id,person,contactphone,remark,account,deleted,parent_id,createTime,updateTime,status from  agents_customer where deleted!=1 and  parent_id = "+memberid);
		json.setRows(list.getList());
		json.setTotal(list.getTotalRow()+0L);
        render(new JsonRender(json).forIE());
	}
	
	/**
	 * 我的员工,子帐号
	 */
	@Before(AgentTokenInterceptor.class)

	public void findSubAgent(){
	
        String memberid  =  getRequest().getParameter("agentId");
        AgentsCustomer agentsCustomer = AgentsCustomer.dao.findById(memberid);
		Page<AgentsCustomer> list=getModel(AgentsCustomer.class).paginateMysql(toPageIndex(), toPageSize()," select id,person,contactphone,remark,account,deleted,parent_id,createTime,updateTime,status from  agents_customer where deleted!=1 and  parent_id = "+memberid);
		List<AgentsCustomer> lt =list.getList();
		for(AgentsCustomer aget:lt)
		{
			aget.setName(agentsCustomer.getName());
			aget.setAddress(agentsCustomer.getAddress());
		}
		//json.getResData().put("rows",list.getList());
		//json.getResData().put("total", list.getTotalRow());
		json.setRows(list.getList());
		json.setTotal(list.getTotalRow()+0L);
        render(new JsonRender(json).forIE());
	}
	
	@Before(AgentTokenInterceptor.class)
	public void findServiceCategoryTree(){
        List<Record> recordsA = Db.find("select * from servicecategory where deleted !=1 ");
    	List<Record> recordsB = Db.find("select A.*,B.serviceid from (select A.*,category as parentId from serviceitem A where deleted !=1)A left join (select * from agentsservice where agentsid = 5 ) B on A.id = B.serviceid");
		recordsA.addAll(recordsB);	
		render(new JsonRender(DbUtil.findTree(recordsA)).forIE());

    
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
	
	
	 
	
	
	
	public void agentLogin(){
		 AgentsCustomer	user=AgentsCustomer.dao.login(getPara("account"), MD5Util.MD5(getPara("password")));
        	if(user==null){
        		json.setResMsg("用户名或者密码不对!");
    			json.setResCode(0);
        	}else{
        		setSessionAttr("loginAgentsCustomer", user);
        		Map<String, Object> map = new HashMap<String, Object>();
        		
        		long token =    System.currentTimeMillis();  
        		Db.update("update agents_customer set token=? where id=? ",token,user.getId());
        		user.setToken(String.valueOf(token));
        		json.setRow(user);
        		json.setResMsg("登录成功!");
    			json.setResCode(1);
        	}
    	
		    render(new JsonRender(json).forIE());
	}
	
	
	
	
	
	public void login() {
		AgentsCustomer user=getSessionAttr("loginAgentsCustomer");
    	if(user==null){
        	user=AgentsCustomer.dao.login(getPara("account"), MD5Util.MD5(getPara("password")));
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