import java.util.ArrayList;

public class RBTree {
    public RBTreeNode nullNode;
    public RBTreeNode root;
    public int colorFlipCount;

    public RBTree() {
        this.nullNode = new RBTreeNode(0,"","");
        this.nullNode.left = null;
        this.nullNode.right = null;
        this.nullNode.isBlackNode = true;
        this.root = this.nullNode;
    }

    public RBTreeNode getRoot() {
        return this.root;
    }

    public RBTreeNode getNullNode() {
        return this.nullNode;
    }

    public RBTreeNode search(int key) {
        RBTreeNode temp = this.root;
        while (temp != this.nullNode) {
            if (temp.BookId == key) {
                return temp;
            }
            if (temp.BookId < key) {
                temp = temp.right;
            } else {
                temp = temp.left;
            }
        }
        return null;
    }

    public RBTreeNode FindClosestNode(int key){
        RBTreeNode current = this.root;
        RBTreeNode closest = null;
        while (current != this.nullNode) {
            if (current.BookId == key) {
                return current;
            }
            if (current.BookId < key) {
                closest = current;
                current = current.right;
            } else {
                closest = current;
                current = current.left;
            }
        }
        return closest;
    }

    private void updateColorFlip(RBTreeNode node, boolean isBlack){
        if(node.isBlackNode != isBlack){
            node.isBlackNode = isBlack;
            colorFlipCount++;
        }
    }

    public void balanceTreeAfterDelete(RBTreeNode node) {
        while (node != this.root && node.isBlackNode) {
            if (node == node.parent.right) {
                RBTreeNode parentSibling = node.parent.left;
                if (!parentSibling.isBlackNode) {
                    updateColorFlip(node.parent,false);
                    updateColorFlip(parentSibling,true);
                    r_rotation(node.parent);
                    parentSibling = node.parent.left;
                }
                if (parentSibling.right.isBlackNode && parentSibling.left.isBlackNode) {
                    updateColorFlip(parentSibling,false);
                    node = node.parent;
                } else {
                    if (parentSibling.left.isBlackNode) {
                        updateColorFlip(parentSibling.right,true);
                        updateColorFlip(parentSibling,false);
                        lRotation(parentSibling);
                        parentSibling = node.parent.left;
                    }
                    parentSibling.isBlackNode = node.parent.isBlackNode;
                    updateColorFlip(node.parent,false);
                    updateColorFlip(parentSibling.left,false);
                    r_rotation(node.parent);
                    node = this.root;
                }
            } else {
                RBTreeNode parentSibling = node.parent.right;
                if (!parentSibling.isBlackNode) {
                    updateColorFlip(node.parent,false);
                    updateColorFlip(parentSibling,true);
                    lRotation(node.parent);
                    parentSibling = node.parent.right;
                }
                if (parentSibling.right.isBlackNode && parentSibling.left.isBlackNode) {
                    updateColorFlip(parentSibling, false);
                    node = node.parent;
                } else {
                    if (parentSibling.right.isBlackNode) {
                        updateColorFlip(parentSibling.left,true);
                        updateColorFlip(parentSibling,false);
                        r_rotation(parentSibling);
                        parentSibling = node.parent.right;
                    }
                    parentSibling.isBlackNode = node.parent.isBlackNode;
                    updateColorFlip(node.parent,true);
                    updateColorFlip(parentSibling.right,true);
                    lRotation(node.parent);
                    node = this.root;
                }
            }
        }
        node.isBlackNode = true;
    }

    private void rbTransplant(RBTreeNode node, RBTreeNode childNode) {
        if (node.parent == null) {
            root = childNode;
        } else if (node == node.parent.right) {
            node.parent.right = childNode;
        } else {
            node.parent.left = childNode;
        }
        childNode.parent = node.parent;
    }

    private int deleteNodeHelper(RBTreeNode node, int key) {
        RBTreeNode deleteNode = nullNode;
        while (node != nullNode) {
            if (node.BookId == key) {
                deleteNode = node;
            }
            if (node.BookId >= key) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        if (deleteNode == nullNode) {
            return 0;
        }

        RBTreeNode y = deleteNode;
        boolean yOriginalColor = y.isBlackNode;
        RBTreeNode x;
        if (deleteNode.left == nullNode) {
            x = deleteNode.right;
            rbTransplant(deleteNode, deleteNode.right);
        } else if (deleteNode.right == nullNode) {
            x = deleteNode.left;
            rbTransplant(deleteNode, deleteNode.left);
        } else {
            y = minimum(deleteNode.right);
            yOriginalColor = y.isBlackNode;
            x = y.right;
            if (y.parent == deleteNode) {
                x.parent = y;
            } else {
                rbTransplant(y, y.right);
                y.right = deleteNode.right;
                y.right.parent = y;
            }

            rbTransplant(deleteNode, y);
            y.left = deleteNode.left;
            y.left.parent = y;
            y.isBlackNode = deleteNode.isBlackNode;
        }
        if (yOriginalColor) {
            balanceTreeAfterDelete(x);
        }

        return key;
    }

    public void balance_after_insert(RBTreeNode curr_node) {
        while (!curr_node.parent.isBlackNode) {
            if (curr_node.parent == curr_node.parent.parent.left) {
                RBTreeNode parent_sibling = curr_node.parent.parent.right;

                if (parent_sibling.isBlackNode) {
                    if (curr_node == curr_node.parent.right) {
                        curr_node = curr_node.parent;
                        lRotation(curr_node);
                    }
                    updateColorFlip(curr_node.parent,true);
                    updateColorFlip(curr_node.parent.parent,false);
                    r_rotation(curr_node.parent.parent);
                } else {
                    updateColorFlip(parent_sibling,true);
                    updateColorFlip(curr_node.parent,true);
                    updateColorFlip(curr_node.parent.parent,false);
                    curr_node = curr_node.parent.parent;
                }
            } else {
                RBTreeNode parent_sibling = curr_node.parent.parent.left;
                if (parent_sibling.isBlackNode) {
                    if (curr_node == curr_node.parent.left) {
                        curr_node = curr_node.parent;
                        r_rotation(curr_node);
                    }
                    updateColorFlip(curr_node.parent,true);
                    updateColorFlip(curr_node.parent.parent,false);
                    lRotation(curr_node.parent.parent);
                } else {
                    updateColorFlip(parent_sibling,true);
                    updateColorFlip(curr_node.parent,true);
                    updateColorFlip(curr_node.parent.parent,false);
                    curr_node = curr_node.parent.parent;
                }
            }

            if (curr_node == root) {
                break;
            }
        }
        root.isBlackNode = true;
    }

    public void FindBooksInRange(RBTreeNode node, int low, int high, ArrayList<RBTreeNode> res) {
        if (node == nullNode) {
            return;
        }

        if (low < node.BookId) {
            FindBooksInRange(node.left, low, high, res);
        }
        if (low <= node.BookId && node.BookId <= high) {
            res.add(node);
        }
        FindBooksInRange(node.right, low, high, res);
    }

    public RBTreeNode minimum(RBTreeNode node) {
        while (node.left != nullNode) {
            node = node.left;
        }
        return node;
    }

    public void lRotation(RBTreeNode x) {
        RBTreeNode y = x.right;
        x.right = y.left;
        if (y.left != nullNode) {
            y.left.parent = x;
        }

        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    public void r_rotation(RBTreeNode x) {
        RBTreeNode y = x.left;
        x.left = y.right;

        if (y.right != this.nullNode) {
            y.right.parent = x;
        }

        y.parent = x.parent;

        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }

        y.right = x;
        x.parent = y;
    }

    public void insert(int BookID, String bookName, String author) {
        RBTreeNode node = new RBTreeNode(BookID,bookName,author);
        node.parent = null;
        node.left = this.nullNode;
        node.right = this.nullNode;
        node.isBlackNode = false;

        RBTreeNode insertion_node = null;
        RBTreeNode temp_node = this.root;

        while (temp_node != this.nullNode) {
            insertion_node = temp_node;

            if (node.BookId < temp_node.BookId) {
                temp_node = temp_node.left;
            } else {
                temp_node = temp_node.right;
            }
        }

        node.parent = insertion_node;

        if (insertion_node == null) {
            this.root = node;
        } else if (node.BookId > insertion_node.BookId) {
            insertion_node.right = node;
        } else {
            insertion_node.left = node;
        }

        if (node.parent == null) {
            node.isBlackNode = true;
            return;
        }

        if (node.parent.parent == null) {
            return;
        }

        this.balance_after_insert(node);
    }

    public int DeleteBook(int bookID) {
        return this.deleteNodeHelper(this.root, bookID);
    }
}