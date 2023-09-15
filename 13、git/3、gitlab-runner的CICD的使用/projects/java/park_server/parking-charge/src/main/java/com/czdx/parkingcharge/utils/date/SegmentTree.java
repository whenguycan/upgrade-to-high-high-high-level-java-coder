package com.czdx.parkingcharge.utils.date;

import lombok.Data;

/**
 *
 * description: 线段树节点
 * @author mingchenxu
 * @date 2023/3/24 14:25
 */
@Data
public class SegmentTree<E> {

    private Node[] data;
    private int size;

    private Merger<E> merger;

    public SegmentTree(E[] source, Merger<E> merger) {
        this.merger = merger;
        this.size = source.length;
        this.data = new Node[size * 4];
//        buildTree(0, source, 0, size - 1);
    }


    private static class Node<E> {

        E data;
        int left;
        int right;

        Node(E data, int left, int right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return String.valueOf(data);
        }
    }


    public interface Merger<E> {
        E merge(E e1, E e2);
    }

}
