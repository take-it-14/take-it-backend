package com.takeit.auth.application.dto;

import com.takeit.auth.domain.entity.User;
import com.takeit.auth.domain.entity.UserRole;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

public record UserPageResponse(
    UserPage userPage
) {
    public static UserPageResponse from(Page<User> userPage) {
        return new UserPageResponse(new UserPage(userPage));
    }

    public static class UserPage extends PagedModel<UserPage.User> {

        public UserPage(Page<com.takeit.auth.domain.entity.User> userPage) {
            super(
                    new PageImpl<>(
                            UserPage.User.from(userPage.getContent()),
                            userPage.getPageable(),
                            userPage.getTotalElements()
                    )
            );
        }

        public static record User(
                String username,
                String nickname,
                String email,
                UserRole role
        ) {
            public static List<User> from(List<com.takeit.auth.domain.entity.User> userList) {
                return userList.stream()
                        .map(User::from)
                        .toList();
            }

            public static User from(com.takeit.auth.domain.entity.User user) {
                return new User(
                        user.getUsername(),
                        user.getNickname(),
                        user.getEmail(),
                        user.getRole()
                );
            }
        }
    }
}
