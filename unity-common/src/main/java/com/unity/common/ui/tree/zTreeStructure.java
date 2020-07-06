package com.unity.common.ui.tree;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
public class zTreeStructure {
	/**
	 * 树节点Id字段
	 */
	private String idField;
	/**
	 * 树节点文本字段
	 */
	private String textField;
	/**
	 * 上级节点id字段
	 */
	private String parentField;
	/**
	 * 多返字段以逗号分隔
	 */
	private String kidField;
	/**
	 * 集次编码字段，如传入将以改编码为主导
	 */
	private String LevelField;

	/**
	 * 是否父不可选中
	 */
	@Builder.Default
	private Boolean parentChkDisabled = false;

	/**
	 * 展开深度
	 */
	@Builder.Default
	private int openLevel = 1;

	/**
	 * 默认选中的节点
	 */
	@Builder.Default
	private List<String> checkedList = new ArrayList<String>();

	/**不可选中节点*/
	@Builder.Default
	private List<String> chkDisabledList = new ArrayList<String>();

	public zTreeStructure(String idField, String textField, String parentField, Boolean ParentChkDisabled) {
		this.idField = idField;
		this.textField = textField;
		this.parentField = parentField;
		this.parentChkDisabled = ParentChkDisabled;
	}


	
	public zTreeStructure(String idField, String textField, String parentField, Boolean ParentChkDisabled, int OpenLevel) {
		this.idField = idField;
		this.textField = textField;
		this.parentField = parentField;
		this.parentChkDisabled = ParentChkDisabled;
		this.openLevel = OpenLevel;
	}
	
	public zTreeStructure(String idField, String textField, String parentField, String kidField, Boolean ParentChkDisabled) {
		this.idField = idField;
		this.textField = textField;
		this.parentField = parentField;
		this.parentChkDisabled = ParentChkDisabled;
		this.kidField = kidField;
	}
	

	public zTreeStructure(String idField, String textField , String kidField, Boolean ParentChkDisabled,String LevelField) {
		this.idField = idField;
		this.textField = textField;
		this.LevelField = LevelField;
		this.parentChkDisabled = ParentChkDisabled;
		this.kidField = kidField;
	}
	
	public zTreeStructure(String idField, String textField , String kidField, Boolean ParentChkDisabled,String LevelField, int OpenLevel) {
		this.idField = idField;
		this.textField = textField;
		this.LevelField = LevelField;
		this.parentChkDisabled = ParentChkDisabled;
		this.kidField = kidField;
		this.openLevel = OpenLevel;
	}
}