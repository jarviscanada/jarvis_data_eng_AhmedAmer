package ca.jrvs.apps.practice.ds;


/**
 * Jarvis Tree
 * @param <E> the type of elements in the tree
 */
public interface JTree<E> {

    /**
     * Inserts item into tree
     * @param element item to be inserted
     * @throws IllegalArgumentException if element already in tree
     */
    E insert(E element);

    /**
     * Search and return element from tree
     * @param element item to be found
     * @return element if found or null
     */
    E search(E element);

    /**
     * Remove item from tree
     * @param element - item to be removed
     * @return removed item
     * @throws IllegalArgumentException if element is not in tree
     */
    E remove(E element);

    /**
     * Traverse tree pre-order
     * @return array of elements in that order
     */
    E[] preOrder();

    /**
     * Traverse tree in-order
     * @return array of elements in that order
     */
    E[] inOrder();

    /**
     * Traverse tree post-order
     * @return array of elements in that order
     */
    E[] postOrder();

    /**
     * Calculate height of the tree
     * @return height of tree
     * @throws NullPointerException if tree is empty
     */
    int findHeight();
}
