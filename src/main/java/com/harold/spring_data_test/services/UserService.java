package com.harold.spring_data_test.services;

import com.harold.spring_data_test.entities.User;
import com.harold.spring_data_test.utils.SystemTempUser;
import com.harold.spring_data_test.utils.SystemUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByPhone(String phone);
    boolean isUserExists(String phone);
    User save(SystemUser systemUser);
}
