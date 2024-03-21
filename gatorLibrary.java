import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class gatorLibrary {

    RBTree rbt = new RBTree();
    public gatorLibrary(){
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Please provide the input file path as an argument.");
            return;
        }

        String inputFilePath = args[0];
        String outputFilePath = inputFilePath.substring(0, inputFilePath.lastIndexOf(".")) + "_output.txt";
        gatorLibrary gatorLibrary = new gatorLibrary();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             PrintWriter writer = new PrintWriter(new FileOutputStream(outputFilePath))) {

            //Redirecting System.out to the PrintWriter
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(new FileOutputStream(outputFilePath), true));

            String line;
            while ((line = reader.readLine()) != null) {
                //Process each line and call the corresponding method.
                processLine(gatorLibrary, line);
            }

            System.setOut(originalOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processLine(gatorLibrary gatorLibrary, String line){
        String methodName = line.substring(0,line.indexOf('('));
        String paramString = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
        String[] params = paramString.split(",");

        switch(methodName){
            case "InsertBook":
                int bookId = Integer.parseInt(params[0].trim());
                String title = params[1].trim().replaceAll("\"","");
                String author = params[2].trim().replaceAll("\"","");
                String availability = params[3].trim().replaceAll("\"","");
                gatorLibrary.InsertBook(bookId,title,author,availability);
                break;
            case "BorrowBook":
                int PatronID = Integer.parseInt(params[0].trim());
                int bookID = Integer.parseInt(params[1].trim());
                int priority = Integer.parseInt(params[2].trim());
                gatorLibrary.BorrowBook(PatronID,bookID,priority);
                break;
            case "ReturnBook":
                PatronID = Integer.parseInt(params[0].trim());
                bookID = Integer.parseInt(params[1].trim());
                gatorLibrary.ReturnBook(PatronID,bookID);
                break;
            case "DeleteBook":
                bookID = Integer.parseInt(params[0].trim());
                gatorLibrary.DeleteBook(bookID);
                break;
            case "PrintBook":
                bookID = Integer.parseInt(params[0].trim());
                gatorLibrary.PrintBook(bookID);
                break;
            case "PrintBooks":
                int low = Integer.parseInt(params[0].trim());
                int high = Integer.parseInt(params[1].trim());
                gatorLibrary.PrintBooks(low, high);
                break;
            case "FindClosestBook":
                bookID = Integer.parseInt(params[0].trim());
                gatorLibrary.FindClosestBook(bookID);
                break;
            case "ColorFlipCount":
                gatorLibrary.ColorFlipCount();
                break;
            default:
                gatorLibrary.Quit();
                break;
        }
    }

    public void InsertBook(int bookID, String title,String author,String availability){
        if(rbt.search(bookID)!=null){
            System.out.println("Duplicate Book ID"+"\n");
        } else {
            rbt.insert(bookID,title,author);
        }
    }

    public void BorrowBook(int PatronID, int bookID, int priority){
        RBTreeNode node = rbt.search(bookID);
        if(node.BorrowedBy == -1){
            node.BorrowedBy = PatronID;
            node.AvailabilityStatus = false;
            System.out.println("Book "+bookID+" Borrowed by Patron "+PatronID+"\n");
        }else{
            node.ReservationHeap.insert(new Reservation(PatronID,priority, LocalDateTime.now()));
            System.out.println("Book "+bookID+" Reserved by Patron "+PatronID+"\n");
        }
    }

    public void FindClosestBook(int bookID){
        RBTreeNode node = rbt.FindClosestNode(bookID);
        PrintBook(node.BookId);
    }

    public void DeleteBook(int bookID){
        RBTreeNode node = rbt.search(bookID);
        rbt.DeleteBook(bookID);
        int reservationSize = node.ReservationHeap.getAllReservations().size();
        if(reservationSize == 0){
            System.out.println("Book "+bookID+" is no longer available."+"\n");
        }else if(reservationSize == 1){
            int patronID = node.ReservationHeap.getAllReservations().get(0).patronID;
            System.out.println("Book "+bookID+" is no longer available. Reservation made by Patron "+patronID+" has been cancelled!"+"\n");
        }else{
            List<Reservation> reservationList = node.ReservationHeap.getAllReservations();
            StringBuilder patronIDs = new StringBuilder();
            for(Reservation res: reservationList){
                patronIDs.append(" ").append(res.patronID).append(",");
            }
            patronIDs.deleteCharAt(patronIDs.length() - 1);
            System.out.println("Book "+bookID+" is no longer available. Reservations made by Patrons"+patronIDs+" have been cancelled!"+"\n");
        }
    }

    public void ReturnBook(int patronID, int bookID) {
        RBTreeNode node = rbt.search(bookID);
        System.out.println("Book " + bookID + " Returned by Patron " + patronID + "\n");

        // Check if there are any reservations in the waitlist
        if (node.ReservationHeap.getAllReservations().isEmpty()) {
            // No reservations, the book is available
            node.BorrowedBy = -1;
            node.AvailabilityStatus = true;
        } else {
            // There are reservations, assign the book to the next patron in the waitlist
            Reservation topReservation = node.ReservationHeap.extractMin();
            int nextPatronID = topReservation.patronID;
            node.BorrowedBy = nextPatronID;
            System.out.println("Book " + bookID + " Allotted to Patron " + nextPatronID + "\n");
        }
    }


    public void ColorFlipCount(){
        System.out.println("Color Flip Count: "+ rbt.colorFlipCount+"\n");
    }

    public void PrintBooks(int low, int high){
        ArrayList<RBTreeNode> list = new ArrayList<>();
        RBTreeNode root = rbt.getRoot();
        rbt.FindBooksInRange(root, low, high, list);

        for(RBTreeNode book: list){
            PrintBook(book.BookId);
        }
    }

    public void PrintBook(int bookID){
        RBTreeNode node = rbt.search(bookID);
        if(node!=null){
            String availability = node.AvailabilityStatus ? "Yes" : "No";
            String borrowedBy = node.BorrowedBy > 0 ? String.valueOf(node.BorrowedBy) : "None";
            StringBuilder reservations = new StringBuilder();
            reservations.append("[");
            for(Reservation reservation: node.ReservationHeap.getAllReservations()){
                reservations.append(reservation.patronID).append(",");
            }
            if(reservations.length()>1){
                //Removing last comma
                reservations.deleteCharAt(reservations.length() - 1);
            }
            reservations.append("]");

            System.out.println("BookID = " + node.BookId);
            System.out.println("Title = \"" + node.BookName + "\"");
            System.out.println("Author = \"" + node.AuthorName + "\"");
            System.out.println("Availability = \"" + availability + "\"");
            System.out.println("BorrowedBy = " + borrowedBy);
            System.out.println("Reservations = " + reservations+"\n");
        }else{
            System.out.println("Book "+bookID+" not found in the library"+"\n");
        }
    }
    public void Quit(){
        System.out.println("Program Terminated!!");
        System.exit(0);
    }
}
