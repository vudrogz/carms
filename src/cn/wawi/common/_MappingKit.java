package cn.wawi.common;

import cn.wawi.model.business.AgentsCustomer;
import cn.wawi.model.business.Agentscustomeremployee;
import cn.wawi.model.business.Agentsservice;
import cn.wawi.model.business.Member;
import cn.wawi.model.business.Memberservice;
import cn.wawi.model.business.Servicecategory;
import cn.wawi.model.business.Serviceitem;
import cn.wawi.model.sys.Department;
import cn.wawi.model.sys.Dict;
import cn.wawi.model.sys.Dictitem;
import cn.wawi.model.sys.Log;
import cn.wawi.model.sys.Privilege;
import cn.wawi.model.sys.Role;
import cn.wawi.model.sys.User;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("sys_department", "id", Department.class);
		arp.addMapping("sys_dict", "id", Dict.class);
		arp.addMapping("sys_dictitem", "id", Dictitem.class);
		arp.addMapping("sys_log", "id", Log.class);
		arp.addMapping("sys_privilege", "id", Privilege.class);
		arp.addMapping("sys_role", "id", Role.class);
		arp.addMapping("sys_user", "id", User.class);
		arp.addMapping("agents_customer", "id", AgentsCustomer.class);
		arp.addMapping("servicecategory", "id", Servicecategory.class);
		arp.addMapping("serviceitem", "id", Serviceitem.class);
		arp.addMapping("agentscustomeremployee", "id", Agentscustomeremployee.class);
		arp.addMapping("agentsservice", "id", Agentsservice.class);
		arp.addMapping("member", "id", Member.class);
		arp.addMapping("memberservice", "id", Memberservice.class);		
		arp.addMapping("payrecord", "id", cn.wawi.model.business.Payrecord.class);

	}
}
