package com.example.md5api.repository;

import com.example.md5api.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends CrudRepository<Product, Long> {

}
