
package com.helpdesk.model;

public class Ticket {

    private int id;
    private String problem;
    private String status;
    private String userEmail;

    // Constructor for existing tickets
    public Ticket(int id, String problem, String status) {
        this(id, problem, status, null);
    }

    // Constructor for new tickets with user ownership
    public Ticket(int id, String problem, String status,
                  String userEmail) {

        this.id = id;
        this.problem = problem;
        this.status = status;
        this.userEmail = userEmail;
    }

    // Constructor for a new OPEN ticket
    public Ticket(int id, String problem) {
        this(id, problem, "OPEN", null);
    }

    public int getId() {
        return id;
    }

    public String getProblem() {
        return problem;
    }

    public String getStatus() {
        return status;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
