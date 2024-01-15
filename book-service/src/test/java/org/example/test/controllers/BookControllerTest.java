package org.example.test.controllers;

import com.redis.testcontainers.RedisContainer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.validation.constraints.Negative;
import org.example.model.Book;
import org.example.model.Category;
import org.example.model.dto.BookDto;
import org.example.repository.BookRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class BookControllerTest {

    @LocalServerPort
    private Integer port;
   @Autowired
    private BookRepository bookRepository;
    @Container
    private static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(DockerImageName.parse("redis:latest"))
                    .withExposedPorts(6379)
                    .withReuse(true);

    @BeforeAll
    static void beforeAll() {
        REDIS_CONTAINER.start();
    }

    @AfterAll
    static void afterAll() {
        REDIS_CONTAINER.stop();
    }
    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }



    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.redis.port", REDIS_CONTAINER::getFirstMappedPort);
    }


    @Test
    @Order(1)
    public void shouldCreateNews() {

        BookDto bookDto = new BookDto();
                bookDto.setId(null);
                bookDto.setTitle("Title 1");
                bookDto.setAuthor("Author 1");
                bookDto.setCategory("Category 1");



        given()
                .contentType(ContentType.JSON)
                .with()
                .body(bookDto)
                .when()
                .post(ApiCollection.CREATE_BOOK)
                .then()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("title", hasToString("Title 1"))
                .body("author", hasToString("Author 1"))
                .body("category", hasToString("Category 1"));

    }


    @Test
    @Order(2)
    public void whenUpdateBookReturnBook() {

        Book book = bookRepository.getAll().get(0);


        BookDto bookDto = new BookDto();
                bookDto.setId(book.getId());
                bookDto.setTitle("UpdatedTitle");
                bookDto.setAuthor(book.getAuthor());
                bookDto.setCategory(book.getStringCategory());


        given()
                .contentType(ContentType.JSON)
                .with()
                .body(bookDto)
                .when()
                .put(ApiCollection.UPDATE_BOOK)
                .then()
                .statusCode(200)
                .body("id", hasToString(book.getId()))
                .body("title", hasToString("UpdatedTitle"))
                .body("author", equalTo(book.getAuthor()))
                .body("category", equalTo(book.getStringCategory()));

    }

    @Test
    @Order(3)
    public void whenFindBookByAuthorAndTitleThenReturnBook() {

        Book book = bookRepository.getAll().get(0);

        given()
                .contentType(ContentType.JSON)
                .pathParam("author", book.getAuthor())
                .pathParam("title", book.getTitle())
                .when()
                .get(ApiCollection.FIND_BOOK_BY_AUTHOR_AND_TITLE)
                .then()
                .statusCode(200)
                .body("id", equalTo(book.getId()))
        .body("author", equalTo(book.getAuthor()))
                .body("title", equalTo(book.getTitle()));
    }

    @Test
    @Order(4)
    public void whenFindBooksByCategoryThenReturnListBook() {

        Book book = new Book();
        book.setAuthor("Author 2");
        book.setTitle("Title 2");
        book.setCategory(new Category("Category 1"));

        bookRepository.save(book);

        List<Book> listBooks = bookRepository.getAll();

        given()
                .contentType(ContentType.JSON)
                .pathParam("category", book.getStringCategory())
                .when()
                .get(ApiCollection.FIND_BOOKS_BY_CATEGORY)
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
    }

    @Test
    @Order(5)
    public void whenDeleteBookThenReturnOk() {

        Book book = bookRepository.getAll().get(0);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", book.getId())
                .when()
                .delete(ApiCollection.DELETE_BOOK)
                .then()
                .statusCode(200)
                .body(is("Book is deleted successfully"));
    }


    @Negative
    @Test
    @Order(6)
    public void whenCreateNewsWithEmptyFieldThenReturnError() {

        BookDto bookDto = new BookDto();
                bookDto.setId(null);
                bookDto.setAuthor("Author 3");
                bookDto.setTitle("");
                bookDto.setCategory("Category 1");


        given()
                .contentType(ContentType.JSON)
                .with()
                .body(bookDto)
                .when()
                .post(ApiCollection.CREATE_BOOK)
                .then()
                .statusCode(200)
                .body("message", hasToString("[Field Title must not be blank]"));
    }

    @Negative
    @Test
    @Order(7)
    public void whenUpdateNewsWithFalseUserIdThenReturnError() {

        Book book = bookRepository.getAll().get(0);


        BookDto bookDto = new BookDto();
                bookDto.setId("error");
                bookDto.setAuthor("Author 33");
                bookDto.setTitle("Title 33");
                bookDto.setCategory("Category 1");

        given()
                .contentType(ContentType.JSON)
                .with()
                .body(bookDto)
                .when()
                .put(ApiCollection.UPDATE_BOOK)
                .then()
                .statusCode(200)
                .body("message", hasToString("Объект не найден"));

    }

}
