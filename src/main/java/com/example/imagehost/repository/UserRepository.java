package com.example.imagehost.repository;

import com.example.imagehost.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {  // 第二个泛型大概是主键的类型
}
