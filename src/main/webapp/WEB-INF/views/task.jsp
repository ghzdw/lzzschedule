<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="<%=request.getContextPath() %>/resources/bootstrap-3.3.5/css/bootstrap.min.css" rel="stylesheet"/>
<link href="<%=request.getContextPath() %>/resources/bootstrap-3.3.5/css/bootstrap-table.min.css" rel="stylesheet"/>
<link href="<%=request.getContextPath() %>/resources/css/comm.css" rel="stylesheet"/>
<script src="<%=request.getContextPath() %>/resources/js/jquery-1.10.2.js"></script>
<script src="<%=request.getContextPath() %>/resources/js/common.js"></script>
<script src="<%=request.getContextPath() %>/resources/bootstrap-3.3.5/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath() %>/resources/bootstrap-3.3.5/js/bootstrap-table.min.js"></script>
<script src="<%=request.getContextPath() %>/resources/bootstrap-3.3.5/js/bootstrap-table-zh-CN.min.js"></script>
<script src="<%=request.getContextPath() %>/resources/echarts/echarts.min.js"></script>
<script src="<%=request.getContextPath() %>/resources/js/d3.v3.min.js"></script>
<title>任务管理</title>
<style>
	.tag_contain{
		margin: 0 20px;
		min-height: 100%;
		background: #fff;
		box-shadow: 0 4px 16px 0 rgba(211,220,230,0.26);
	}
	.tag_contain .account_headbtn{
		padding: 10px 20px;
    border-bottom: 1px solid #ECF1F8;
	}
	.tag_contain .account_contain{
		padding: 20px;
	}
	.tag_contain table{
		border: 1px solid #ECF1F8;
	}
	.tag_contain table th{
		background: #E5E9F2;
		color: #6F7D95;
		font-weight: normal;
	}
	.tag_contain table td{
		color: #303852;
	}
	.fixed-table-container{
		border: none;
	}
	.pagination-detail{
		display: none;
	}
</style>
</head>
<body>
<div class="container-fluid">
   <!-- 新增模态框 -->
	<div class="modal fade" data-backdrop="static" data-keyboard="false" tabindex="-1" id="addModal">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" ><span>&times;</span></button>
	        <h4 class="modal-title">创建任务</h4>
	      </div>
	      <div class="modal-body">
	          <form class="form-horizontal" id="addForm">
				  <div class="form-group">
				    <label for="inputEmail3" class="col-sm-3 control-label">名称</label>
				    <div class="col-sm-6">
				      <input type="text" class="form-control input-sm" name="name" >
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="inputPassword3" class="col-sm-3 control-label">类型</label>
				    <div class="col-sm-6">
				       <select class="form-control input-sm" name="type">
						  <option value="shell">shell</option>
						  <option value="sql">sql</option>
					   </select>
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="inputPassword3" class="col-sm-3 control-label">cron表达式</label>
				    <div class="col-sm-6">
				      <input type="text" class="form-control input-sm" name="cron">
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="inputPassword3" class="col-sm-3 control-label">父级编号</label>
				    <div class="col-sm-6">
				      <input type="text" class="form-control input-sm" name="parentIds" >
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="inputPassword3" class="col-sm-3 control-label">描述</label>
				    <div class="col-sm-6">
				      <input type="text" class="form-control input-sm" name="desc" >
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="col-sm-offset-3 col-sm-6">
				      <input type="button" class="btn btn-default" style="margin-left:40px" data-dismiss="modal"  value="取消">
				      <input type="button" id="btnAdd" class="btn btn-primary" style="margin-left:40px" value="确认">
				    </div>
				  </div>
				</form>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->		
	<!-- 编辑账户模态框 -->
	<div class="modal fade" data-backdrop="static" data-keyboard="false" tabindex="-1" id="editModal">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" ><span>&times;</span></button>
	        <h4 class="modal-title">编辑任务</h4>
	      </div>
	      <div class="modal-body">
	          <form class="form-horizontal" id="editForm">
	            <div class="form-group">
				    <label for="inputEmail3" class="col-sm-3 control-label">任务编号</label>
				    <div class="col-sm-6">
				      <input type="text" class="form-control input-sm" name="id" >
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="inputEmail3" class="col-sm-3 control-label">任务名称</label>
				    <div class="col-sm-6">
				      <input type="text" class="form-control input-sm" name="name" >
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="inputPassword3" class="col-sm-3 control-label">描述</label>
				    <div class="col-sm-6">
				      <input type="text" class="form-control input-sm" name="desc" >
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="inputPassword3" class="col-sm-3 control-label">类型</label>
				    <div class="col-sm-6">
				       <select class="form-control input-sm" name="type">
						  <option>生效</option>
						  <option>失效</option>
					   </select>
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="inputPassword3" class="col-sm-3 control-label">cron表达式</label>
				    <div class="col-sm-6">
				      <input type="text" class="form-control input-sm" name="cron" >
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="inputPassword3" class="col-sm-3 control-label">父级编号</label>
				    <div class="col-sm-6">
				      <input type="text" class="form-control input-sm" name="parentIds" >
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="inputPassword3" class="col-sm-3 control-label">责任人</label>
				    <div class="col-sm-6">
				      <input type="text" class="form-control input-sm" name="creator" >
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="inputPassword3" class="col-sm-3 control-label">最后修改人</label>
				    <div class="col-sm-6">
				      <input type="text" class="form-control input-sm" name="modifier" >
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="inputPassword3" class="col-sm-3 control-label">创建时间</label>
				    <div class="col-sm-6">
				      <input type="text" class="form-control input-sm" name="createTime" >
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="inputPassword3" class="col-sm-3 control-label">修改时间</label>
				    <div class="col-sm-6">
				      <input type="text" class="form-control input-sm" name="modifyTime" >
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="col-sm-offset-3 col-sm-6">
				      <input type="button" class="btn btn-default" style="margin-left:40px" data-dismiss="modal"  value="取消">
				      <input type="button" id="btnEdit" class="btn btn-primary" style="margin-left:40px" value="确认">
				    </div>
				  </div>
				</form>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->		
    <!-- 提示模态框 -->
	<div class="modal fade" tabindex="-1" id="tipModal">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" ><span>&times;</span></button>
	        <h4 class="modal-title">提示</h4>
	      </div>
	      <div class="modal-body">
	        <p id="tip"></p>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
	<div class="row" style="height: 100%">
		<jsp:include page="left.jsp"></jsp:include>
		<section id="cont" class="">
			<jsp:include page="head.jsp"></jsp:include>
			<div class="tag_contain">
				<section class="tagsection">
					<div class="account_headbtn">
						<a id="btn_add" class="btn btn-default" href="javascript:void(0)">创建任务</a>
						<a id="btn_edit" class="btn btn-default" href="javascript:void(0)">编辑任务</a>
						<a id="join" class="btn btn-default" href="javascript:void(0)">加入任务调度</a>
						<a id="remove" class="btn btn-default" href="javascript:void(0)">移除任务调度</a>
					</div>
					<div class="account_contain">
						<table id="table"></table>
					</div>
				</section>
			</div>
		</section>
	</div>
</div>
<script>
//添加任务调度
$("#join").click(function(){
	var selections=$("#table").bootstrapTable('getSelections');
	if(selections.length==0){
		showTipModal("至少选择一行!"); 
	}else{
		var idArr=[];
		$.each(selections,function(i,obj){
			idArr.push(obj["id"]);
		});
		var taskIds=idArr.join(",");
		$.ajax({
			type:"post",
			url:"taskJoinInSchedule",
		    data:{"taskIds":taskIds},
		    success:function(data){
		    	
		    }
		});
	}
});
//移除任务调度
$("#remove").click(function(){
	var selections=$("#table").bootstrapTable('getSelections');
	if(selections.length==0){
		showTipModal("至少选择一行!"); 
	}else{
		var idArr=[];
		$.each(selections,function(i,obj){
			idArr.push(obj["id"]);
		});
		var taskIds=idArr.join(",");
		$.ajax({
			type:"post",
			url:"taskRemoveFromSchedule",
		    data:{"taskIds":taskIds},
		    success:function(data){
		    	
		    }
		});
	}
});
//添加任务
$("#btnAdd").click(function(){
	$.ajax({
		type:"post",
		url:"addTask",
	    data:str2Json("#addForm"),
	    success:function(data){
	    	if("success"==data.status){
	    		$("#addModal").modal("hide");
	    		showTipModal("任务添加成功"); 
			}else{
	    		showTipModal("任务添加失败"); 
			}
	    }
	});
});
//修改任务
$("#btnEdit").click(function(){
	$.ajax({
		type:"post",
		url:"updateTask",
	    data:str2Json("#editForm"),
	    success:function(data){
	    	if("success"==data.status){
	    		$("#editModal").modal("hide");
	    		showTipModal("任务修改成功!"); 
			}else{
	    		showTipModal("任务修改失败!"); 
			}
	    }
	});
});
//显示添加任务对话框
$("#btn_add").click(function(){
	$("#addModal").modal("show");
});
//选中一行进行编辑
$("#btn_edit").click(function(){
	var selections=$("#table").bootstrapTable('getSelections');
	if(selections.length!=1){
		showTipModal("只能选择一行进行编辑"); 
	}else{
		var selected=selections[0];
		$.ajax({
			type:"post",
			url:"queryTaskById",
		    data:{"taskId":selected["id"]},
		    success:function(data){
		    	if("success"==data.status){
		    		var one=data.data;
		    		$.each(one,function(k,v){
		    			$("#editForm").find("input[name="+k+"]").val(v);
		    		});
		    		$("#editModal").modal("show");
				}else{
		    		showTipModal("查询任务失败"); 
				}
		    }
		});
	}
});
//任务表格
$('#table').bt({ 
    url: 'getTasks',
    columns: [{
		title: '全选',
		field: 'ck',
		checkbox: true,
		width: 10,
		align: 'center',
	},{
        field: 'id',
		title: '任务编号',
		align: 'center',
        sortable:true
    },{
        field: 'name',
		title: '任务名称',
		align: 'center',
        sortable:true
    },{
        field: 'desc',
		title: '描述',
		align: 'center',
        sortable:true
    },{
        field: 'type',
		title: '类型',
		align: 'center',
        sortable:true
    },{
        field: 'cron',
		title: '执行表达式',
		align: 'center',
        sortable:true
    },{
        field: 'parentIds',
		title: '父级编号',
		align: 'center',
        sortable:true
    },{
        field: 'creator',
		title: '责任人',
		align: 'center',
        sortable:true
    },{
        field: 'createTime',
		title: '创建时间',
		align: 'center',
        sortable:true
    },{
        field: 'xx',
		title: '操作',
		align: 'center',
        formatter:function(v,row,i){
           return "<a class='a_editTag' href='javascript:void(0)'>编辑</a>";
        }
    }]
});
</script>
</body>
</html>