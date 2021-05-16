package cn.wawi.controller.business;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import cn.wawi.common.annotation.Permission;
import cn.wawi.controller.BaseController;
import cn.wawi.model.business.Member;
import cn.wawi.model.business.Payrecord;
import cn.wawi.model.sys.User;
import cn.wawi.utils.DbUtil;
import cn.wawi.utils.QRCodeUtil;

import com.google.zxing.WriterException;
import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.render.JsonRender;

@ControllerBind(controllerKey="/member")
public class MemberController extends BaseController<Member>{
	
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
	   String memberid =	getRequest().getParameter("memberid");
	   String serviceid = getRequest().getParameter("serviceid");
       String times = getRequest().getParameter("times");
       String payway = getRequest().getParameter("payway");
       
       
       
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
