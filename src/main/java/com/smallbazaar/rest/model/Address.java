package com.smallbazaar.rest.model;

import javax.persistence.Column;

public class Address {

	@Column (name = "street_address", length = 30)
    private String street1;
	
    @Column (name = "street_number", length = 10)
    private String streetNumber;
    
    @Column (name = "zip_code", length = 6)
    private String zipCode;
    
    @Column (name = "locality", length = 30)
    private String locality;
    
    @Column (name = "country", length = 30)
    private String country;
}
