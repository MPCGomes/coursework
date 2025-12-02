public class Person {
    private String name;
    private String address;
    private String cpf;
    private String age;
    private String height;
    private String birthDate;

    public Person(String name, String address, String cpf, String age, String height, String birthDate) {
        this.name = name;
        this.address = address;
        this.cpf = cpf;
        this.age = age;
        this.height = height;
        this.birthDate = birthDate;
    }

    public static Person fromString(String line) {
        String[] parts = line.split(";");
        return new Person(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s;%s;%s;%s",
                name, address, cpf, age, height, birthDate);
    }
}