$(function() {
	

	easyExt.initTreeGrid=function(id,url){
		$(id).treegrid({
			 url: easyExt.url+url,
			 method: 'get',
			 fitColumns : true,
			 idField : 'id',
			 treeField:'name',
			 fit : true,
			 rownumbers: true,
			 pageSize: 10,
			 autoRowHeight: true,
			 showRefresh: true,
			 pagination: true,
			 animate: true,
			 collapsible: true,
			 pagePosition: 'bottom',
			 /**
			 columns: [[{
				 title: '序号', 
		         field: 'id', 
		         align: 'center', 
		        
		         //复选框 
		         checkbox: true 
			 },
			 {
				 title: '名字', 
		         field: 'name', 
		         align: 'center',
		        
		         //复选框 
		        
			 }, {
				 title: '资源路径', 
		         field: 'url', 
		         align: 'center',
		         
		         //复选框 
		        
			 }, 
			 {
				 title: '权限编码', 
		         field: 'permCode', 
		         align: 'center'
		         //复选框 
		        
			 },
			 
			{
				 title: '排序', 
		         field: 'sort', 
		         align: 'center'
		         //复选框 
		        
			 },
			 
			 {
				 title: '描述', 
		         field: 'description', 
		         align: 'center'
		         //复选框 
		        
			 }
			
			 ]],
			 **/
			 onLoadError: function(){
				 $.messager.alert("提示", "数据加载失败！", "info");
			 }
		 }).treegrid('clientPaging');
	};
	
	//初始化表格
	easyExt.initTreeGrid('#tg','/sys_privilege/findAll'); 
	
	//删除实现
	$("#del").click(function(){
		var selRows=$('#tg').treegrid('getSelections');
		easyExt.del(selRows,'/sys_privilege/deleteBatch',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#tg').treegrid('reload'); 
		});
	});
	//添加实现
	$("#add").click(function(){
		easyExt.add('/sys_privilege/addOne',function(){
			$('#tg').treegrid('reload'); 
		});
	});
	//修改实现
	$("#edit").click(function(){
		var selRows=$('#tg').treegrid('getSelections');
		easyExt.edit(selRows,'/sys_privilege/updateOne',function(){
			$('#tg').treegrid('reload'); 
		});
	});
	//上级菜单
	$('#parent').combotree({
		width:180,
		method:'GET',
	    url: easyExt.url+'/sys_privilege/getMenu',
		iconCls: 'iconCls',
	    animate:true
	}); 
});

