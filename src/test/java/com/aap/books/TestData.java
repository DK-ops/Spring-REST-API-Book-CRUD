package com.aap.books;

import com.aap.books.domain.Book;
import com.aap.books.domain.BookEntity;
import org.junit.jupiter.api.Test;

public final class TestData {
    private TestData(){

    }

    public static Book testBook(){

        return Book.builder()
                .isbn("666")
                .title("Come With Me")
                .author("Harry")
                .build();
    }
    public static BookEntity testBookEntity(){
        return BookEntity.builder()
                .isbn("666")
                .title("Come With Me")
                .author("Harry")
                .build();
    }
}
