package com.yohanii.lostandfound.component.crud.repository;

import com.yohanii.lostandfound.component.crud.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
