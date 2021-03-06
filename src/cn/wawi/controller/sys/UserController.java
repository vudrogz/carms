package cn.wawi.controller.sys;

import cn.wawi.common.annotation.Logs;
import cn.wawi.common.interceptor.GlobalInterceptor;
import cn.wawi.common.interceptor.LoginInterceptor;
import cn.wawi.controller.BaseController;
import cn.wawi.model.sys.Privilege;
import cn.wawi.model.sys.User;
import cn.wawi.utils.MD5Util;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.render.JsonRender;

@ControllerBind(controllerKey="/sys_user")
public class UserController extends BaseController<User>{

	@Logs(des="用户登录")
	@Clear(GlobalInterceptor.class)
	@Before(LoginInterceptor.class)
	public void login() {
		User user=getSessionAttr("loginUser");
    	if(user==null){
        	user=User.dao.login(getPara("username"), MD5Util.MD5(getPara("password")));
        	if(user==null){
            	setAttr("msg", "用户名或密码不对!");
            	renderFreeMarker("/login.html");
        	}
    	}
		setSessionAttr("loginUser", user);
		setAttr("permissions", Privilege.dao.findUserPermission(user.get("id"),"F"));
		renderFreeMarker("/index.html");
	}
	
	
	public void userlogin() {		
    	User user=User.dao.login(getPara("username"),MD5Util.MD5(getPara("password")));
        if(user==null){
        		json.setResMsg("用户名或者密码不对!");
    			json.setResCode(0);
        }else{
        		long token = System.currentTimeMillis();
        		user.setToken(String.valueOf(token));  //  
		        		user.update();
        	    json.setRow(user);
        	    json.setResMsg("登录成功!");
    			json.setResCode(1);
        }
		render(new JsonRender(json).forIE());
	}
	
	
	public void exit(){
		getSession().removeAttribute("loginUser");
		getSession().invalidate();
		renderFreeMarker("/login.html");
	}
}
