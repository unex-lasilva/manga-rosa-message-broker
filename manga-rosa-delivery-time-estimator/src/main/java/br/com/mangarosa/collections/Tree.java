package br.com.mangarosa.interfaces;

public interface Tree <T extends Comparable<T>>{

    void add(T value);

    boolean remove(T value);

    boolean contains(T value);

    boolean isLeaf(T value);

    BinaryTreeNode<T> root();
}
