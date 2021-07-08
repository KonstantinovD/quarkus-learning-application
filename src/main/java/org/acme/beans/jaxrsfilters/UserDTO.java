package org.acme.beans.jaxrsfilters;

import lombok.Data;

@Data
public class UserDTO {
    private String userId;
    private String login;
    private String password;
}
