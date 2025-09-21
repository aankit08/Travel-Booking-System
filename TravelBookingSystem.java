import java.util.*;

class User {
    private String name, email, dob, gender, seatPreference;
    public User(String name, String email, String dob, String gender, String seatPreference) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.seatPreference = seatPreference;
    }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getDob() { return dob; }
    public String getGender() { return gender; }
    public String getSeatPreference() { return seatPreference; }
}

abstract class Transport {
    protected String id, type, from, to, date;
    public Transport(String id, String type, String from, String to, String date) {
        this.id = id; this.type = type; this.from = from; this.to = to; this.date = date;
    }
    public String getId() { return id; }
    public String getType() { return type; }
    public String getFrom() { return from; }
    public String getTo() { return to; }
    public String getDate() { return date; }
}

class Train extends Transport {
    private int acSeats, sleeperSeats;
    private boolean[] acSeatMap, sleeperSeatMap;
    private double baseFare;
    private String departure, arrival;

    public Train(String id, String from, String to, String date, int acSeats, int sleeperSeats, double baseFare, String departure, String arrival) {
        super(id, "Train", from, to, date);
        this.acSeats = acSeats; this.sleeperSeats = sleeperSeats;
        this.baseFare = baseFare; this.departure = departure; this.arrival = arrival;
        acSeatMap = new boolean[acSeats];
        sleeperSeatMap = new boolean[sleeperSeats];
    }

    public double getAcFare() { return baseFare * 1.5; }
    public double getSleeperFare() { return baseFare; }
    public int getAcSeats() { return acSeats; }
    public int getSleeperSeats() { return sleeperSeats; }
    public String getDeparture() { return departure; }
    public String getArrival() { return arrival; }

    public void displaySeatsCount(String cls) {
        if(cls.equalsIgnoreCase("AC")) System.out.println("AC seats available: " + acSeats);
        else System.out.println("Sleeper seats available: " + sleeperSeats);
    }

    public int assignRandomSeat(String cls) {
        Random rand = new Random();
        int seatNo;
        if(cls.equalsIgnoreCase("AC")) {
            do { seatNo = rand.nextInt(acSeatMap.length) + 1; } while(acSeatMap[seatNo - 1]);
            acSeatMap[seatNo - 1] = true; acSeats--;
        } else {
            do { seatNo = rand.nextInt(sleeperSeatMap.length) + 1; } while(sleeperSeatMap[seatNo - 1]);
            sleeperSeatMap[seatNo - 1] = true; sleeperSeats--;
        }
        return seatNo;
    }

    public void cancelSeat(String cls, int seatNo) {
        if(cls.equalsIgnoreCase("AC")) { acSeatMap[seatNo-1] = false; acSeats++; }
        else { sleeperSeatMap[seatNo-1] = false; sleeperSeats++; }
    }
}

class Booking {
    User user; Transport transport; String travelClass; int seatNo; double fare; String pnr;

    public Booking(User user, Transport transport, String travelClass, int seatNo, double fare) {
        this.user = user; this.transport = transport; this.travelClass = travelClass;
        this.seatNo = seatNo; this.fare = fare;
        this.pnr = "PNR" + new Random().nextInt(999999);
    }

    public String getUserName() { return user.getName(); }

    public void displayTicket() {
        System.out.println("\n---- Ticket Details ----");
        System.out.println("PNR: " + pnr);
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("DOB: " + user.getDob());
        System.out.println("Gender: " + user.getGender());
        System.out.println("Seat: " + seatNo + " - " + user.getSeatPreference());
        System.out.println("Transport: " + transport.getType() + " ID: " + transport.getId());
        if(transport instanceof Train t)
            System.out.println("Class: " + travelClass + " | Departure: " + t.getDeparture() + " | Arrival: " + t.getArrival());
        System.out.println("Date of Journey: " + transport.getDate());
        System.out.println("Fare: " + fare);
        System.out.println("-----------------------\n");
    }
}

public class TravelBookingSystem {
    static Scanner sc = new Scanner(System.in);
    static List<Booking> bookings = new ArrayList<>();
    static String[] cities = {"Delhi","Mumbai","Bangalore","Chennai","Kolkata","Hyderabad","Pune","Jaipur","Lucknow","Ahmedabad"};

    public static void main(String[] args) {
        while(true) {
            System.out.println("\n==== Travel Booking System ====");
            System.out.println("1. Book Ticket");
            System.out.println("2. Cancel Ticket");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt(); sc.nextLine();
            switch(choice) {
                case 1 -> bookTicket();
                case 2 -> cancelBooking();
                case 3 -> { System.out.println("Exiting. Thank you!"); return; }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private static void bookTicket() {
        System.out.println("Available Cities:");
        for(int i=0;i<cities.length;i++) System.out.println((i+1)+". "+cities[i]);
        System.out.print("Choose source city: "); int fromIndex = sc.nextInt() - 1;
        System.out.print("Choose destination city: "); int toIndex = sc.nextInt() - 1; sc.nextLine();
        if(fromIndex == toIndex){ System.out.println("Source and destination cannot be same!"); return; }
        String fromCity = cities[fromIndex], toCity = cities[toIndex];
        System.out.print("Enter travel date (yyyy-MM-dd): "); String date = sc.nextLine();

        // Generate random 1-6 trains
        Random rand = new Random();
        int nTrains = rand.nextInt(6) + 1;
        List<Train> availableTrains = new ArrayList<>();
        for(int i=1;i<=nTrains;i++){
            int acSeats = rand.nextInt(21)+10;
            int sleeperSeats = rand.nextInt(21)+10;
            double baseFare = rand.nextInt(401)+500;
            String dep = String.format("%02d:00", rand.nextInt(24));
            String arr = String.format("%02d:00", (Integer.parseInt(dep.split(":")[0])+rand.nextInt(5)+1)%24);
            Train t = new Train("T"+(2000+i), fromCity, toCity, date, acSeats, sleeperSeats, baseFare, dep, arr);
            availableTrains.add(t);
        }

        System.out.println("\nAvailable Trains:");
        for(Train t: availableTrains) {
            System.out.println(t.getId()+" | Departure: "+t.getDeparture()+" Arrival: "+t.getArrival()
                +" | AC Seats: "+t.getAcSeats()+" | Sleeper Seats: "+t.getSleeperSeats()
                +" | AC Fare: "+t.getAcFare()+" | Sleeper Fare: "+t.getSleeperFare());
        }

        System.out.print("Enter Train ID to book: "); String tid = sc.nextLine();
        Train chosenTrain = null;
        for(Train t: availableTrains) if(t.getId().equalsIgnoreCase(tid)) chosenTrain = t;
        if(chosenTrain == null){ System.out.println("Invalid Train ID!"); return; }

        // Ask for class first
        System.out.println("Choose class:\n1. AC\n2. Sleeper");
        int cls = sc.nextInt(); sc.nextLine();
        String chosenClass = cls==1?"AC":"Sleeper";

        // Show available seats in selected class
        chosenTrain.displaySeatsCount(chosenClass);

        // Then ask seat preference
        System.out.print("Enter seat preference (Window/Aisle/Upper/Lower): ");
        String seatPref = sc.nextLine();

        double fare = chosenClass.equals("AC") ? chosenTrain.getAcFare() : chosenTrain.getSleeperFare();
        int seatNo = chosenTrain.assignRandomSeat(chosenClass);

        // Personal details
        System.out.print("Enter your name: "); String name = sc.nextLine();
        System.out.print("Enter your email: "); String email = sc.nextLine();
        System.out.print("Enter your date of birth (yyyy-MM-dd): "); String dob = sc.nextLine();
        System.out.print("Enter your gender: "); String gender = sc.nextLine();
        User user = new User(name, email, dob, gender, seatPref);

        // Mock payment
        System.out.print("Enter mock card number: "); sc.nextLine();
        System.out.print("Enter OTP sent to your number: "); sc.nextLine();
        System.out.println("Payment successful!");

        Booking booking = new Booking(user, chosenTrain, chosenClass, seatNo, fare);
        bookings.add(booking);
        booking.displayTicket();
    }

    private static void cancelBooking() {
        System.out.print("Enter your name to cancel booking: "); String name = sc.nextLine();
        for(Booking b: bookings) {
            if(b.getUserName().equalsIgnoreCase(name)) {
                Train t = (Train) b.transport;
                t.cancelSeat(b.travelClass,b.seatNo);
                bookings.remove(b);
                System.out.println("Booking cancelled for "+name);
                return;
            }
        }
        System.out.println("No booking found for "+name);
    }
}
