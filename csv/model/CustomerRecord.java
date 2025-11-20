package files.model;

public class CustomerRecord {
    private String name;
    private String age;
    private String cpf;
    private String rg;
    private String birthDate;
    private String gender;
    private String zodiacSign;
    private String motherName;
    private String fatherName;
    private String email;
    private String password;
    private String zipCode;
    private String address;
    private String number;
    private String neighborhood;
    private String city;
    private String state;
    private String landlinePhone;
    private String mobilePhone;
    private String height;
    private String weight;
    private String bloodType;
    private String color;

    public CustomerRecord(
            String name,
            String age,
            String cpf,
            String rg,
            String birthDate,
            String gender,
            String zodiacSign,
            String motherName,
            String fatherName,
            String email,
            String password,
            String zipCode,
            String address,
            String number,
            String neighborhood,
            String city,
            String state,
            String landlinePhone,
            String mobilePhone,
            String height,
            String weight,
            String bloodType,
            String color
    ) {
        this.name = name;
        this.age = age;
        this.cpf = cpf;
        this.rg = rg;
        this.birthDate = birthDate;
        this.gender = gender;
        this.zodiacSign = zodiacSign;
        this.motherName = motherName;
        this.fatherName = fatherName;
        this.email = email;
        this.password = password;
        this.zipCode = zipCode;
        this.address = address;
        this.number = number;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.landlinePhone = landlinePhone;
        this.mobilePhone = mobilePhone;
        this.height = height;
        this.weight = weight;
        this.bloodType = bloodType;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String toCsv() {
        return String.join(",",
                name,
                age,
                cpf,
                rg,
                birthDate,
                gender,
                zodiacSign,
                motherName,
                fatherName,
                email,
                password,
                zipCode,
                address,
                number,
                neighborhood,
                city,
                state,
                landlinePhone,
                mobilePhone,
                height,
                weight,
                bloodType,
                color
        );
    }
}
