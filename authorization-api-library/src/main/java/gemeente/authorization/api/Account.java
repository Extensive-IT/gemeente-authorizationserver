package gemeente.authorization.api;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

import static gemeente.authorization.api.Address.EMPTY_ADRESS;

public class Account {
    private UUID id;
    private String registrationReferenceId;
    private String emailAddress;
    private String fullName;
    private Address address = EMPTY_ADRESS;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRegistrationReferenceId() {
        return registrationReferenceId;
    }

    public void setRegistrationReferenceId(String registrationReferenceId) {
        this.registrationReferenceId = registrationReferenceId;
    }

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        if (!Objects.isNull(address)) {
            this.address = address;
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Account.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("registrationReferenceId='" + registrationReferenceId + "'")
                .add("emailAddress='" + emailAddress + "'")
                .add("fullName='" + fullName + "'")
                .add("address=" + address)
                .toString();
    }
}
