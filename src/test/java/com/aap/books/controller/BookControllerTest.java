package com.aap.books.controller;

import com.aap.books.TestData;
import com.aap.books.domain.Book;
import com.aap.books.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Test
    void testThatBookIsCreatedReturnsHttp200() throws Exception {
        final Book book = TestData.testBook();
        final ObjectMapper objectMapper = new ObjectMapper();
        final String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/books/"+ book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()));

    }
    @Test
    void testThatBookIsUpdatedReturnsHttp201() throws Exception {
        final Book book = TestData.testBook();
        bookService.save(book);

        book.setAuthor("Fiersa Kecili");
        book.setTitle("Masa KecilMu");

        final ObjectMapper objectMapper = new ObjectMapper();
        final String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/books/"+ book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()));

    }

    @Test
    void testThatRetrieveBookReturnWhenBookNotFound() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/books/965969"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testThatRetrieveBookReturnsHttp200WhenBookExists() throws Exception{
        final Book book = TestData.testBook();
        bookService.save(book);

        mockMvc.perform(MockMvcRequestBuilders.get("/books/" + book.getIsbn()))
                .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()));
    }

    @Test
    void testThatListBooksReturnHttp200EmptyListWhenNoBooksExist() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/books" ))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testReturnHttp200WhenBookExist() throws Exception{
        final Book book = TestData.testBook();
        bookService.save(book);
        mockMvc.perform(MockMvcRequestBuilders.get("/books" ))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isbn").value(book.getIsbn()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value(book.getAuthor()));
    }

    @Test
    void testHttp204KetikaBukuTidakAda() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/4523912939" ))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testHttp204IsReturnWhenExistingBookIsDeleted()throws Exception {
        final Book book = TestData.testBook();
        bookService.save(book);

        mockMvc.perform(MockMvcRequestBuilders.delete("/books/"+book.getIsbn() ))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}