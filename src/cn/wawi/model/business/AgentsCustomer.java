package cn.wawi.model.business;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import cn.wawi.model.sys.base.BaseAgentsCustomer;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class AgentsCustomer extends BaseAgentsCustomer<AgentsCustomer> {
	public static final AgentsCustomer dao = new AgentsCustomer();
	
	
	public List<Record> getAgentsServices(String agentsId){
		return Db.find("select serviceid id from agentsservice where agentsId=?",agentsId);
	}
	
	public AgentsCustomer login(String username,String password)
	{
	return findFirst("select * from agents_customer where account=? and password=? ",username,password );
		
	}
    
	
	
	
}