import java.time.LocalDateTime;
public class Reservation implements Comparable<Reservation> {

    int patronID;
    int priorityNumber;
    LocalDateTime timeOfReservation;

    //Constructor
    public Reservation(int patronID, int priorityNumber, LocalDateTime timeOfReservation){
        this.patronID = patronID;
        this.priorityNumber = priorityNumber;
        this.timeOfReservation = timeOfReservation;
    }

    @Override
    public int compareTo(Reservation other){
        if(other!=null) {
            if (this.priorityNumber != other.priorityNumber) {
                return Integer.compare(this.priorityNumber, other.priorityNumber);
            }
            return this.timeOfReservation.compareTo(other.timeOfReservation);
        }else {
            return 0;
        }
    }

}
