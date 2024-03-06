package com.yohanii.lostandfound.domain.item;

import com.yohanii.lostandfound.domain.post.Post;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    void save() {
        Post post = Post.builder().build();
        Item item = Item.builder()
                .post(post)
                .name("testItem")
                .place("testPlace")
                .itemCategory(ItemCategory.SMART_PHONE)
                .build();

        Long savedId = itemRepository.save(item);

        Item findItem = itemRepository.find(savedId);
        assertThat(findItem.getPost()).isEqualTo(post);
        assertThat(findItem.getName()).isEqualTo(item.getName());
        assertThat(findItem.getPlace()).isEqualTo(item.getPlace());
        assertThat(findItem.getItemCategory()).isEqualTo(item.getItemCategory());
    }

    @Test
    void find() {
        Item item = Item.builder().build();
        itemRepository.save(item);

        Item findItem = itemRepository.find(item.getId());
        assertThat(findItem).isEqualTo(item);
    }

    @Test
    void findAll() {
        Item item1 = Item.builder().build();
        Item item2 = Item.builder().build();
        itemRepository.save(item1);
        itemRepository.save(item2);

        List<Item> items = itemRepository.findAll();
        assertThat(items).contains(item1, item2);
        assertThat(items.size()).isEqualTo(2);
    }
}