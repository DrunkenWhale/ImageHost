package com.example.imagehost.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

// 引入spring-boot-starter-data-jpa依赖才能使用
@Entity
@Table(name="user")
public class User implements Serializable {

    @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY) 自动增长 主键为null时自动累加
    private String mailbox;

    @Column(nullable = false)
    private String password;

    @Column
    private int counts;  // 每个人上传的图片数量

    @OneToMany(targetEntity = Image.class, mappedBy = "host")
    private List<Image> images;

    public User(String mailbox, String password){
        this.mailbox = mailbox;
        this.password = password;   // 加密还没撸呢
        this.counts = 0;
    }

    public User() {}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
