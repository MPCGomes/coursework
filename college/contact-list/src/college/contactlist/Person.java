package college.contactlist;

public class Person extends Contact {

    private String cpf;
    private String birthday;
    private String maritalStatus;

    public Person(String name, String address, String email, String cpf, String birthday, String maritalStatus) {
        super(name, address, email);
        this.cpf = cpf;
        this.birthday = birthday;
        this.maritalStatus = maritalStatus;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }
}
