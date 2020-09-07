/*
* Copyright (c) 2016,2021 云南空港航空食品有限公司. All rights reserved.
* create time Wed Oct 12 15:45:08 CST 2016
*
*/
package fuku.automybatis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fuku.automybatis.BusinessViewParamTool;

public class BusinessModelAO extends BusinessModel implements Serializable {
    private static final long serialVersionUID = -1358680496512503959L;
    
    private List<BusinessPojoAO> pojoList;
    
    private List<BusinessViewAO> viewList;
    private List<BusinessViewAO> addViewList;
    private List<BusinessViewAO> editViewList;
    private List<BusinessViewAO> approveViewList;
    private List<BusinessViewAO> viewViewList;
    
    private List<BusinessValidationAO> validationList;
    private List<BusinessValidationAO> addValidationList;
    private List<BusinessValidationAO> editValidationList;
    private List<BusinessValidationAO> approveValidationList;
    private List<BusinessValidationAO> viewValidationList;
    
    
	public List<BusinessValidationAO> getValidationList() {
		return validationList;
	}

	public void setValidationList(List<BusinessValidationAO> validationList) {
		this.validationList = validationList;
		if(validationList != null) {
			for(BusinessValidationAO validation : validationList) {
				switch (validation.getViewType()) {
				case BusinessValidationAO.VIEW_TYPE_ADD:
					if(this.addValidationList == null) {
						addValidationList = new ArrayList<>();
					}
					addValidationList.add(validation);
					break;
				case BusinessValidationAO.VIEW_TYPE_EDIT:
					if(this.editValidationList == null) {
						editValidationList = new ArrayList<>();
					}
					editValidationList.add(validation);
					break;
				case BusinessValidationAO.VIEW_TYPE_AUDIT:
					if(this.approveValidationList == null) {
						approveValidationList = new ArrayList<>();
					}
					approveValidationList.add(validation);
					break;
				case BusinessValidationAO.VIEW_TYPE_VIEW:
					if(this.viewValidationList == null) {
						viewValidationList = new ArrayList<>();
					}
					viewValidationList.add(validation);
					break;
				case BusinessValidationAO.VIEW_TYPE_ALL:
					if(this.addValidationList == null) {
						addValidationList = new ArrayList<>();
					}
					addValidationList.add(validation);
					if(this.editValidationList == null) {
						editValidationList = new ArrayList<>();
					}
					editValidationList.add(validation);
					if(this.approveValidationList == null) {
						approveValidationList = new ArrayList<>();
					}
					approveValidationList.add(validation);
					if(this.viewValidationList == null) {
						viewValidationList = new ArrayList<>();
					}
					viewValidationList.add(validation);
					break;

				default:
					break;
				}
			}
		}
	}

	public List<BusinessPojoAO> getPojoList() {
		return pojoList;
	}

	public void setPojoList(List<BusinessPojoAO> pojoList) {
		this.pojoList = pojoList;
	}

	public List<BusinessViewAO> getViewList() {
		return viewList;
	}

	public void setViewList(List<BusinessViewAO> viewList) {
		this.viewList = viewList;
		if(viewList != null) {
			for(BusinessViewAO view : viewList) {
				switch (view.getViewType()) {
				case BusinessViewAO.VIEW_TYPE_ADD:
					if(this.addViewList == null) {
						addViewList = new ArrayList<>();
					}
					addViewList.add(view);
					break;
				case BusinessViewAO.VIEW_TYPE_EDIT:
					if(this.editViewList == null) {
						editViewList = new ArrayList<>();
					}
					editViewList.add(view);
					break;
				case BusinessViewAO.VIEW_TYPE_APPROVE:
					if(this.approveViewList == null) {
						approveViewList = new ArrayList<>();
					}
					approveViewList.add(view);
					break;
				case BusinessViewAO.VIEW_TYPE_VIEW:
					if(this.viewViewList == null) {
						viewViewList = new ArrayList<>();
					}
					viewViewList.add(view);
					break;

				default:
					break;
				}
			}
		}
	}

	public List<BusinessViewAO> getAddViewList() {
		return addViewList;
	}
	
	public String getAddViewDicIds() {
		if(addViewList == null || addViewList.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(BusinessViewAO view : this.addViewList) {
			if(view.getParam() != null && !view.getParam().isEmpty()) {
				List<String> dicIds = getDicId(view.getParam());
				if(dicIds != null && dicIds.size() > 0) {
					for(String dicId : dicIds) {
						sb.append("\"").append(dicId).append("\",");
					}
				}
			}
		}
		if(sb.length() > 0) {
			return sb.substring(0,sb.length()-1);
		} else {
			return sb.toString();
		}
	}
	
	public String getEditViewDicIds() {
		if(editViewList == null || editViewList.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(BusinessViewAO view : this.editViewList) {
			if(view.getParam() != null && !view.getParam().isEmpty()) {
				List<String> dicIds = getDicId(view.getParam());
				if(dicIds != null && dicIds.size() > 0) {
					for(String dicId : dicIds) {
						sb.append("\"").append(dicId).append("\",");
					}
				}
			}
		}
		if(sb.length() > 0) {
			return sb.substring(0,sb.length()-1);
		} else {
			return sb.toString();
		}
	}
	
	public String getApproveViewDicIds() {
		if(approveViewList == null || approveViewList.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(BusinessViewAO view : this.approveViewList) {
			if(view.getParam() != null && !view.getParam().isEmpty()) {
				List<String> dicIds = getDicId(view.getParam());
				if(dicIds != null && dicIds.size() > 0) {
					for(String dicId : dicIds) {
						sb.append("\"").append(dicId).append("\",");
					}
				}
			}
		}
		if(sb.length() > 0) {
			return sb.substring(0,sb.length()-1);
		} else {
			return sb.toString();
		}
	}
	
	public String getViewViewDicIds() {
		if(viewViewList == null || viewViewList.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(BusinessViewAO view : this.viewViewList) {
			if(view.getParam() != null && !view.getParam().isEmpty()) {
				List<String> dicIds = getDicId(view.getParam());
				if(dicIds != null && dicIds.size() > 0) {
					for(String dicId : dicIds) {
						sb.append("\"").append(dicId).append("\",");
					}
				}
			}
		}
		if(sb.length() > 0) {
			return sb.substring(0,sb.length()-1);
		} else {
			return sb.toString();
		}
	}

	private List<String> getDicId(String param) {
		if(param == null || param.isEmpty()) {
			return null;
		}
		List<String> ret = BusinessViewParamTool.parseDics(param);
		return ret;
	}
	
	public void setAddViewList(List<BusinessViewAO> addViewList) {
		this.addViewList = addViewList;
	}

	public List<BusinessViewAO> getEditViewList() {
		return editViewList;
	}

	public void setEditViewList(List<BusinessViewAO> editViewList) {
		this.editViewList = editViewList;
	}
 
	public List<BusinessViewAO> getApproveViewList() {
		return approveViewList;
	}

	public void setApproveViewList(List<BusinessViewAO> approveViewList) {
		this.approveViewList = approveViewList;
	}

	public List<BusinessViewAO> getViewViewList() {
		return viewViewList;
	}

	public void setViewViewList(List<BusinessViewAO> viewViewList) {
		this.viewViewList = viewViewList;
	}

	public List<BusinessValidationAO> getAddValidationList() {
		return addValidationList;
	}

	public void setAddValidationList(List<BusinessValidationAO> addValidationList) {
		this.addValidationList = addValidationList;
	}

	public List<BusinessValidationAO> getEditValidationList() {
		return editValidationList;
	}

	public void setEditValidationList(List<BusinessValidationAO> editValidationList) {
		this.editValidationList = editValidationList;
	}

	public List<BusinessValidationAO> getApproveValidationList() {
		return approveValidationList;
	}

	public void setApproveValidationList(List<BusinessValidationAO> approveValidationList) {
		this.approveValidationList = approveValidationList;
	}

	public List<BusinessValidationAO> getViewValidationList() {
		return viewValidationList;
	}

	public void setViewValidationList(List<BusinessValidationAO> viewValidationList) {
		this.viewValidationList = viewValidationList;
	}
	
}