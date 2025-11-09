package com.sample.springtraining.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sample.springtraining.entity.Member;
import com.sample.springtraining.projection.NameOnly;
import com.sample.springtraining.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*
     * 모든 사용자 조회
     */
    @GetMapping
    public List<Member> getAllUsers() {
        return userService.findAllUsers();
    }

    /*
     * 닉네임으로 사용자 조회 (PathVariable)
     */
    @GetMapping("/{nickname}")
    public Member getUserByNickname(@PathVariable("nickname") String nickname) {
        return userService.findUserByNickname(nickname);
    }

    /*
     * 이메일로 사용자 조회 (RequestParam)
     */
    @GetMapping("/email")
    public List<Member> getUsersOrderByEmail(@RequestParam("query") String query) {
        return userService.findUsersByEmailContaining(query);
    }

    /*
     * 이메일로 사용자 이름 조회 (Projection Interface)
     */
    @GetMapping("/name-only/{email}")
    public List<NameOnly> getNameOnlyByEmail(@PathVariable("email") String email) {
        return userService.findNameOnlyByEmail(email);
    }
}
