package com.takeit.product.application.service;

import com.takeit.common.exception.CustomException;
import com.takeit.common.exception.ErrorCode;
import com.takeit.product.application.dto.category.CategoryEntityResponse;
import com.takeit.product.application.dto.category.CategoryResponse;
import com.takeit.product.application.dto.category.CreateCategoryDto;
import com.takeit.product.application.dto.category.UpdateCategoryDto;
import com.takeit.product.application.dto.user.UserDto;
import com.takeit.product.domain.entity.Category;
import com.takeit.product.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.takeit.common.utils.AccessValidator.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @Transactional
    public CategoryEntityResponse createCategory(CreateCategoryDto request, String requesterUsername) {
        UserDto userDto = userService.getUser(requesterUsername);
        if(!isMaster(userDto.role()) && !isManager(userDto.role())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Optional<Category> category = categoryRepository.findByName(request.name());
        if (category.isPresent()) {
            category.get().restore();
        } else {
            category = Optional.of(categoryRepository.save(
                    Category.create(request.name())
            ));
        }

        return CategoryEntityResponse.from(category.get());
    }

    @Transactional
    public CategoryEntityResponse updateCategory(UpdateCategoryDto request, UUID categoryId, String requesterUsername) {
        UserDto userDto = userService.getUser(requesterUsername);
        if(!isMaster(userDto.role()) && !isManager(userDto.role())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        return categoryRepository.findByUuidAndIsDeleteFalse(categoryId).map(category -> {
            category.update(request.name());
            return CategoryEntityResponse.from(category);
        }).orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

    }

    @Transactional
    public void deleteCategory(Long categoryId, String requesterUsername) {
        UserDto userDto = userService.getUser(requesterUsername);
        if(!isMaster(userDto.role()) && !isManager(userDto.role())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        categoryRepository.findByIdAndIsDeleteFalse(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND))
                .delete(userDto.username());
    }

    public CategoryEntityResponse getCategory(Long categoryId, String requesterUsername) {
        UserDto userDto = userService.getUser(requesterUsername);
        if(!isMaster(userDto.role()) && !isManager(userDto.role())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        return categoryRepository.findByIdAndIsDeleteFalse(categoryId)
                .map(CategoryEntityResponse::from)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }
  
    public PagedModel<CategoryResponse> getAllCategories(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.getAllCategories(pageable);
        return new PagedModel<>(
                new PageImpl<>(
                        categoryPage.getContent().stream()
                                .map(category -> CategoryResponse.of(category.getUuid(), category.getName()))
                                .toList(),
                        categoryPage.getPageable(),
                        categoryPage.getTotalElements()
                )
        );
    }

    /*
        feign 전용 service
     */

    public CategoryEntityResponse getCategoryByUuid(UUID categoryId) {
        return categoryRepository.findByUuidAndIsDeleteFalse(categoryId)
                .map(CategoryEntityResponse::from)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    public CategoryEntityResponse getCategoryById(Long categoryId) {
        return categoryRepository.findByIdAndIsDeleteFalse(categoryId)
                .map(CategoryEntityResponse::from)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
