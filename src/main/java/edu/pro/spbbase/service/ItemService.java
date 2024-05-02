package edu.pro.spbbase.service;

/*
  @author   george
  @project   spb-base
  @class  ItemService
  @version  1.0.0 
  @since 11.02.24 - 12.28
*/

import edu.pro.spbbase.model.Item;
import edu.pro.spbbase.model.ItemEditRequest;
import edu.pro.spbbase.model.ItemInsertRequest;
import edu.pro.spbbase.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j

public class ItemService {

    private final ItemRepository repository;

    private List<Item> items = List.of(
            new Item("4", "name1", "000001", "description1"),
            new Item("5", "name2", "000002", "description2"),
            new Item("6", "name3", "000003", "description3")

    );

    @PostConstruct
   public void init() {
        repository.deleteAll();
        repository.saveAll(items);
    }

    // CRUD


    public List<Item> getAll() {
        log.info(" -----------  GET ALL ------------------");
        return repository.findAll();
    }


    public Item getById(String id) {
        log.info(" request for id = " + id);
        return repository.findById(id).orElse(null);
    }


    public Item create(Item item) {
        log.info(" request for creation = " + item);
        return repository.save(item);
    }
    public Item update(Item item) {
        log.info(" request for creation = " + item);
        return repository.save(item);
    }


    public void delete(String id) {
        log.info(" delete id = " + id);
        repository.deleteById(id);
    }

    public Item create(ItemInsertRequest request) {
        if (repository.existsItemByCode(request.code())) {
            return null;
        }
        if (request.name() == null || request.name().isEmpty()) {
            return null;
        }
        if (request.code().length() != 6){
            return null;
        }
        if (request.description().contains("###test")){
            return null;
        }
        Item itemToPersist = new Item(request.name(), request.code(), request.description());
        return this.create(itemToPersist);
    }

    public Item update(ItemEditRequest request) {
        if(request.id().isEmpty()) {
            return null;
        }
        if(request.name().isEmpty()) {
            return null;
        }
        if(request.code().isEmpty()) {
            return null;
        }
        if(request.description().isEmpty()) {
            return null;
        }

        if (request.code().length() != 6){
            return null;
        }

        Item itemToPersist = new Item(request.name(), request.code(), request.description());
        return this.update(itemToPersist);
    }

}
