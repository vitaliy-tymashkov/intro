package com.globallogic.basecamp.model;

/**
 * Address is a representation of the {@link com.globallogic.basecamp.model.Student Student} home address.
 */
public class Address {

    private String country;

    private String city;

    private String street;

    private Integer houseNumber;

    /**
     * TODOx - done: implement getters and other methods if necessary
     */
    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    /**
     * Allows to get the builder for the Address
     *
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class is a part of the builder pattern implementation
     * Needed to ease the Address object construction
     * <p>
     * TODOx - done: implement the builder functionality
     */
    public static class Builder {
        private final Address newAddress;

        public Builder(){
            newAddress = new Address();
        }

        public Builder setCountry(String country) {
            newAddress.country = country;
            return this;
        }

        public Builder setCity(String city) {
            newAddress.city = city;
            return this;
        }

        public Builder setStreet(String street) {
            newAddress.street = street;
            return this;
        }

        public Builder setHouseNumber(Integer houseNumber) {
            newAddress.houseNumber = houseNumber;
            return this;
        }

        public Address build() {
            return newAddress;
        }
    }

}
