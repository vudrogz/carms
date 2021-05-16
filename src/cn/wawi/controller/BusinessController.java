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

public abstract class BusinessController<M extends Model<M>> extends Controller {
	private Class<M> clazz;
	private String tablename=null;
	private final Integer PAGE_SIZE=10;         //Ĭ��ÿҳ��ʾ10������
	private final Integer PAGE_INDEX=1;         //Ĭ�ϵ�ǰҳΪ��һҳ
	protected Json<M> json=null;       //���ڴ洢json����
	
	@SuppressWarnings("unchecked")
	public BusinessController() {
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
	 * ͨ�÷�ҳ����
	 */
	public void findAllByPage(){
		Page<M> list=getModel(clazz).paginateMysql(toPageIndex(), toPageSize(),getSql());
		json.getResData().put("rows",list.getList());
		json.getResData().put("total", list.getTotalRow());
		json.setRows(list.getList());
		json.setTotal(list.getTotalRow()+0L);
        render(new JsonRender(json).forIE());
	}

	/**
	 * ͨ�ò���ȫ��
	 */
	public void findAll(){
		List<M> list=getModel(clazz).find(getSql());
		json.getResData().put("rows",list);
		json.getResData().put("total", list.size());
		json.setRows(list);
		json.setTotal(list.size()+0L);
        render(new JsonRender(json).forIE());
	}


	/**
	 * ͨ�ø���id����
	 */
	public void findById(){
		M record=getModel(clazz).findById(getParaToInt("id"));

//		M record=getModel(clazz).findById(tablename,getParaToInt("id"));
		List<M> list=new ArrayList<M>();
		list.add(record);
	    json.setRows(list);
		json.getResData().put("rows",list);
		json.getResData().put("total", list.size());
		json.setTotal(1L);
		render(new JsonRender(json).forIE());
	}

	/**
	 * ͨ������
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
			json.setResMsg("���ʧ��!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}

	/**
	 * ͨ���޸�
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
			json.setResMsg("�޸�ʧ��!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}
	/**
	 * ͨ��ɾ��
	 */
	@Permission("delete")
	public void delOne1(){
		if(!getModel(clazz).delete()){
			json.setResMsg("ɾ��ʧ��!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}
	
	public void delOne(){
		if(!getModel(clazz).updateDeletedStatus()){
			json.setResMsg("ɾ��ʧ��!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}
	
	
	

	
	/**
	 * ����idɾ��
	 */
	public void deleteBatch(){
		String ids=getPara("ids");
		if(Db.update("delete from "+tablename+" where id in ("+ids+")")<=0){
			json.setResMsg("����ɾ��ʧ��!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}

	/**
	 * ����id����
	 */
	@Permission("update")
	public void updateBatch(){
		String ids=getPara("ids");
		if(Db.update("update "+tablename+" set status=0 where id in ("+ids+")")<=0){
			json.setResMsg("��������ʧ��!");
			json.setResCode(0);
		}
		render(new JsonRender(json).forIE());
	}

	/**
	 * ��ȡ��ǰҳ
	 * @return ��ǰҳ��
	 */
	public int toPageIndex(){
		String pageIndex=getPara("page")==null?getPara("pageIndex"):getPara("page");
		if(StrKit.isBlank(pageIndex)){
			return PAGE_INDEX;
		}
		return Integer.parseInt(pageIndex);
	}
	/**
	 * ��ȡÿҳ��ʾ����������
	 * @return ��ǰҳ��
	 */
	public int toPageSize(){
		String pageSize=getPara("rows")==null?getPara("pageSize"):getPara("rows");
		if(StrKit.isBlank(pageSize)){
			return PAGE_SIZE;
		}
		return Integer.parseInt(pageSize);
	}

	/**
	 * ��ȡ��̬sql
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
		   if(null!=value&&!"".equals(value.trim())){//���˿�ֵ����ַ���
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
