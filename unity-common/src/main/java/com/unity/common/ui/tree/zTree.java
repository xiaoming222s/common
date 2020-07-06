package com.unity.common.ui.tree;

import com.unity.common.base.BaseEntity;
import com.unity.common.base.IBaseEntity;
import com.unity.common.util.ValueUtil;
import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class zTree<T extends IBaseEntity> {

	private static <T> void loadTreeNode(List<T> list, zTreeStructure structure, List<TNode> nodes, String parent, int lev) {
		// 查找所有子记录

		List<T> childs = new ArrayList<T>();
		
		if(parent==null) parent="";
		for(T o:list){
			IBaseEntity model = (IBaseEntity) o;
			//级次编码
			if(structure.getLevelField()!=null && !"".equals(structure.getLevelField())){
				String level=model.get(structure.getLevelField()) + "";
				String val = model.get(structure.getIdField()) + "";
				String pp=parent;
				if(!parent.equals("")) pp=parent+".";
				if(level.indexOf(".")>-1) level = String.valueOf(level.length()-level.replace(".", "").length());
				
				if(level.equals(String.valueOf(lev)) && val.startsWith(pp)){
					childs.add(o);
				}
			}
			//id, parentid
			else{
			    String val = "";
			    Object oo = model.get(structure.getParentField());
				val = oo==null ?"":oo+"";
	
				if (val.equals(parent + "")) {
					childs.add(o);
				}
			}
		}

		// 循环添加到节点
		childs.forEach(item -> {
		    BaseEntity model = (BaseEntity) item;
			TNode tn = TNode.newInstance()
				.id(model.get(structure.getIdField())+"")
//				.iconSkin("treeNodeChild")
				.text(model.get(structure.getTextField()) + "").build();
			if(structure.getKidField()!=null){
				tn.setAttr(new HashMap<String,Object>());
				for (String k:structure.getKidField().split(",")) {
					tn.getAttr().put(k, model.get(k));
				}
			}
			loadTreeNode(list, structure, tn.getChildren(), tn.getId(), lev + 1);

			if(tn.getChildren().size()>0){
				tn.setIsParent(true);
//				tn.setIconSkin("treeNodeParent");
			}
			else {
				tn.setIsParent(false);
			}

			// 父不可选中
			if (structure.getParentChkDisabled()) {
				if (tn.getChildren() != null && tn.getChildren().size() > 0) {
					tn.setChkDisabled(true);
				} else {
					tn.setChkDisabled(false);
				}
			}

			// 是否展开
			if (lev <= structure.getOpenLevel()) {
				//if (tn.children != null && tn.children.size() > 0)
					tn.setOpen(true);
			} else {
				tn.setOpen(false);
			}

			// 选中
			if (structure.getCheckedList() != null && structure.getCheckedList().size() > 0) {
				ValueUtil<Boolean> flag = new ValueUtil<Boolean>(false);
				structure.getCheckedList().forEach(id -> {
					if (id != null && id.equals(tn.getId())) {
						flag.set(true);
					}
				});

				if (flag.get()) {
					tn.setChecked(true);
				} else {
					tn.setChecked(false);
				}
			}

            if (structure.getChkDisabledList() != null && structure.getChkDisabledList().size() > 0) {
                ValueUtil<Boolean> flag = new ValueUtil<Boolean>(false);
                structure.getChkDisabledList().forEach(id -> {
                    if (id != null && id.equals(tn.getId())) {
                        flag.set(true);
                    }
                });

                if (flag.get()) {
                    tn.setChkDisabled(true);
                } else {
                    tn.setChkDisabled(false);
                }
            }

			nodes.add(tn);
			
		});
	}

	public static <T> List<TNode> getTree(List<T> list, zTreeStructure structure) {
		return getTree(list, structure, "");
	}

	public static <T> List<TNode> getTree(List<T> list, zTreeStructure structure,String topId,String topText) {
		
		return getTree(list, structure, new TNode(topId,topText));
	}
	
	public static <T> List<TNode> getTree(List<T> list, zTreeStructure structure,TNode topNode) {
		return getTree(list, structure, topNode.getId(),topNode);
	}
	public static <T> List<TNode> getTree(List<T> list, zTreeStructure structure, String parent,TNode topNode) {
		try {

			int level = 1;

			List<TNode> treeNodes = new ArrayList<TNode>();

			if(topNode==null) {
				loadTreeNode(list, structure, treeNodes, parent, level);
			}
			else {
				treeNodes.add(topNode);
				if(topNode.getChildren()==null){
					topNode.setChildren(Lists.newArrayList());
				}
				loadTreeNode(list, structure, topNode.getChildren(), parent, level);
			}

			return treeNodes;
		} catch (Exception ex) {
			return null;
		}
	}
	
	
	public static <T> List<TNode> getTree(List<T> list, zTreeStructure structure, String parent) {
		try {

			int level = 1;

			List<TNode> treeNodes = new ArrayList<TNode>();

			loadTreeNode(list, structure, treeNodes, parent, level);

			return treeNodes;
		} catch (Exception ex) {
			return null;
		}
	}
}
