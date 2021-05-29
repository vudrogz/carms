package cn.wawi.controller.business;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import cn.wawi.common.annotation.Permission;
import cn.wawi.common.interceptor.SellerTokenInterceptor;
import cn.wawi.controller.BaseController;
import cn.wawi.model.business.ConsumRecord;
import cn.wawi.model.business.Member;
import cn.wawi.model.business.Payrecord;
import cn.wawi.model.sys.User;
import cn.wawi.utils.MD5Util;
import cn.wawi.utils.QRCodeUtil;

import com.google.zxing.WriterException;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.render.JsonRender;
import com.mchange.v2.lang.StringUtils;

@ControllerBind(controllerKey="/member")
public class MemberController extends BaseController<Member>{
	
	
	//生成一批二维码
	
	
	//修改二维码
	
	
	//会员审核(后台)
	
	//会员充值(手机端)...
	
	//销售人员()...
	
	//后台子帐号列表
	
	
	
	//消费
	
	//员工token
	@Before(SellerTokenInterceptor.class)
	public void addMember(){
		Model<Member> m=getModel(Member.class);
		m.set("userid", getPara("userid"));
		if(!m.save())
	    {
			json.setResMsg("添加失败!");
			json.setResCode(0);
		}
		
	}
	
	//更新二维码
	@Before(SellerTokenInterceptor.class)
	public void updateMemberQRcode(){
		int x = Db.update("update member set code=? where userid=? and id =? ", getPara("code"),getPara("userid"),getPara("id"));
		if(x==0){
			json.setResMsg("失败!");
			json.setResCode(0);
		}
	}
	
	@Before(SellerTokenInterceptor.class)
	public void findMyMember(){
		Page<Member> list=getModel(Member.class).paginateMysql(toPageIndex(), toPageSize(),"select * from member where id!=1 and userid = ?",getPara("userid"));
		json.setRows(list.getList());
		json.setTotal(list.getTotalRow()+0L);
        render(new JsonRender(json).forIE());
		
	}
	
	
	//修改二维码
	@Before(SellerTokenInterceptor.class)
	public void updateMember()
	{
		Model<?> m=getModel(Member.class);
		if(m.dbHasProp("updateTime"))
		{
			m.set("updateTime", new Date());
		}
		if(m.dbHasProp("password")&&StringUtils.nonEmptyString(m.getStr("password")))
		{
			m.set("password", MD5Util.MD5(m.getStr("password")));
		}
		if(!m.update()){
			json.setResMsg("修改失败!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());	
	}
	
	
	
	/**
	 * 通用新增
	 */
	@Permission("add")
	public void addOne(){	
		 UUID uuid = UUID.randomUUID();
		 String fileName =  System.currentTimeMillis()+".png";
		String path = null;
		try {
			String q = this.getClass().getClassLoader().getResource("/").getPath();
			File file = new File(q);
			QRCodeUtil.encode(uuid.toString(), file.getParentFile().getParentFile().getPath()+"/code/" ,fileName);
			path = "/code/"+fileName;
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		Model<Member> m=getModel(Member.class);
		m.set("code",path );
		User user=getSessionAttr("loginUser");
        m.set("userid", user.getId());
		if(!m.save()){
			json.setResMsg("添加失败!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}
    
	@Before(Tx.class)
	public void openService(){
		getRequest().getParameter("user");
		User user=getSessionAttr("loginUser");
		Model<Payrecord> m=getModel(Payrecord.class);
        m.set("userid", user.getId());
	}
	
	public void loadOpenService(){
		render(new JsonRender(Db.find("select id,name namex from serviceitem where id in(select SERVICEid from memberservice where MEMBERID =?) and deleted !=1 ",getRequest().getParameter("mid"))).forIE());
	}
	
	/**
	 * 会员审核
	 */
	@Permission("checkMember")
	public void validateMember(){
		System.out.println(getRequest().getParameter("mid"));
	    int mid =    getParaToInt("mid");
        int cst=  getParaToInt("checkStatus");
		if(cst==1)
		{
			Db.update("update member set checkStatus=1 , status=1 where id =? ",mid);
		}else{
			Db.update("update member set checkStatus=2 ,remarker=? where id =? ",getPara("remarker"),mid);	
		}
		render(new JsonRender(json).forIE());
        
	}
	
	
    
	@Before(Tx.class)
	public void addMoney(){
		getRequest().getParameter("user");
		User user=getSessionAttr("loginUser");
		Model<Payrecord> m=getModel(Payrecord.class);
        m.set("userid", user.getId());
        String memberid = String.valueOf(m.get("memberid"));
        Record record = Member.dao.getMemberById(memberid); //用户金钱
        BigDecimal allmoney =  record.get("money");
        allmoney.add(new BigDecimal((String)m.get("money"))); //金钱
        Integer totalcount = record.get("totalcount");
        String count =  m.get("count");
        m.save();
        Db.update("update member set totalcount = ? ,money=? where id = ? ",totalcount+count,allmoney,memberid);
    	json.setResMsg("充值成功!");
		json.setResCode(0);
		render(new JsonRender(json).forIE());
        
	}
	
	
	
	
	@Before(Tx.class)
	public void cosume(){
		//会员二位码  - >会员id
		//商户开通服务   用户拥有这个服务
		//商户ID
		//服务ID
		//扣点数
		//扣次数
		
	   String memberid = getRequest().getParameter("memberid");  //会员Id  
	   String agentsid = getRequest().getParameter("agentId"); //代理商员工Id
	   String serviceid = getRequest().getParameter("serviceid"); //服务项目Id
	   
	   //会员
	   Record record_member = Db.findFirst("select SERVICEid,times from memberservice where MEMBERID =? and serviceid=? ) and deleted !=1",memberid,serviceid);
	   if(record_member==null){
		   json.setResMsg("会员未开通此服务");
		   json.setResCode(0);
		   render(new JsonRender(json).forIE());
	       return;
	   }
	   Record memberInfo = Db.findById("member", memberid);
	   
	   
	   //检测商户是否开通此服务
	   Record as=  Db.findFirst("select parent_id from agents_customer where id = ?",agentsid);
	   Record record_agents = null;
	   if(as.getStr("parent_id")==null){
		   record_agents = Db.findFirst("select * from agentsservice where agentsid=? and serviceid = ?  ",agentsid,serviceid);
	   }else{
		   record_agents= Db.findFirst("select * from agentsservice where agentsid=? and serviceid = ?  ",as.getStr("parent_id"),serviceid);
	   }
	   if(record_agents==null){
		   json.setResMsg("商户未开通此服务");
		   json.setResCode(0);
		   render(new JsonRender(json).forIE());
	       return;
	   }
	   
	   Record serviceItem =  Db.findById("serviceitem", "id", serviceid);
	   int times = record_member.getInt("times"); //会员拥有的服务次数
	   String payway =serviceItem.get("payway");
	   String price = serviceItem.get("singleprice");
	   ConsumRecord record = new ConsumRecord();
	   record.setAgentsid(agentsid);
	   record.setMemberid(Integer.valueOf(memberid));
	   record.setServiceitem(serviceid);
	   if(payway.contains("1")&&Integer.valueOf(price)==0)
	   {
		   //免费
           record.setCount(0);  //消费0点
           record.setPayway("1"); //按点数支付
           record.setMessage("系统赠送...");
           if(record.save()){
        	   json.setResMsg("免费服务!");
    		   json.setResCode(1);
    		   render(new JsonRender(json).forIE());
           }else{
        	   json.setResMsg("支付失败!,请联系服务员");
    		   json.setResCode(1);
    		   render(new JsonRender(json).forIE());
           }
	   }else{
		   
	     if(payway.equals("2")){
	    	   if(times==0){
	    		   json.setResMsg("次数不足，请充值");
				   json.setResCode(0);
				   render(new JsonRender(json).forIE());
			       return;
	    	   }else{
	    		   //扣次数
	    		   record.setTimes(1);;  //消费0点
	               record.setPayway("2"); //按点数支付
	               record.setMessage("消费....");
	               record.save();
	                Db.update("update memberservice set time = time-1   where memberid = ?,serviceid = ?  ",memberid,serviceid);
                   json.setResMsg("消费成功");
				   json.setResCode(1);
				    render(new JsonRender(json).forIE());
	    	       return ;	   
	    	   }
	    	  
	     }
	     
	     if(payway.equals("1"))
	     {
	    	 //只支持点数
	    	 if(Integer.valueOf(price)>memberInfo.getInt("totalcount"))
	    	 {
	    		     // 点数不够，请充值
	    		   json.setResMsg("点数不足，请充值");
				   json.setResCode(0);
				   render(new JsonRender(json).forIE());
			       return;  
	    	 }else{
	    		 //扣点数s
	    		 //扣次数
	    		   record.setCount(Integer.valueOf(price));  //消费0点
	               record.setPayway("1"); //按点数支付
	               record.setMessage("消费点数....");
	               Db.update("update  member set totalcount= totalcount-"+Integer.valueOf(price)+" where id = ?", memberid);
	               record.save();
	                Db.update("update memberservice set time = time-1   where memberid = ?,serviceid = ?  ",memberid,serviceid);
                 
	    		 json.setResMsg("消费成功");
				 json.setResCode(1);
				 render(new JsonRender(json).forIE());
	    		 
	    	     return ;
	    	 }
	    }
	     
	     
	    if(payway.contains("1, 2"))
	    {
	    	//既支持点数，又支持次数，先扣次数..
	    	
	    	if(times>0)
	    	{
	    		//先扣次数。。
	    		  record.setTimes(1);;  //消费0点
	               record.setPayway("2"); //按点数支付
	               record.setMessage("消费....");
	               record.save();
	                Db.update("update memberservice set time = time-1   where memberid = ?,serviceid = ?  ",memberid,serviceid);
                  json.setResMsg("消费成功");
				   json.setResCode(1);
				    render(new JsonRender(json).forIE());
	    	       return ;	   
	    		
	    	}
	    	
	    	
	    	if(Integer.valueOf(price)>memberInfo.getInt("totalcount"))
	    	 {
	    		     // 点数不够，请充值
	    		   json.setResMsg("不足，请充值");
				   json.setResCode(0);
				   render(new JsonRender(json).forIE());
			       return;  
	    	 }else{
	    		 //扣点数s
	    		 //扣次数
	    		   record.setCount(Integer.valueOf(price));  //消费0点
	               record.setPayway("1"); //按点数支付
	               record.setMessage("消费点数....");
	               Db.update("update  member set totalcount= totalcount-"+Integer.valueOf(price)+" where id = ?", memberid);
	               record.save();
	                Db.update("update memberservice set time = time-1   where memberid = ?,serviceid = ?  ",memberid,serviceid);
                
	    		 json.setResMsg("消费成功");
				 json.setResCode(1);
				 render(new JsonRender(json).forIE());
	    		 
	    	     return ;
	    	 }
	    	

	    	
	    }	
	     
	     
	     
	     
	     
	     
	     
		   
		   //会员拥有的点数
	   }
	   
	   
	  
	   
	   
	   
	   
	}
	
	
	
	/*
	 * 获取商户已经开通的服务
	 */
	public void getServices(){
		render(new JsonRender(Member.dao.getMemberServices(getPara("id"))).forIE());
	}
	
	/*
	 * 保存角色对应的权限
	 */
	@Before(Tx.class)
	public void saveMemberService(){

		String memberid=getPara("memberid");
		Db.update("delete from memberservice where memberid=?", memberid);
		String privilegeIds=getPara("privilegeIds");
		if(privilegeIds!=null&&!"".equals(privilegeIds.trim())){
			String[] privileges=privilegeIds.split(",");
			Object[][] o=new Object[privileges.length][2];
			for (int i = 0; i < o.length; i++) {
				o[i][0]=memberid;
				o[i][1]=privileges[i];
			}
			Db.batch("insert into memberservice(memberid,serviceid) values(?,?)",o, 1000);
		}
		render(new JsonRender(json).forIE());
	}
	
}
