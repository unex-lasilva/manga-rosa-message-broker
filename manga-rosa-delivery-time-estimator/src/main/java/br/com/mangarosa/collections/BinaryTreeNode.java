package br.com.mangarosa.interfaces;

public interface BinaryTreeNode<T extends Comparable<T>> {
    BinaryTreeNode<T> leftChild();
    BinaryTreeNode<T> rightChild();
    T value();
}
