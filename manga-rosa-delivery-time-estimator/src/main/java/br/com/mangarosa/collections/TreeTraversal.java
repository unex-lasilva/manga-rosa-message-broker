package br.com.mangarosa.interfaces;

import java.util.List;

public interface TreeTraversal <T extends Comparable<T>> {
    List<T> traverse(Tree<T> tree);
}
