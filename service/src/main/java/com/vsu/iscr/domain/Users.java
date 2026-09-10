package com.vsu.iscr.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users implements Serializable {
    private Integer uId;
    private String userName;
    private String email;
    private String password;
    private String phone;
    private Integer role;

}
