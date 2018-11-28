package com.sslfer.sslftest.dataStructure.tree;

import lombok.Data;

/**
 * 树
 * 数组简单实现
 *
 *       A
 *    /   \
 *   B     C
 *  / \   / \
 * D  E  F   G
 *
 * 树的特点：
 *  1.每个节点有且只有一个父节点；
 *  2.一个父节点有一个或多个子节点；
 *
 * 树的名词解释：
 *  1.节点的度：节点包含直接子节点的个数；例如：节点B的度为2。
 *  2.树的度：该树中，节点度的最大值，就是该树的度；最大节点度为2，所以该树的度也为2。
 *  3.节点的层次：根节点为第一层，直接子节点为第二层，以此类推； B为第二层，D为第三层。
 *  4.树的高度：最子节点为1，依次向上递增；A的高度为3。
 *  5.树的深度：和高度相反；A的深度为1
 *
 * @author sslf
 * @date 2018/11/28
 */
@Data
public class TreeNode {


    /**
     * 父节点的下标
     */
    private int parentId;

    /**
     * 存储的数据
     */
    private Object data;

    public static void main(String[] args) {

        TreeNode[] tree = new TreeNode[10];

        TreeNode node = new TreeNode();
        node.setData("data");
        node.setParentId(0);

    }

}
