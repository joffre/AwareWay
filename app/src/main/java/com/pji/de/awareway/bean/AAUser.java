package com.pji.de.awareway.bean;

/**
 * Created by Geoffrey on 18/04/2016.
 */
public class AAUser {

    public static final String ID_USER = "idUser";
    public static final String EMAIL_USER = "email";
    public static final String FIRST_NAME_USER = "firstName";
    public static final String LAST_NAME_USER = "lastName";
    public static final String PASSWORD_USER = "password";

    private Integer idUser;

    private String email;

    private String firstName;

    private String lastName;

    private String password;

    public AAUser(Integer idUser, String email, String firstName,
                  String lastName, String password) {
        super();
        this.idUser = idUser;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public AAUser() {
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
