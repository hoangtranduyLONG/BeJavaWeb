package com.example.md5api.validator;

import com.example.md5api.model.Product;
import com.example.md5api.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductValidator implements Validator {
    @Autowired
    private IProductService productService;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Product product = (Product) target;
        if (product.getName().length() < 5) {
            errors.rejectValue("name", "name.length", "Tên phải dài ít nhất 5 ký tự");
        } else if (product.getName() == null) {
            errors.rejectValue("name", "name.null", "Tên không được để trống");
        }
        if (product.getPrice() == null) {
            errors.rejectValue("price", "price.null", "Phải nhập giá");
        }
        if (product.getDescription() == null ||product.getDescription().equals("")) {
            errors.rejectValue("description", "description.null", "Mô tả không được để trống");
        }
        if (product.getCategory() == null) {
            errors.rejectValue("category", "category.null", "Hãy chọn phân loại hàng");
        }
    }
}
