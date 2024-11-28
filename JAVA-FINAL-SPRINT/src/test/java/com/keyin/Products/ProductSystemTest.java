package com.keyin.Products;

import com.keyin.User.User;
import com.keyin.User.UserDAO;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductSystemTest {
    private static ProductDAO productDAO;
    private static ProductService productService;
    private static UserDAO userDAO;
    private static User testSeller;
    private Product testProduct;

    @BeforeAll
    static void setUp() throws SQLException {
        productDAO = new ProductDAO();
        productService = new ProductService();
        userDAO = new UserDAO();

        // Create a test seller
        testSeller = new User("testSeller", "password", "seller@test.com", "SELLER");
        testSeller = userDAO.createUser(testSeller);
    }

    @BeforeEach
    void setUpTestProduct() {
        testProduct = new Product(
                "Test Product",
                "Test Description",
                99.99,
                10,
                testSeller.getUser_id()  // Use the actual seller ID
        );
    }

    @Test
    @DisplayName("Test Product Creation")
    void testProductCreation() {
        assertEquals("Test Product", testProduct.getName());
        assertEquals("Test Description", testProduct.getDescription());
        assertEquals(99.99, testProduct.getPrice());
        assertEquals(10, testProduct.getQuantity());
        assertEquals(testSeller.getUser_id(), testProduct.getSeller_id());
    }

    @Test
    @DisplayName("Test Product Price Validation")
    void testProductPriceValidation() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Product("Test", "Description", -10.0, 5, testSeller.getUser_id());
        });
    }

    @Test
    @DisplayName("Test Product Quantity Validation")
    void testProductQuantityValidation() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Product("Test", "Description", 10.0, -5, testSeller.getUser_id());
        });
    }

    @Test
    @DisplayName("Test Database Operations")
    void testDatabaseOperations() throws SQLException {
        // Test creating product
        Product savedProduct = productDAO.createProduct(testProduct);
        assertNotNull(savedProduct);
        assertTrue(savedProduct.getProduct_id() > 0);

        // Test retrieving product
        Product retrievedProduct = productDAO.getProductById(savedProduct.getProduct_id());
        assertNotNull(retrievedProduct);
        assertEquals(testProduct.getName(), retrievedProduct.getName());

        // Clean up
        productDAO.deleteProduct(savedProduct.getProduct_id());
    }

    @Test
    @DisplayName("Test Product Search")
    void testProductSearch() throws SQLException {
        // Create test products
        Product product1 = new Product("Laptop Dell", "A great laptop", 999.99, 5, testSeller.getUser_id());
        Product product2 = new Product("Laptop HP", "Another great laptop", 899.99, 3, testSeller.getUser_id());

        productDAO.createProduct(product1);
        productDAO.createProduct(product2);

        // Test search
        List<Product> results = productDAO.searchProducts("Laptop");
        assertFalse(results.isEmpty());

        // Clean up
        productDAO.deleteProduct(product1.getProduct_id());
        productDAO.deleteProduct(product2.getProduct_id());
    }

    @AfterAll
    static void tearDown() throws SQLException {
        // Clean up test data
        if (testSeller != null) {
            userDAO.deleteUser(testSeller.getUser_id());
        }
    }
}