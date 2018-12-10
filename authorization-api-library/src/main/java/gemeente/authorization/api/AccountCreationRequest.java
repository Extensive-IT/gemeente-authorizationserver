package gemeente.authorization.api;

public class AccountCreationRequest {
    private String emailAddress;
    private String fullName;
    private String registrationReferenceId;
    private Address address;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRegistrationReferenceId() {
        return registrationReferenceId;
    }

    public void setRegistrationReferenceId(String registrationReferenceId) {
        this.registrationReferenceId = registrationReferenceId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
