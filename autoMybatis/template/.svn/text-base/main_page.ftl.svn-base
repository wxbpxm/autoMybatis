${'<#include "/component/rightcheck.ftl">'}
${'<#include "/component/forbiddenshow.ftl">'}

 	${'<#include "/common/base.ftl">'}
 	${'<#include "/component/pagination.ftl">'}
	
	${'<@base title="'}${r'${(sysProperties.config.sysname)!}'}${'管理系统">'}
	
	${'<@sidebar topmenu="'}${moduleName!}_menu" submenu="${moduleName!}_sub">${'</@sidebar>'}
	    ${'<@rightCheck>'}
		<!-- BEGIN PAGE -->
		<div id="main-content">
			<!-- BEGIN PAGE CONTAINER-->
			<div class="container-fluid">
				<!-- BEGIN PAGE HEADER-->
				<div class="row-fluid">
					<div class="span12">
						<!-- BEGIN PAGE TITLE & BREADCRUMB-->
						<ul class="breadcrumb">
							<li>
                                <a href="#"><i class="icon-home"></i></a><span class="divider">&nbsp;</span>
							</li>
                            <li>
                                <a href="#">${pageName!}管理</a> <span class="divider">&nbsp;</span>
                            </li>
							<li><a href="#">${pageName!}配置</a><span class="divider-last">&nbsp;</span></li>
						</ul>
						<!-- END PAGE TITLE & BREADCRUMB-->
					</div>
				</div>
				<!-- END PAGE HEADER-->
				<!-- BEGIN PAGE CONTENT-->
				<div id="page" class="dashboard">
                    <div class="row-fluid">
                        <div class="span12">
                            <!-- BEGIN EXAMPLE TABLE widget-->
                            <div class="widget">
                                <div class="widget-body">
                                	${'<@pageTopButtons />'}
	                                 <#if searchFields??>
	                                 <form id="search-form" action= "${r'${(sysProperties.config.adminServerPath)!}'}/${moduleName!}" class="from-search form-horizontal">
                                		<div class="span12 search-area">
                                			<#list searchFields as searchField>
	                                			<label class="search-label3">${(searchField.label)!}:</label>
	                                			<#if searchField.htmlType = "文本">
	                                				<#if searchField.name = "name" || searchField.name = "title">
												<input name="keyword" style="width:120px;" value="${r'${(search.keyword)!}'}" type="text"    />
		                                			<#else>
		                                	    <input name="${(searchField.name)!}" style="width:120px;" value="${r'${(search.'}${(searchField.name)!}${')!}'}" type="text"    />
		                                			</#if>
		                                		<#elseif searchField.htmlType = "下拉框">
		                                			<#if searchField.name = "status">
				                                  <select name="${(searchField.name)!}"  value="${r'${(search.'}${(searchField.name)!}${')!}'}" class="span1 chosen">
				                                    <option value="">全部</option>
				                                 	<#if searchField.options??>
				                                    <#list searchField.options as option>
				                                    	<#if (option.key)?? && option.key != "删除">
				                                 	<option value="${(option.value)!}" ${'<#if (search.status)?? && search.status == "'}${(option.value)!}${'">selected</#if>'}>${(option.key)!}</option>
				                                    	</#if>
				                                    </#list>
				                                 	</#if>	
				                                  </select>
			                                		<#else>
		                                		<select name="${(searchField.name)!}"  value="${r'${(detail.'}${(searchField.name)!}${')!}'}" class="span2 chosen">
			                                     ${'<#if '}${(searchField.name)!}list${'??>'}
			                                     ${'<#list '}${(searchField.name)!}list as ${(searchField.name)!}${'>'}
							     					<option ${'<#if (search.'}${(searchField.name)!}${')?? && search.'}${(searchField.name)!}${' = ${(searchField.name)!}.id>selected</#if>'} value="${r'${('}${(searchField.name)!}.id${')}'}">${r'${('}${(searchField.name)!}${'.name)!}'}</option>						                                        
			                                     ${'</#list>'}
			                                     ${'</#if>'}
				                                </select>
			                                		</#if>
	                                			</#if>
                                			</#list>
											<input type="submit" class="btn btn-success" value="搜索">
											<input type="button" id="search-clear" class="btn btn-danger" value="清空">
										</div>
									</form>
									 </#if>
                                    <table class="table  table-bordered  table-hover" id="${moduleName!}Table">
                                        <thead>
                                        <tr>
                                        	<#list fields as field>
	                                        <th width="">${(field.label)!}</th>
                                        	</#list>
                                        	${'<#if currentListButtons?? && currentListButtons?size gt 0>'}
                                            <th>操作</th>
										    ${'</#if>'}
                                        </tr>
                                        </thead>
                                        <tbody>
	                                    ${'<#if datalist??>'}
	                                    ${'<#list datalist as ${moduleName!}>'}
	                                    <tr class="odd gradeX">
	                                    	<#list fields as field>
	                                    	<#if field.htmlType == '文本'>
	                                    	<td><#if field.needLink?? && field.needLink><a href="${r'${(sysProperties.config.adminServerPath)!}'}/${moduleName!}/detail?id=${r'${('}${moduleName!}${'.id)!}'}"></#if>${r'${('}${moduleName!}.${(field.name)!})!${'}'}<#if field.needLink?? && field.needLink></a></#if></td>
											<#elseif field.htmlType == '数值'>
											<td>${r'${('}${moduleName!}.${(field.name)!}?c${')!}'}</td>
	                                    	<#elseif field.htmlType == '图片'>
	                                    	<td><#if field.needLink?? && field.needLink><a href="${r'${(sysProperties.config.adminServerPath)!}'}/${moduleName!}/detail?id=${r'${('}${moduleName!}${'.id)!}'}"></#if><img src="${r'${(sysProperties.config.uploaddir)!}'}${r'${('}${moduleName!}.${(field.name)!}${')!}'}"/><#if field.needLink?? && field.needLink></a></#if></td>
	                                    	<#elseif field.htmlType == '日期'> 
	                                    	<td>${r'${('}${moduleName!}.${(field.name)!}${'?string("yyyy-MM-dd"))!}'}</td>
	                                    	<#elseif field.htmlType == '时间'>
	                                    	<td>${r'${('}${moduleName!}.${(field.name)!}${'?string("yyyy-MM-dd HH:mm"))!}'}</td>
	                                    	<#elseif field.htmlType == '下拉框'>
	                                    	<td><#if field.needLink?? && field.needLink><a href="${r'${(sysProperties.config.adminServerPath)!}'}/${moduleName!}/detail?id=${r'${('}${moduleName!}${'.id)!}'}"></#if>${r'${('}${moduleName!}.${(field.name)!}Name${')!}'}<#if field.needLink?? && field.needLink></a></#if></td>
	                                    	</#if>
                                        	</#list>
                                        	${'<#if currentListButtons?? && currentListButtons?size gt 0>'}
                                        	<td>
										    ${'<#list currentListButtons as button>'}
										    <button onClick="${'${('}button.btnScript)!${'}'}" data-id="${r'${('}${moduleName!}.id${')!}'}" class="${'${('}button.btnClass)!${'}'}">${'${('}button.btnName)!${'}'}</button>
										    ${'</#list>'}
										    </td>
		                                    ${'</#if>'}
	                                    </tr>
	                                    ${'</#list>'}
	                                    ${'</#if>'}
	                                    </tbody>
	                                    </table>
                                    ${'<@pagination domid="pagination" pg=page/>'}
                                </div>
                            </div>
                            <!-- END EXAMPLE TABLE widget-->
                        </div>
                    </div>
				</div>
				<!-- END PAGE CONTENT-->
			</div>
			<!-- END PAGE CONTAINER-->
		</div>
		<!-- END PAGE -->
	    </div>
		<!-- END CONTAINER -->
		${'<#include "/common/script.ftl">'}
		<script type="text/javascript" src="${r'${(sysProperties.config.staticServerPath)!}'}/js/component/pagination.js"></script>
		<script>
		
		    //新增${pageName!}按钮
			var add${moduleNameCal!} = function() {
			 	window.location.href = "${r'${(sysProperties.config.adminServerPath)!}'}/${moduleName!}/detail";
		    };
		    
			//修改${pageName!}按钮
			var edit${moduleNameCal!} = function(btn) {
				window.location.href = "${r'${(sysProperties.config.adminServerPath)!}'}/${moduleName!}/detail?id=" + $(btn).data("id");
		    };
		    
			//删除${pageName!}按钮
			var delete${moduleNameCal!} = function(btn) {
				if(!confirm("请慎重，您确定要删除吗?")){
                    return;
                }
				var id = $(btn).data("id");
        		 $.ajax({
	                url: '${r'${(sysProperties.config.adminServerPath)!}'}/${moduleName!}/delete',
	                data: {id:id},
	                type: 'POST'
	            }).done(function(res) {
	           	 if(res) {
	           	 	alert("成功移除！");
	           		 window.location.href = "${r'${(sysProperties.config.adminServerPath)!}'}/${moduleName!}";
	           	 } else {
	           		 alert(res.msg);
	           	 }
	          });   
		    };
		    
		    
            jQuery(document).ready(function() {
                // initiate layout and plugins
                App.setMainPage(false);
                App.init();
                
                jQuery('#search-clear').click(function () {
                 	$('#search-form input[name="keyword"]').val("");
                 });
                 
                 
                $("#pagination").pagination({
                    getUrl: function(){
                    	return "${r'${(sysProperties.config.adminServerPath)!}'}/${moduleName!}";
                    }
                });
            });
        </script>
        ${'</@rightCheck>'}
        ${'<@forbiddenshow>'}
        <div id="main-content">
        <p>
欢迎使用${r'${(sysProperties.config.sysname)!}'}后台管理系统
        </p>
        </div>
		${'</@forbiddenshow>'}
${'</@base>'}
