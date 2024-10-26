package br.com.mangarosa.collections;

import br.com.mangarosa.teste.BinaryTree;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.Assert.*;

public class BinaryTreeTest {

    private final Tree<Integer> binaryTree;

    public BinaryTreeTest(){
        binaryTree = new BinaryTree<>();

        BinaryTree<Integer> teste = new BinaryTree<>();
    }

    @BeforeEach
    public void init(){
        binaryTree.clear();
    }

    @Test
    public void shouldAddElements(){

        binaryTree.add(29);

        // checks if the root was updated
        assertNotNull(binaryTree.root());
        assertNotNull(binaryTree.root().getValue());
        assertEquals(1, binaryTree.size());
        assertEquals(Integer.valueOf(29), binaryTree.root().getValue());

        binaryTree.add(7);
        binaryTree.add(65);
        binaryTree.add(25);
        binaryTree.add(44);
        binaryTree.add(94);

        //Check if the root node exists and if the size of the tree has changed
        assertNotNull(binaryTree.root());
        assertNotNull(binaryTree.root().getValue());
        assertEquals(6, binaryTree.size());

        //Check if the left and right children are not null and if their value are correct
        BinaryTreeNode<Integer> leftNode = binaryTree.root().getLeftChild();
        BinaryTreeNode<Integer> rightNode = binaryTree.root().getRightChild();

        assertNotNull(leftNode);
        assertNotNull(rightNode);

        assertEquals(Integer.valueOf(7), leftNode.getValue());
        assertEquals(Integer.valueOf(65), rightNode.getValue());

        assertNull(leftNode.getLeftChild());
        assertNotNull(leftNode.getRightChild());
        assertNotNull(rightNode.getLeftChild());
        assertNotNull(rightNode.getRightChild());

        assertEquals(Integer.valueOf(25), leftNode.getRightChild().getValue());
        assertEquals(Integer.valueOf(44), rightNode.getLeftChild().getValue());
        assertEquals(Integer.valueOf(94), rightNode.getRightChild().getValue());

        //Check if the nodes are leaves
        assertNull(leftNode.getRightChild().getLeftChild());
        assertNull(leftNode.getRightChild().getRightChild());
        assertNull(rightNode.getLeftChild().getLeftChild());
        assertNull(rightNode.getLeftChild().getRightChild());
        assertNull(rightNode.getRightChild().getLeftChild());
        assertNull(rightNode.getRightChild().getRightChild());

        binaryTree.add(3);
        binaryTree.add(52);
        binaryTree.add(77);
        binaryTree.add(33);

        assertEquals(10, binaryTree.size());

        // left of the left of the root
        assertNotNull(leftNode.getLeftChild());
        assertEquals(Integer.valueOf(3), leftNode.getLeftChild().getValue());

        //check the left of the right of the root
        BinaryTreeNode<Integer> leftRight = rightNode.getLeftChild();

        //check if it is not null and it is not a leaf
        assertNotNull(leftRight);
        assertNotNull(leftRight.getLeftChild());
        assertNotNull(leftRight.getRightChild());

        //check on its children
        assertEquals(Integer.valueOf(33), leftRight.getLeftChild().getValue());
        assertEquals(Integer.valueOf(52), leftRight.getRightChild().getValue());

        //check if they are leaves
        assertNull(leftRight.getLeftChild().getLeftChild());
        assertNull(leftRight.getLeftChild().getRightChild());
        assertNull(leftRight.getRightChild().getLeftChild());
        assertNull(leftRight.getRightChild().getRightChild());


        //check the right of the right of the root
        BinaryTreeNode<Integer> rightRight = rightNode.getRightChild();

        //check if it is not null and it is not a leaf
        assertNotNull(rightRight);
        assertNotNull(rightRight.getLeftChild());
        assertNull(rightRight.getRightChild());

        //check on its children
        assertEquals(Integer.valueOf(77), rightRight.getLeftChild().getValue());

        //check if they are leaves
        assertNull(rightRight.getLeftChild().getLeftChild());
        assertNull(rightRight.getLeftChild().getRightChild());
    }

    @Test
    public void shouldRemoveElements(){

        binaryTree.add(29);
        binaryTree.add(7);
        binaryTree.add(65);
        binaryTree.add(25);
        binaryTree.add(44);
        binaryTree.add(94);
        binaryTree.add(3);
        binaryTree.add(52);
        binaryTree.add(77);
        binaryTree.add(33);

        BinaryTreeNode<Integer> root = binaryTree.root();
        assertNotNull(root);
        assertEquals(10, binaryTree.size());

        //remove leaves
        binaryTree.remove(3);
        binaryTree.remove(52);

        assertEquals(8, binaryTree.size());

        BinaryTreeNode<Integer> left = root.getLeftChild();
        BinaryTreeNode<Integer> right = root.getRightChild();

        assertNotNull(left);
        assertNotNull(right);

        //left node has only the right child
        assertNull(left.getLeftChild());
        assertNotNull(left.getRightChild());

        BinaryTreeNode<Integer> leftRight = right.getLeftChild();

        assertNotNull(leftRight);
        // left child of thr right child has only one child
        assertNotNull(leftRight.getLeftChild());
        assertNull(leftRight.getRightChild());

        //remove a node with only one child (left side)
        binaryTree.remove(44);
        assertEquals(7, binaryTree.size());

        leftRight = right.getLeftChild();
        assertNotNull(leftRight);
        assertEquals(Integer.valueOf(33), leftRight.getValue());

        //remove a node with only one child (right side)
        binaryTree.remove(7);
        assertEquals(6, binaryTree.size());

        left = root.getLeftChild();
        assertNotNull(left);
        assertEquals(Integer.valueOf(25), left.getValue());

        //remove a node with two children
        binaryTree.remove(65);
        assertEquals(5, binaryTree.size());

        right = root.getRightChild();
        assertNotNull(right);
        assertEquals(Integer.valueOf(77), right.getValue());

        assertNotNull(right.getLeftChild());
        assertNotNull(right.getRightChild());
        assertEquals(Integer.valueOf(33), right.getLeftChild().getValue());
        assertEquals(Integer.valueOf(94), right.getRightChild().getValue());

        // remove root
        binaryTree.remove(29);

        root = binaryTree.root();
        assertNotNull(root);
        assertEquals(4, binaryTree.size());

        assertEquals(Integer.valueOf(33), root.getValue());

        left = root.getLeftChild();
        right = root.getRightChild();

        assertNotNull(left);
        assertNotNull(right);

        //check left child
        assertEquals(Integer.valueOf(25), left.getValue());
        assertNull(left.getLeftChild());
        assertNull(left.getRightChild());

        //check right child
        assertEquals(Integer.valueOf(77), right.getValue());
        assertNull(right.getLeftChild());
        assertNotNull(right.getRightChild());
        assertEquals(Integer.valueOf(94), right.getRightChild().getValue());
    }

    @Test
    public void shouldContainsElements(){

        //tree is empty
        assertNull(binaryTree.root());
        assertFalse(binaryTree.contains(29));
        binaryTree.add(29);
        assertTrue(binaryTree.contains(29));

        //add two new nodes.
        binaryTree.add(7);
        binaryTree.add(65);
        assertTrue(binaryTree.contains(7));
        assertTrue(binaryTree.contains(65));

        //insert new nodes
        binaryTree.add(25);
        binaryTree.add(44);
        binaryTree.add(94);
        binaryTree.add(3);
        binaryTree.add(52);
        binaryTree.add(77);
        binaryTree.add(33);

        //contains and not contains
        assertTrue(binaryTree.contains(44));
        assertTrue(binaryTree.contains(33));
        assertFalse(binaryTree.contains(54));
        assertFalse(binaryTree.contains(35));
        assertFalse(binaryTree.contains(23));

        //contains, remove and not contains
        assertTrue(binaryTree.contains(52));
        assertTrue(binaryTree.contains(77));

        binaryTree.remove(52);
        binaryTree.remove(77);

        assertFalse(binaryTree.contains(52));
        assertFalse(binaryTree.contains(77));

        //contains, remove, not contains - root value
        assertTrue(binaryTree.contains(29));
        binaryTree.remove(29);
        assertFalse(binaryTree.contains(29));

        //not contains, add, contains
        assertFalse(binaryTree.contains(24));
        binaryTree.add(24);
        assertTrue(binaryTree.contains(24));
    }

    @Test
    public void shouldBeEmpty(){

        //empty tree
        assertNull(binaryTree.root());
        assertTrue(binaryTree.isEmpty());

        //add an element
        binaryTree.add(29);

        //not empty tree
        assertNotNull(binaryTree.root());
        assertTrue(binaryTree.contains(29));
        assertFalse(binaryTree.isEmpty());

        //insert new elements
        binaryTree.add(7);
        binaryTree.add(65);
        assertFalse(binaryTree.isEmpty());

        //partial removal of elements
        binaryTree.remove(65);
        binaryTree.remove(7);
        assertFalse(binaryTree.isEmpty());

        //complete removal of elements
        binaryTree.remove(29);
        assertTrue(binaryTree.isEmpty());

        binaryTree.add(29);
        binaryTree.add(65);
        binaryTree.add(7);

        assertFalse(binaryTree.isEmpty());

        binaryTree.clear();

        assertTrue(binaryTree.isEmpty());
    }

    @Test
    public void shouldBeLeaf(){

        // not exists
        assertNull(binaryTree.root());

        // exists and it is the unique node
        binaryTree.add(29);
        assertTrue(binaryTree.isLeaf(29));

        // exists and it is a leaf and the root node is not a leaf
        binaryTree.add(7);
        binaryTree.add(65);
        assertFalse(binaryTree.isLeaf(29));
        assertTrue(binaryTree.isLeaf(7));
        assertTrue(binaryTree.isLeaf(65));

        //insert new nodes
        binaryTree.add(25);
        assertFalse(binaryTree.isLeaf(29));
        assertFalse(binaryTree.isLeaf(7));
        assertTrue(binaryTree.isLeaf(65));

        binaryTree.add(44);
        assertFalse(binaryTree.isLeaf(29));
        assertFalse(binaryTree.isLeaf(7));
        assertFalse(binaryTree.isLeaf(65));


        binaryTree.add(94);
        binaryTree.add(3);
        binaryTree.add(52);
        binaryTree.add(77);
        binaryTree.add(33);

        assertFalse(binaryTree.isLeaf(29));
        assertFalse(binaryTree.isLeaf(7));
        assertFalse(binaryTree.isLeaf(65));
        assertFalse(binaryTree.isLeaf(44));
        assertFalse(binaryTree.isLeaf(94));
        assertTrue(binaryTree.isLeaf(3));
        assertTrue(binaryTree.isLeaf(25));
        assertTrue(binaryTree.isLeaf(33));
        assertTrue(binaryTree.isLeaf(52));
        assertTrue(binaryTree.isLeaf(77));

    }

    @Test
    public void shouldConvertToArray(){

        //empty
        assertNull(binaryTree.toList());
        binaryTree.add(29);

        assertNotNull(binaryTree.toList());
        assertEquals(Integer.valueOf(29), binaryTree.toList().get(0));

        binaryTree.add(7);
        binaryTree.add(65);
        assertNotNull(binaryTree.toList());
        assertEquals(List.of(7, 29, 65), binaryTree.toList());

        binaryTree.add(25);
        binaryTree.add(44);
        assertNotNull(binaryTree.toList());
        assertEquals(List.of(7, 25, 29, 44, 65), binaryTree.toList());

        binaryTree.add(94);
        binaryTree.add(3);
        binaryTree.add(52);
        binaryTree.add(77);
        binaryTree.add(33);

        assertNotNull(binaryTree.toList());
        assertEquals(List.of(3, 7, 25, 29, 33, 44, 52, 65, 77, 94), binaryTree.toList());

        binaryTree.remove(29);
        binaryTree.remove(7);
        binaryTree.remove(65);
        binaryTree.remove(25);
        binaryTree.remove(44);
        assertNotNull(binaryTree.toList());
        assertEquals(List.of(3, 33, 52, 77, 94), binaryTree.toList());

        binaryTree.remove(94);
        binaryTree.remove(3);
        binaryTree.remove(52);
        binaryTree.remove(77);
        binaryTree.remove(33);

        assertNull(binaryTree.toList());
    }

    @Test
    public void shouldClearTheTree(){

        assertNull(binaryTree.root());

        binaryTree.add(29);
        assertNotNull(binaryTree.root());

        binaryTree.add(7);
        binaryTree.add(65);
        binaryTree.add(25);
        binaryTree.add(44);

        assertNotNull(binaryTree.root());

        binaryTree.clear();

        assertNull(binaryTree.root());
    }
}
