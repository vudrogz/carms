package cn.wawi.controller.business;

import java.util.List;

import cn.wawi.controller.BaseController;
import cn.wawi.model.business.Servicecategory;
import cn.wawi.utils.DbUtil;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.JsonRender;

@ControllerBind(controllerKey="/servicecategory")
public class ServiceCategoryController extends BaseController<Servicecategory>{
	

	
	public void getServiceCateegory(){
		//List<Record>  records =   Servicecategory.dao.getCategorys();
		//Map<String,Object> maps = new HashMap<String, Object>();
		
		List<Record> recordsA = Db.find("select * from servicecategory where deleted !=1 ");
		List<Record> recordsB = Db.find("select A.*,category as parentId from serviceitem A where deleted !=1 ");
		recordsA.addAll(recordsB);
		
		render(new JsonRender(DbUtil.findTree(recordsA)).forIE());

	}

}
