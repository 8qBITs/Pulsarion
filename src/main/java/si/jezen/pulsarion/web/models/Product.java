package si.jezen.pulsarion.web.models;

import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Product {
    @Getter private int id;
    @Getter private int category;
    @Getter private String name;
    @Getter private String description;
    @Getter private double price;
    @Getter private String command;
    @Getter private boolean featured;
    @Getter private String imageUrl;
    public Product(int id, int category, String name, String description, double price, String command, boolean featured, String imageUrl) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
        this.command = command;
        this.featured = featured;
        this.imageUrl = imageUrl;
    }

    public static List<Product> getProducts(Connection conn) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, category, name, description, price, command, featured, imageUrl FROM products"; // Include command in SELECT
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getInt("category"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("command"),
                        rs.getBoolean("featured"),
                        rs.getString("imageUrl")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return products;
    }

    public static List<Product> getProductsFromCategory(Connection conn, int categoryId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, category, name, description, price, command, featured, imageUrl FROM products WHERE category = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, categoryId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getInt("category"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("command"),
                        rs.getBoolean("featured"),
                        rs.getString("imageUrl")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching products by category: " + e.getMessage());
        }
        return products;
    }

    public static Product getProductById(Connection conn, int productId) {
        String sql = "SELECT id, category, name, description, price, command, featured, imageUrl FROM products WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Product(
                        rs.getInt("id"),
                        rs.getInt("category"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("command"),
                        rs.getBoolean("featured"),
                        rs.getString("imageUrl")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching product: " + e.getMessage());
        }
        return null; // Consider throwing an exception if the product is not found
    }

    public static List<Product> getFeaturedProducts(Connection conn) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, category, name, description, price, command, featured, imageUrl FROM products WHERE featured = TRUE";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getInt("category"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("command"),
                        rs.getBoolean("featured"),
                        rs.getString("imageUrl")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching featured products: " + e.getMessage());
        }
        return products;
    }

    public static void addProduct(Connection conn, int category, String name, String description, double price, String command, Boolean featured, String imageUrl) {
        String sql = "INSERT INTO products (category, name, description, price, command, featured, imageUrl) VALUES (?, ?, ?, ?, ?, ?, ?)"; // Include command in INSERT
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, category);
            pstmt.setString(2, name);
            pstmt.setString(3, description);
            pstmt.setDouble(4, price);
            pstmt.setString(5, command);
            pstmt.setBoolean(6, featured);
            pstmt.setString(7, imageUrl);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    public static void updateProduct(Connection conn, int id, int category, String name, String description, double price, String command, Boolean featured, String imageUrl) {
        String sql = "UPDATE products SET category = ?, name = ?, description = ?, price = ?, command = ?, featured = ?, imageUrl = ? WHERE id = ?"; // Include command in UPDATE
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, category);
            pstmt.setString(2, name);
            pstmt.setString(3, description);
            pstmt.setDouble(4, price);
            pstmt.setString(5, command); // Set command
            pstmt.setBoolean(6, featured);
            pstmt.setString(7, imageUrl);
            pstmt.setInt(8, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    public static void deleteProductById(Connection conn, int productId) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }

    // Additional methods and logic as needed
}
