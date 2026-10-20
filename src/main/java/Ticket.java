
public class Ticket {

    int id;
    String problem;
    String status;

    public Ticket(int id, String problem) {
        this.id = id;
        this.problem = problem;
        this.status = "OPEN";
    }

    public Ticket(int id, String problem, String status) {
        this.id = id;
        this.problem = problem;
        this.status = status;
    }
}
