package edu.pro.spbbase.service;

import edu.pro.spbbase.model.Item;
import edu.pro.spbbase.model.ItemEditRequest;
import edu.pro.spbbase.model.ItemInsertRequest;
import edu.pro.spbbase.repository.ItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/*
  @author   rakel
  @project   spb-base
  @class  ItemServiceTest
  @version  1.0.0 
  @since 25.04.2024 - 19.06
*/class ItemServiceTest {

    @Mock
    private ItemRepository mockRepository;
    private ItemService underTest;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new ItemService(mockRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void itShouldSaveItem() {
        //given
        String code = "000000";
        ItemInsertRequest request = new ItemInsertRequest("Artem", code, "smth");
        given(mockRepository.existsItemByCode(code)).willReturn(false);

        //when
        underTest.create(request);

        //then

        verify(mockRepository).save(any());
        verify(mockRepository).save(any(Item.class));
        verify(mockRepository, times(1)).existsItemByCode(code);
        verify(mockRepository, times(1)).save(any(Item.class));
    }
    @Test
    void itShouldNotSaveItemWhenCodePresentInDb() {
        //given
        String code = "000000";
        ItemInsertRequest request = new ItemInsertRequest("Artem", code, "smth");
        given(mockRepository.existsItemByCode(code)).willReturn(true);

        //when
        underTest.create(request);

        //then
        verify(mockRepository, never()).save(any());
        verify(mockRepository, times(1)).existsItemByCode(code);
        verify(mockRepository, times(0)).save(any());
    }

    @Test
    void itShouldNotSaveItemWhenCodeLengthIsNot6() {
        //given
        String invalidCode = "1234567";
        ItemEditRequest request = new ItemEditRequest("1", "Artem", invalidCode, "smth");
        given(mockRepository.existsItemByCode(eq(invalidCode))).willReturn(false);

        //when
        underTest.update(request);

        //then
        verify(mockRepository, never()).save(any());
        verify(mockRepository, never()).existsItemByCode(anyString());
    }


}