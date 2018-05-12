public class Student {

    // first_name | last_name | programs | email | phone | city | state | team
    //      0     |     1     |     2    |   3   |   4   |   5  |   6   |  7

    private String firstName;
    private String lastName;
    private String programs;
    private String email;
    private String phone;
    private String team;
    private String role;

    public Student(String firstName, String lastName,
                   String programs, String email, String phone,
                   String team, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.programs = programs;
        this.email = email;
        this.phone = phone;
        this.team = team;
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPrograms() {
        return programs;
    }

    public void setPrograms(String programs) {
        this.programs = programs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isLocal(){
        return this.programs.contains("ON-CAMPUS");
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", programs='" + programs + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", team='" + team + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
