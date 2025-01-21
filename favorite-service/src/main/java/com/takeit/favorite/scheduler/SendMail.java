package com.takeit.favorite.scheduler;

import com.takeit.favorite.application.dto.product.ProductDto;
import com.takeit.favorite.application.dto.user.UserDto;
import com.takeit.favorite.application.service.EmailService;
import com.takeit.favorite.application.service.FavoriteService;
import com.takeit.favorite.application.service.ProductService;
import com.takeit.favorite.application.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendMail {
    private final ProductService productService;
    private final FavoriteService favoriteService;
    private final UserService userService;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 8 * * *")
    public void sendProductOpenMail() {
        log.info("Sending product open mail start");
        List<ProductDto> todayOpenProducts = productService.getProducts(null).stream()
                .filter(productDto -> productDto.openTime().toLocalDate().equals(LocalDate.now()))
                .toList();

        for(ProductDto todayOpenProduct : todayOpenProducts) {
            List<Long> favoritedUserIds = favoriteService.favoritedUserIdsByProductId(todayOpenProduct.id());
            List<UserDto> users = userService.getUsers(favoritedUserIds);
            String subject = String.format("%s 상품이 오늘 열립니다.", todayOpenProduct.productName());
            String text = String.format(
                        """
                        안녕하세요.
                        
                        고객님이 찜하신 %s 상품이 오늘 %s에 열립니다.
                        
                        감사합니다.
                        """, todayOpenProduct.productName(), todayOpenProduct.openTime().toLocalTime().toString());
            users.forEach(user -> emailService.sendMail(user.email(), subject, text));
        }

        log.info("Sending product open mail end");
    }


}
