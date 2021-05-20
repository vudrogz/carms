$(function() {
	//初始化表格
	

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
			 autoRowHeight: false,
			 showRefresh: true,
			 pagination: true,
			 animate: true,
			 collapsible: true,
			 pagePosition: 'bottom',
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
		         align: 'center'
		         //复选框 
		        
			 }
			 ]],
			
			 onLoadError: function(){
				 $.messager.alert("提示", "数据加载失败！", "info");
			 },onLoadSuccess:function(row, data){
				 
			 }
		 }).treegrid('clientPaging');
	};
	
	easyExt.initDataGrid('#dg','/member/findAllByPage');
	easyExt.initTreeGrid('#tg','/servicecategory/getServiceCateegory'); 

	//easyExt.initDataGrid('#tg','/serviceitem/findAll');

	//删除实现
	$("#del").click(function(){
		var selRows=$('#dg').datagrid('getSelections');
		easyExt.del(selRows,'/member/deleteBatch',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#dg').datagrid('reload'); 
		});
	});
	
	$("#check").click(function(){
		var selRows=$('#dg').datagrid('getSelections');
        $("#mid").val(selRows[0].id);
		$('#add4Dialog').dialog('open').dialog('setTitle','审核');

	});
	
	
	
	//添加实现
	$("#add").click(function(){
		easyExt.add('/member/addOne',function(){
			$('#dg').datagrid('reload'); 
		});
	});
	//修改实现
	$("#edit").click(function(){
		var selRows=$('#dg').datagrid('getSelections');
		easyExt.edit(selRows,'/member/updateOne',function(){//删除成功后执行的动作，一般用于刷新datagrid
			$('#dg').datagrid('reload'); 
		});
	});
	//搜索实现
	$("#search").click(function(){
		$('#dg').datagrid('load',$('#tForm').serializeJson());
	});
	
	//上级菜单
	$('#parent').combotree({
		width:180,
		method:'GET',
	    url: easyExt.url+'/member/getCategory',
		iconCls: 'iconCls',
	    animate:true
	}); 

});



var member=member ||{};


function checks()
{
    var jt=  $("#checkForm").serialize();
	easyExt.ajax({url:easyExt.url+"/member/validateMember?"+jt,type:'post'},function(data,status, xhr){
		$('#add4Dialog').dialog('close');
		$('#dg').datagrid('reload'); 

	});	
}

//查看服务管理
function lookP(id){
	member.id=id;
	$('#xray').dialog('open').dialog('setTitle','查看服务');

	$('#dg').datagrid('clearSelections');
	easyExt.ajax({url:easyExt.url+"/member/getServices?id="+id,type:'GET'},function(data,status, xhr){
		$('#tg').treegrid("unselectAll");
		for(var i=0;j=data.length,i<j;i++){
			$('#tg').treegrid("select",data[i].columns.id);
			
		}
	});
}


function lookP3(id){
	member.id=id;
	easyExt.initDataGrid('#sg','/member/loadOpenService?mid='+id);
	$('#add3Dialog').dialog('open').dialog('setTitle','充值次数');
}

function lookP1(id){
	
	member.id=id;
	$("#memberid").val(id);

	$("#add2Dialog").dialog('open').dialog('setTitle','充值');;
}


function formatter(value, row, index){
	return '<a href="javascript:lookP('+row.id+')"><div class="icon-hamburg-lock" style="width:16px;height:16px" title="查看服务"></div></a>';
}

function formatter2(value,row,index)
{
  if(value=='1')	
     return '开启';
  if(value=='0')
    return '关闭';
  return '未知'
  
}

function formatter1(value, row, index){
	return '<a href="javascript:lookP1('+row.id+')"><div class="icon-hamburg-lock" style="width:16px;height:16px" title="按点数充值"></div>点数充值</a> ';
}


function formatter3(value, row, index){
	return '<a href="javascript:lookP3('+row.id+')"><div class="icon-hamburg-lock" style="width:16px;height:16px" title="次数充值"></div>次数充值</a> ';
}

function addMoney(){
	
	easyExt.ajax({url:easyExt.url+"/member/addMoney?"+$("#addForm2").serialize()},function(data, status, xhr){
		if(data){
			$.messager.alert("提示", "充值成功!", "info");
		}
	});
	
	
}


//开通服务
function saveSerice(){
	var p=$('#tg').treegrid("getSelections");
	var pIds="";
	for(var i=0;j=p.length,i<j;i++){
		pIds+=p[i].id+",";
	}
	easyExt.ajax({url:easyExt.url+"/member/saveMemberService?memberid="+member.id+"&privilegeIds="+pIds},function(data, status, xhr){
		if(data){
			$.messager.alert("提示", "开通服务成功！", "info");
		}
	});
}