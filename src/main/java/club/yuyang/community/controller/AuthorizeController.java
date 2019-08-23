package club.yuyang.community.controller;

import club.yuyang.community.dto.AccessTokenDTO;
import club.yuyang.community.dto.GithubUser;
import club.yuyang.community.entity.User;
import club.yuyang.community.provider.GithubProvider;
import club.yuyang.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author yuyang
 * @date 2019/7/27 18:38
 */
@Controller
@Slf4j
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.client.redirect.uri}")
    private String redirectUri;

   @Resource
   private UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state,
                           HttpServletResponse response) throws IOException {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser!=null){
            User user = new User();
            String token = UUID.randomUUID().toString();

            user.setToken(token);
            user.setBio(githubUser.getBio());
            user.setName(githubUser.getName());
            user.setAccountId(githubUser.getId());
            user.setAvatarUrl(githubUser.getAvatarUrl());
            user.setGithubUrl(githubUser.getHtmlUrl());
            user.setLocation(githubUser.getLocation());
            user.setEmail(githubUser.getEmail());

            userService.createOrUpdate(user);
            // 登录成功，写cookie和session
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else {
            // 登录失败，重新登录
            log.error("callback get github error,{}", githubUser);
            return "redirect:/";
        }
    }

    //退出登录
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
