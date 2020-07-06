package com.unity.common.ui.tree;

import com.unity.common.base.IBaseEntity;
import com.unity.common.util.ValueUtil;

import java.util.ArrayList;
import java.util.List;

public class zTrees<T extends IBaseEntity,J extends IBaseEntity> {
    private static <T, J> void loadTreeNode(List<T> list, List<J> Jlist, zTreeStructure Tstructure, zTreeStructure Jstructure, List<TNode> nodes, String parent, int lev) {
        // 查找所有子记录

        List<T> childs = new ArrayList<T>();
        // parent+="";

        list.forEach(o -> {
            IBaseEntity model = (IBaseEntity) o;
            String val = model.get(Jstructure.getParentField()) + "";

            if (val.equals(parent + "")) {
                childs.add(o);
            }
        });

        // 循环添加到节点
        childs.forEach(item -> {
            IBaseEntity model = (IBaseEntity) item;
            TNode tn = TNode.newInstance()
                    .id(model.get(Jstructure.getIdField())+"")
                    .text(model.get(Jstructure.getTextField()) + "").build();
            loadTreeNode(list, Jlist, Tstructure, Jstructure, tn.getChildren(), tn.getId(), lev + 1);

            List<J> j = new ArrayList<J>();
            Jlist.forEach(o -> {
                IBaseEntity mm = (IBaseEntity) o;
                if (mm.get(Jstructure.getIdField()) != null && mm.get(Jstructure.getIdField()).toString() == tn.getId()) {
                    j.add(o);
                }
            });
            if (j != null && j.size() > 0) {
                j.forEach(o -> {
                    IBaseEntity mm = (IBaseEntity) o;
                    TNode n = TNode.newInstance()
                            .id(model.get(Jstructure.getIdField())+"")
                            .text(model.get(Jstructure.getTextField()) + "").build();

                    // 选中
                    if (Jstructure.getCheckedList() != null && Jstructure.getCheckedList().size() > 0) {
                        ValueUtil<Boolean> flag = new ValueUtil<Boolean>(false);
                        Jstructure.getCheckedList().forEach(chk -> {
                            if (chk.equals(n.getId())) {
                                flag.set(true);
                            }
                        });
                        n.setChecked(flag.get());
                    }
                    tn.getChildren().add(n);
                });
            }

            // 父不可选中
            if (Tstructure.getParentChkDisabled()) {
                if (tn.getChildren() != null && tn.getChildren().size() > 0) {
                    tn.setChkDisabled(true);
                } else {
                    tn.setChkDisabled(false);
                }
            }

            // 是否展开
            if (lev <= Tstructure.getOpenLevel()) {
                //if (tn.children != null && tn.children.size() > 0)
                tn.setOpen(true);
            } else {
                tn.setOpen(false);
            }

            // 选中
            if (Tstructure.getCheckedList() != null && Tstructure.getCheckedList().size() > 0) {
                ValueUtil<Boolean> flag = new ValueUtil<Boolean>(false);
                Tstructure.getCheckedList().forEach(id -> {
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

            //不可选中
            if (Tstructure.getChkDisabledList() != null && Tstructure.getChkDisabledList().size() > 0) {
                ValueUtil<Boolean> flag = new ValueUtil<Boolean>(false);
                Tstructure.getChkDisabledList().forEach(id -> {
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

    public static <T, J> List<TNode> getTreeJSON(List<T> list, zTreeStructure Tstructure, List<J> Jlist, zTreeStructure Jstructure) {
        try {

            int level = 1;

            List<TNode> treeNodes = new ArrayList<TNode>();

            loadTreeNode(list, Jlist, Tstructure, Jstructure, treeNodes, "", level);

            return treeNodes;
        } catch (Exception ex) {
            return null;
        }

    }
}
