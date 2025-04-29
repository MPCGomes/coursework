package college.contactlist;

public class Company extends Contact {

    private String cnpj;
    private String stateRegistration;
    private String businessName;

    public Company(String name, String address, String email, String cnpj, String stateRegistration, String businessName) {
        super(name, address, email);
        this.cnpj = cnpj;
        this.stateRegistration = stateRegistration;
        this.businessName = businessName;
    }

    public Company() {

    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getStateRegistration() {
        return stateRegistration;
    }

    public void setStateRegistration(String stateRegistration) {
        this.stateRegistration = stateRegistration;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}
