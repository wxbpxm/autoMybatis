${'<#include "/component/rightcheck.ftl">'}
${'<#include "/component/forbiddenshow.ftl">'}

  	${'<#include "/common/base.ftl">'}
  	 <#if needImgUpload?? && needImgUpload>
  	 ${'<@addCSS path=["assets/dropzone/css/dropzone"]/>'}
	 ${'<@addCSS path=["css/jquery.Jcrop"]/>'}
    </#if>

	${'<@base title="'}${r'${(sysProperties.config.sysname)!}'}${'管理系统">'}
	<#if needImgUpload?? && needImgUpload>
	<style>
		.image_load{
			display:none;  
			height:10px;  
			width:80px;  
		}  
		/* 下面是jcrop图片裁剪部分样式 */
		.jcrop-holder #preview-pane {
		  display: block;
		  position: absolute;
		  z-index: 2000;
		  top: 0px;
		  right: -320px;
		  padding: 6px;
		  border: 1px rgba(0,0,0,.4) solid;
		  background-color: white;
		
		  -webkit-border-radius: 6px;
		  -moz-border-radius: 6px;
		  border-radius: 6px;
		
		  -webkit-box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);
		  -moz-box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);
		  box-shadow: 1px 1px 5px 2px rgba(0, 0, 0, 0.2);
		}
		/* The Javascript code will set the aspect ratio of the crop
		   area based on the size of the thumbnail preview,
		   specified here */
		#preview-pane .preview-container {
		  width: 380px;
		  height: 260px;
		  overflow: hidden;
		}
	</style>
	</#if>
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
							<li><a href="${r'${(sysProperties.config.adminServerPath)!}'}/${moduleName!}">${pageName!}配置</a><span class="divider">&nbsp;</span></li>
							<li><a href="#">${pageName!}详情</a><span class="divider-last">&nbsp;</span></li>
						</ul>
						<!-- END PAGE TITLE & BREADCRUMB-->
					</div>
				</div>
				<!-- END PAGE HEADER-->
				<!-- BEGIN PAGE CONTENT-->
				<div id="page" class="dashboard">
                    <div class="row-fluid">
                        <div class="span12">
                            <!-- BEGIN widget-->   
			                  <div class="widget"> 
			                     <div class="widget-body form">
			                        <!-- BEGIN FORM-->
			                        <form id="form" class="form-horizontal" method="POST">
			                           <input type="hidden" name="id" value="${r'${(detail.id)!}'}">
	                           <#if fields?? && fields?size gt 0>
                           		<#list fields as field>
	                                 <#if field.htmlType?? && field.htmlType == "数值">
		                                 <div class="control-group">
				                              <label class="control-label">${(field.label)!}</label>
				                              <div class="controls">
				                                <input name="${(field.name)!}" value="${r'${(detail.'}${(field.name)!}${r')!}'}" type="text" class="span6  popovers"
				                                 onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" />
				                              </div>
			                              </div>
		                             <#elseif field.htmlType?? && field.htmlType == "下拉框">
		                                  <#if field.name == "status">
		                                   <div class="control-group">
				                              <label class="control-label">${(field.label)!}</label>
				                              <div class="controls">
				                                 <select name="${(field.name)!}"  value="${r'${(detail.'}${(field.name)!}${')!}'}" class="span6 chosen">
				                                 	<#if field.options??>
				                                    <#list field.options as option>
				                                    <#if (option.key)?? && option.key != "删除">
				                                 	<option value="${(option.value)!}" ${'<#if (detail.status)?? && detail.status == "'}${(option.value)!}${'">selected</#if>'}>${(option.key)!}</option>
													</#if>
				                                    </#list>
				                                 	</#if>
				                                 </select>
				                              </div>
				                           </div>
		                                  <#else>
		                                   <div class="control-group">
				                              <label class="control-label">${(field.label)!}</label>
				                              <div class="controls">
				                                 <select name="${(field.name)!}"  value="${r'${(detail.'}${(field.name)!}${')!}'}" class="span6 chosen">
			                                     ${'<#if '}${(field.name)!}list${'??>'}
			                                     ${'<#list '}${(field.name)!}list as ${(field.name)!}${'>'}
							     					<option ${'<#if (detail.'}${(field.name)!}${')?? && detail.'}${(field.name)!}${' = ${(field.name)!}.id>selected</#if>'} value="${r'${('}${(field.name)!}.id${')}'}">${r'${('}${(field.name)!}${'.name)!}'}</option>						                                        
			                                     ${'</#list>'}
			                                     ${'</#if>'}
				                                 </select>
				                              </div>
				                           </div>
		                                  </#if>
		                             <#elseif field.htmlType?? && field.htmlType == "图片">
		                                  <div class="control-group">
				                              <label class="control-label">${(field.label)!}</label>
				                              <div class="controls">
				                                 <div id="main-photo" style="cursor:pointer">点我上传照片
					                                 <div><img id="main_load" class="image_load" src="${r'${(sysProperties.config.staticServerPath)!}'}/img/preview.gif" alt="Fancy pants"></div>
				                                     <div id="main-img">
						                                 ${'<#if detail?? && detail.'}${(field.name)!}${r'??>'}
						                                    <img style="width:300px;" src="${r'${(sysProperties.config.uploaddir)!}'}${r'${(detail.'}${(field.name)!}${r')!}'}"/>
						                                 ${'</#if>'}
					                             	 </div>
					                             	 <input type="hidden" name="${(field.name)!}Id" class="fileid" value="${r'${(detail.'}${(field.name)!}${r'Id)!}'}" >
				                                	 <input type="hidden" name="${(field.name)!}" class="filepath" value="${r'${(detail.'}${(field.name)!}${r')!}'}">
				                                 </div>
				                              </div>
				                           </div>
				                           <div class="control-group">
				                              <label class="control-label">待裁剪</label>
				                              <div class="controls" id="cutImg">
				                              </div>
				                           </div>
		                             <#else>
		                             	  <div class="control-group">
				                              <label class="control-label">${(field.label)!}</label>
				                              <div class="controls">
				                                <input name="${(field.name)!}" value="${r'${(detail.'}${(field.name)!}${r')!}'}" type="text" class="span6  popovers" />
				                              </div>
			                              </div>
	                                 </#if>
                           		</#list>
	                           </#if>
			                           <div class="control-group">
			                              <div class="controls">
			                                 <input type="button" id="j-save" class="btn btn-success" value="保存">
			                              </div>
			                           </div>
			                        </form>
			                        <!-- END FORM-->  
			                     </div>
			                  </div>
		                  <!-- END widget-->
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
      	<!-- BEGIN JAVASCRIPTS -->
        <!-- Load javascripts at bottom, this will reduce page load time -->
        ${'<#include "/common/script.ftl">'}
        ${'<#include "/common/formValidateScript.ftl">'}
        <#if needImgUpload?? && needImgUpload>
        <script src="${r'${(sysProperties.config.staticServerPath)!}'}/assets/dropzone/dropzone.js"></script>
        <script src="${r'${(sysProperties.config.staticServerPath)!}'}/js/jquery.Jcrop.min.js"></script>
        </#if>
        <script>
            <#if needImgUpload?? && needImgUpload>
             var imgWidth = 380, imgHeight = 260, imgRealWidth = 1140, imgRealHeight = 780;
            </#if>
            jQuery(document).ready(function() {
                App.setMainPage(false);
                App.init();

				$("#form").validate({
			        rules: {
			       	 <#list fields as field>
           		     <#if !field.canBeNull>
           		     ${(field.name)!}: {
		            	required: true
		            }<#if field_index < (fields?size-1)>,</#if> 
		            </#if>
               		</#list>
			        },
			        messages: {
			        <#list fields as field>
           		    <#if !field.canBeNull>
           		     ${(field.name)!}: {
			                required: "请填写${(field.label)!}"
			         }<#if field_index < (fields?size-1)>,</#if> 
		            </#if>
               		</#list>
			        }
			    });
			    
                  $("#j-save").click(function(){
               		   if(!$('#form').valid()) {
				    		alert("您填写的信息有误或不全，请按提示填写正确")
				    		return;
				       }
                      <#if needImgUpload?? && needImgUpload>
                       if($(".main-img img").length <1) {
                      	 	alert("请先裁剪图片，再保存！");
                           return;
                       }
                       </#if>
                       
                       var param = $("#form").serialize();
                       	$.ajax({
			                url: '${r'${(sysProperties.config.adminServerPath)!}'}/${moduleName!}/save',
			                type: 'POST',
			                data: param
			            }).done(function(res) {
				           	 if(res.success) {
				           	     alert("保存成功");
				           		 window.location.href = "${r'${(sysProperties.config.adminServerPath)!}'}/${moduleName!}";
				           	 } else {
				           		 alert(res.msg);
				           	 }
			            });
                       
                  });
                  <#if needImgUpload?? && needImgUpload>
                  initFileUpload('main-photo');
                  </#if>
            });
        </script>
        <!-- END JAVASCRIPTS -->
${'</@rightCheck>'}
${'</@base>'}
${'<@forbiddenshow>'}
对不起,您没有访问权限
${'</@forbiddenshow>'}