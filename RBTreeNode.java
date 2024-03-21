import java.util.TreeMap;

public class RBTreeNode {
        int BookId; //key
        String BookName;
        String AuthorName;
        boolean AvailabilityStatus;
        int BorrowedBy;
        BinaryMinHeap ReservationHeap;
        RBTreeNode parent, left, right;
        boolean isBlackNode;

        public RBTreeNode(int bookId, String bookName, String authorName){
            this.BookId = bookId;
            this.BookName = bookName;
            this.AuthorName = authorName;
            this.AvailabilityStatus = true;
            this.BorrowedBy = -1;
            this.ReservationHeap = new BinaryMinHeap();
            this.left = this.parent = this.right = null;
            this.isBlackNode = true;
        }
}
