package com.coppy_camss.spring.backend.User;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class UserInfo {
    private String userId;
    private String userNm;
    private String password;
    private String psnlPhnNbr;
    private String DEPT;
    private String AUTH;
}
