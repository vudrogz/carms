package cn.wawi.controller;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.wawi.common.annotation.Permission;
import cn.wawi.common.interceptor.Json;
import cn.wawi.utils.MD5Util;
import cn.wawi.utils.SqlHelper;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.TableMapping;
import com.jfinal.render.JsonRender;
import com.mchange.v2.lang.StringUtils;

public abstract class BaseController<M extends Model<M>> extends Controller{

	private Class<M> clazz;
	private String tablename=null;
	private final Integer PAGE_SIZE=10;         //默认每页显示10条数据
	private final Integer PAGE_INDEX=1;         //默认当前页为第一页
	protected Json<M> json=null;       //用于存储json数据
	
	@SuppressWarnings("unchecked")
	public BaseController() {
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		this.clazz = (Class<M>) pt.getActualTypeArguments()[0];
		this.tablename=TableMapping.me().getTable(clazz).getName();
		json=new Json<M>();
	}
	@Permission("view")
	public void main(){
		String pName=clazz.getPackage().getName();
		String main="/views/"+clazz.getPackage().getName().substring(pName.lastIndexOf(".")+1)+"/"+clazz.getSimpleName()+"/main.html";
		renderFreeMarker(main);
	}
	/**
	 * 通用分页查找
	 */
	public void findAllByPage(){
		Page<M> list=getModel(clazz).paginateMysql(toPageIndex(), toPageSize(),getSql());
		//json.getResData().put("rows",list.getList());
		//json.getResData().put("total", list.getTotalRow());
		json.setRows(list.getList());
		json.setTotal(list.getTotalRow()+0L);
        render(new JsonRender(json).forIE());
	}

	/**
	 * 通用查找全部
	 */
	public void findAll(){
		List<M> list=getModel(clazz).find(getSql());
	//	json.getResData().put("rows",list);
	//	json.getResData().put("total", list.size());
		json.setRows(list);
		json.setTotal(list.size()+0L);
        render(new JsonRender(json).forIE());
	}


	/**
	 * 通用根据id查找
	 */
	public void findById(){
		M record=getModel(clazz).findById(getParaToInt("id"));

//		M record=getModel(clazz).findById(tablename,getParaToInt("id"));
		List<M> list=new ArrayList<M>();
		list.add(record);
	    json.setRows(list);
		//json.getResData().put("rows",list);
		//json.getResData().put("total", list.size());
		json.setTotal(1L);
		render(new JsonRender(json).forIE());
	}

	/**
	 * 通用新增
	 */
	@Permission("add")
	public void addOne(){
Model<?> m=getModel(clazz);
		
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
	 * 通用修改
	 */
	@Permission("update")
	public void updateOne(){
		Model<?> m=getModel(clazz);
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
	 * 通用删除
	 */
	@Permission("delete")
	public void delOne1(){
		if(!getModel(clazz).delete()){
			json.setResMsg("删除失败!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}
	
	public void delOne(){
		if(!getModel(clazz).updateDeletedStatus()){
			json.setResMsg("删除失败!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}
	
	
	
	/**
	 * 批量id删除
	 */
	@Permission("delete")
	public void deleteBatchTruth(){
		String ids=getPara("ids");
		if(Db.update("delete from "+tablename+" where id in ("+ids+")")<=0){
			json.setResMsg("批量删除失败!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}
	
	/**
	 * 批量id删除
	 */
	public void deleteBatch(){
		String ids=getPara("ids");
		if(Db.update("update "+tablename+" set  deleted = 1 where id in ("+ids+")")<=0){
			json.setResMsg("批量删除失败!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}

	/**
	 * 批量id更新
	 */
	@Permission("update")
	public void updateBatch(){
		String ids=getPara("ids");
		if(Db.update("update "+tablename+" set status=0 where id in ("+ids+")")<=0){
			json.setResMsg("批量更新失败!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}

	/**
	 * 获取当前页
	 * @return 当前页码
	 */
	public int toPageIndex(){
		String pageIndex=getPara("page")==null?getPara("pageIndex"):getPara("page");
		if(StrKit.isBlank(pageIndex)){
			return PAGE_INDEX;
		}
		return Integer.parseInt(pageIndex);
	}
	/**
	 * 获取每页显示多少条数据
	 * @return 当前页码
	 */
	public int toPageSize(){
		String pageSize=getPara("rows")==null?getPara("pageSize"):getPara("rows");
		if(StrKit.isBlank(pageSize)){
			return PAGE_SIZE;
		}
		return Integer.parseInt(pageSize);
	}

	/**
	 * 获取动态sql
	 */
	public  String getSql(){
		String sql=null;
		boolean flag=true;
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("deleted", 0);
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
			sql="select * from "+tablename + " where deleted = 0 ";
		}else{
			sql=SqlHelper.getSql("cn.wawi.common.Dao.find"+clazz.getSimpleName(),map);
		}
		return sql;
	}
}
