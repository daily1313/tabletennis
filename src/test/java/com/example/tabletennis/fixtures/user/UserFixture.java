package com.example.tabletennis.fixtures.user;

import com.example.tabletennis.domain.user.Status;
import com.example.tabletennis.domain.user.User;

import java.util.List;

public class UserFixture {

    public static User createActiveUser() {
        return User.of(1, "activeUser1", "active@example.com");
    }

    public static User createWaitUser() {
        return User.of(31, "waitUser1", "wait@example.com");
    }

    public static User createNonActiveUser() {
        return User.of(61, "nonActiveUser1", "nonactive@example.com");
    }

    public static List<User> createActiveUserList() {
        return List.of(
                User.of(1, "soyung33", "sunho.hong@gmail.com"),
                User.of(2, "seojun.hwang", "sunhang14@gmail.com"),
                User.of(3, "inhwa.jang", "banhee.nam@nam.biz"),
                User.of(4, "vheo", "kyungjoo.lee@yahoo.com"),
                User.of(5, "miyoung45", "kwak.subin@yahoo.com"),
                User.of(6, "namho.kim", "song.yejin@shim.info"),
                User.of(7, "nayun.choi", "sungeun.kwak@hotmail.com"),
                User.of(8, "yang.jieun", "hyounjung04@yahoo.com"),
                User.of(9, "pchang", "lim.jihoo@hotmail.com"),
                User.of(10, "jungshik02", "yoon.sungeun@baek.kr")
        );
    }
}
