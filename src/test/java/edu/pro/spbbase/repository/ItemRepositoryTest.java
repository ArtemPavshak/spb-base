package edu.pro.spbbase.repository;

import edu.pro.spbbase.model.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/*
  @author   rakel
  @project   spb-base
  @class  ItemRepositoryTest
  @version  1.0.0 
  @since 19.04.2024 - 11.59
*/


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataMongoTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository underTest;

    @BeforeEach
    void setUp() {
         /*List<Item> items = List.of(
                new Item("1", "Maria", "000001", "$$testhtj"),
                new Item("2", "Artem", "000002", "test##"),
                new Item("3", "Iryna", "000003", "hj6##test*")
        );

         underTest.saveAll(items);*/
    }

    @AfterEach
    void tearDown() {
        List<Item> items = underTest.findAll()
                .stream()
                .filter(item -> item.getDescription().contains("test"))
                .toList();
         underTest.deleteAll(items);
    }

    @Test
    void itShouldCheckTheCollectionIsNotEmpty() {
        List<Item> itemsList = List.of(
                new Item("1", "Maria", "000001", "$$testhtj"),
                new Item("2", "Artem", "000002", "test##"),
                new Item("3", "Iryna", "000003", "hj6##test*")
        );

         underTest.saveAll(itemsList);
        assertFalse(underTest.findAll().isEmpty());
        List<Item> items = underTest.findAll()
                .stream()
                .filter(item -> item.getDescription().contains("test"))
                .toList();
        assertEquals(items.size(), 3);
    }

    @Test
    void itShouldSaveItem () {
        Item testItem = new Item("Sergeyhyjk", "0002", "testSaveItem");
        underTest.save(testItem);
        Item forTest = underTest.findAll()
                .stream()
                .filter(item -> item.getName().equals("Sergeyhyjk"))
                .filter(item -> item.getDescription().contains("test"))
                .findAny()
                .orElse(null);
        assertNotNull(forTest);
        assertNotNull(forTest.getId());
        assertFalse(forTest.getId().isEmpty());
        assertEquals(forTest.getCode(), "0002");
    }

    @Test
    void itShouldUpdateItemDescription() {
        Item testItem = new Item("Sergeykaa", "0003", "testUp");
        underTest.save(testItem);

        String itemId = testItem.getId();

        String updatedDescription = "Updated description";
        underTest.findById(itemId).ifPresent(item -> {
            item.setDescription(updatedDescription);
            underTest.save(item);
        });

        Optional<Item> updatedItem = underTest.findById(itemId);
        assertTrue(updatedItem.isPresent());
        assertEquals(updatedDescription, updatedItem.get().getDescription());
    }

    @Test
    void itShouldUpdateItemName() {
        Item testItem = new Item("Sergeykaa", "0003", "testUp");
        underTest.save(testItem);

        String itemId = testItem.getId();

        String updatedName = "Antoshka";
        underTest.findById(itemId).ifPresent(item -> {
            item.setName(updatedName);
            underTest.save(item);
        });

        Optional<Item> updatedItem = underTest.findById(itemId);
        assertTrue(updatedItem.isPresent());
        assertEquals(updatedName, updatedItem.get().getName());
    }

    @Test
    void itShouldUpdateItemCode() {
        Item testItem = new Item("Sergeykaa", "0003", "testUp");
        underTest.save(testItem);

        String itemId = testItem.getId();

        String updatedCode = "1205";
        underTest.findById(itemId).ifPresent(item -> {
            item.setCode(updatedCode);
            underTest.save(item);
        });

        Optional<Item> updatedItem = underTest.findById(itemId);
        assertTrue(updatedItem.isPresent());
        assertEquals(updatedCode, updatedItem.get().getCode());
    }

    @Test
    void itShouldDeleteItem() {
        Item testItem = new Item("Sergey23", "0002", "testDeleteItem");
        underTest.save(testItem);

        String itemId = testItem.getId();

        underTest.deleteById(itemId);

        assertFalse(underTest.findById(itemId).isPresent());
    }

    @Test
    void itShouldDeleteAllItems() {
        Item item1 = new Item("Sergey13", "0001", "testItem1");
        Item item2 = new Item("Anna", "0002", "testItem2");
        Item item3 = new Item("John", "0003", "testItem3");

        underTest.save(item1);
        underTest.save(item2);
        underTest.save(item3);

        underTest.deleteAll();

        assertTrue(underTest.findAll().isEmpty());
    }

    @Test
    void itShouldDeleteItemsByName() {
        Item item1 = new Item("Sergey", "0001", "testItem1");
        Item item2 = new Item("Anna", "0002", "testItem2");
        Item item3 = new Item("John", "0003", "testItem3");

        underTest.save(item1);
        underTest.save(item2);
        underTest.save(item3);

        List<Item> items = underTest.findAll();
        System.out.println(items);
        items.removeIf(item -> item.getName().equals("Sergey"));
        underTest.saveAll(items);
        System.out.println(items);
        assertFalse(items.stream().anyMatch(item -> item.getName().equals("Sergey")));
    }

    @Test
    void itShouldDeleteItemsByCode() {
        Item item1 = new Item("Sergey", "0001", "testItem1");
        Item item2 = new Item("Anna", "0002", "testItem2");
        Item item3 = new Item("John", "0003", "testItem3");

        underTest.save(item1);
        underTest.save(item2);
        underTest.save(item3);

        List<Item> items = underTest.findAll();
        System.out.println(items);
        items.removeIf(item -> item.getCode().equals("0002"));
        underTest.saveAll(items);
        System.out.println(items);
        assertFalse(items.stream().anyMatch(item -> item.getCode().equals("0002")));
    }

    @Test
    void itShouldDeleteItemsByDescription() {
        Item item1 = new Item("Sergey", "0001", "testItem1");
        Item item2 = new Item("Anna", "0002", "testItem2");
        Item item3 = new Item("John", "0003", "testItem3");

        underTest.save(item1);
        underTest.save(item2);
        underTest.save(item3);

        List<Item> items = underTest.findAll();
        System.out.println(items);
        items.removeIf(item -> item.getDescription().equals("testItem3"));
        underTest.saveAll(items);
        System.out.println(items);
        assertFalse(items.stream().anyMatch(item -> item.getDescription().equals("testItem3")));
    }

    @Test
    void itShouldCountItems() {
        underTest.deleteAll();
        Item item1 = new Item("Sergey", "0001", "testItem1");
        Item item2 = new Item("Anna", "0002", "testItem2");
        Item item3 = new Item("John", "0003", "testItem3");

        underTest.saveAll(List.of(item1, item2, item3));

        System.out.println(underTest.findAll());
        long itemCount = underTest.count();

        assertEquals(3, itemCount);
    }

    @Test
    void itShouldFindItemsByCodeLength() {
        Item item1 = new Item("Sergey", "0001", "testItem1");
        Item item2 = new Item("Anna", "0002", "testItem2");
        Item item3 = new Item("John", "0003", "testItem3");

        underTest.saveAll(List.of(item1, item2, item3));

        int codeLength = 4;

        List<Item> foundItems = underTest.findAll().stream()
                .filter(item -> item.getCode().length() == codeLength).toList();

        boolean allItemsHaveCorrectCodeLength = foundItems.size() == underTest.findAll().size();

        assertTrue(allItemsHaveCorrectCodeLength);
    }

}