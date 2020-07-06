package com.unity.common.ui.tree;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder(builderMethodName = "newInstance")
@NoArgsConstructor
@AllArgsConstructor
public class TNode {
	/**
	 * 节点id
	 */
	private String id;
	/**
	 * 节点文本
	 */
	private String text;


	/**
	 * 是否选中
	 */
	private Boolean checked;
	/**
	 * 子节点列表
	 */
	@Builder.Default
	private List<TNode> children = new ArrayList<TNode>();

	/**
	 * 是否父节点
	 */
	private Boolean isParent;
	/**
	 * 是否可被选中
	 */
	private Boolean chkDisabled;
	/**
	 * 是否展开
	 */
	private Boolean open;

	private String iconSkin;

	/**
	 * 多返的数据
	 */
	private Map<String,Object> attr;

//	public String state;
//	public Boolean halfCheck;
//	public String name;
//	public String target;
//	public String url;
//
//
//	public String icon;
//	public String iconClose;
//	public String iconOpen;
//	public String iconSkin;



	public  TNode(String id,String Text){
		this.id=id;
		this.text=Text;
	}
	public TNode(String id, String text,Map<String,Object> attr) {
		super();
		this.id = id;
		this.text = text;
		this.attr = attr;
	}
	
	public TNode(String id, String text,Boolean open, Map<String,Object> attr) {
		super();
		this.id = id;
		this.text = text;
		this.attr = attr;
		this.open= open;
	}
}
