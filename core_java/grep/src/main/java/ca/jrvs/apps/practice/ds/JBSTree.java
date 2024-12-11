//package ca.jrvs.apps.practice.ds;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Objects;
//
//
///**
// * Simplified BST implementation
// * @param <E> type of elements in tree
// */
//public class JBSTree<E> implements JTree<E> {
//
//    /**
//     * Comparator used to maintain order in tree, cannot be null
//     */
//    private Comparator<E> comparator;
//
//    /**
//     * Create new Jarvis BST
//     * @param comparator comporator used to order the elements in this tree
//     * @throws IllegalArgumentException if comparator is null
//     */
//    public JBSTree(Comparator<E> comparator) {
//        this.comparator = comparator;
//    }
//
//    /**
//     * Inserts item into tree
//     *
//     * @param element item to be inserted
//     * @throws IllegalArgumentException if element already in tree
//     */
//    @Override
//    public E insert(E element) {
//        return null;
//    }
//
//    /**
//     * Search and return element from tree
//     *
//     * @param element item to be found
//     * @return element if found or null
//     */
//    @Override
//    public E search(E element) {
//        return null;
//    }
//
//    /**
//     * Remove item from tree
//     *
//     * @param element - item to be removed
//     * @return removed item
//     * @throws IllegalArgumentException if element is not in tree
//     */
//    @Override
//    public E remove(E element) {
//        return null;
//    }
//
//    /**
//     * Traverse tree pre-order
//     *
//     * @return array of elements in that order
//     */
//    @Override
//    public E[] preOrder() {
//    }
//
//    /**
//     * Traverse tree in-order
//     *
//     * @return array of elements in that order
//     */
//    @Override
//    public E inOrder() {
//    }
//
//    /**
//     * Traverse tree post-order
//     *
//     * @return array of elements in that order
//     */
//    @Override
//    public E postOrder() {
//    }
//
//    /**
//     * Calculate height of the tree
//     *
//     * @return height of tree
//     * @throws NullPointerException if tree is empty
//     */
//    @Override
//    public int findHeight() {
//        return 0;
//    }
//
//    static final class Node<E> {
//
//        E value;
//        Node<E> parent;
//        Node<E> rightNode;
//        Node<E> leftNode;
//
//        public Node(E value, Node<E> parent) {
//            this.value = value;
//            this.parent = parent;
//        }
//
//        public E getValue() {
//            return value;
//        }
//
//        public void setValue(E value) {
//            this.value = value;
//        }
//
//        public void setLeftNode(Node<E> leftNode) {
//            this.leftNode = leftNode;
//        }
//
//        public void setRightNode(Node<E> rightNode) {
//            this.rightNode = rightNode;
//        }
//
//        public Node<E> getLeftNode() {
//            return leftNode;
//        }
//
//        public Node<E> getRightNode() {
//            return rightNode;
//        }
//
//        public void setParent(Node<E> parent) {
//            this.parent = parent;
//        }
//
//        public Node<E> getParent() {
//            return parent;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (!(o instanceof Node)) return false;
//            Node<?> node = (Node<?>) o;
//
//            return getValue().equals(node.getLeftNode())
//                    &&
//                    Objects.equals(node.getLeftNode(), getLeftNode())
//                    &&
//                    Objects.equals(node.getRightNode(), getRightNode())
//                    &&
//                    getParent().equals(node.getParent());
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(getValue(), getLeftNode(), getRightNode(), getParent());
//        }
//    }
//}
