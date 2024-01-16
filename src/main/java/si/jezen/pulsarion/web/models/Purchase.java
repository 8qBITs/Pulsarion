package si.jezen.pulsarion.web.models;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import si.jezen.pulsarion.Pulsarion;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Purchase {
    @Getter
    private int id;
    @Getter
    private String email;
    @Getter
    private int product;
    @Getter
    private String username;
    @Getter
    private double price;
    @Getter
    private Timestamp timestamp;

    public Purchase(int id, String email, int product, String username, double price, Timestamp timestamp) {
        this.id = id;
        this.email = email;
        this.product = product;
        this.username = username;
        this.price = price;
        this.timestamp = timestamp;
    }

    public static List<Purchase> getPurchases(Connection conn) {
        List<Purchase> purchases = new ArrayList<>();
        String sql = "SELECT id, email, product, username, price, timestamp FROM purchases";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Purchase purchase = new Purchase(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getInt("product"),
                        rs.getString("username"),
                        rs.getDouble("price"),
                        rs.getTimestamp("timestamp")
                );
                purchases.add(purchase);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return purchases;
    }

    public static Purchase getPurchaseById(Connection conn, int purchaseId) {
        Purchase purchase = null;
        String sql = "SELECT id, email, product, username, price, timestamp FROM purchases WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, purchaseId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    purchase = new Purchase(
                            rs.getInt("id"),
                            rs.getString("email"),
                            rs.getInt("product"),
                            rs.getString("username"),
                            rs.getDouble("price"),
                            rs.getTimestamp("timestamp")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching purchase by ID: " + e.getMessage());
        }
        return purchase;
    }

    public static Purchase getPurchaseByValidation(Connection conn, String validation) {
        Purchase purchase = null;
        String sql = "SELECT id, email, product, username, price, timestamp FROM purchases WHERE validation = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, validation);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    purchase = new Purchase(
                            rs.getInt("id"),
                            rs.getString("email"),
                            rs.getInt("product"),
                            rs.getString("username"),
                            rs.getDouble("price"),
                            rs.getTimestamp("timestamp")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching purchase by validation string: " + e.getMessage());
        }
        return purchase;
    }

    public static List<Purchase> getLastFivePurchases(Connection conn) {
        List<Purchase> lastFivePurchases = new ArrayList<>();
        String sql = "SELECT id, email, product, username, price, timestamp FROM purchases ORDER BY timestamp DESC LIMIT 5";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Purchase purchase = new Purchase(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getInt("product"),
                        rs.getString("username"),
                        rs.getDouble("price"),
                        rs.getTimestamp("timestamp")
                );
                lastFivePurchases.add(purchase);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching last five purchases: " + e.getMessage());
        }
        return lastFivePurchases;
    }

    public static void addPurchase(Connection conn, String email, int product, String username, double price, String validation, Timestamp timestamp) {
        String sql = "INSERT INTO purchases (email, product, username, price, validation, timestamp) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setInt(2, product);
            pstmt.setString(3, username);
            pstmt.setDouble(4, price);
            pstmt.setString(5, validation); // Set the validation string

            // If timestamp is null, use the current datetime
            if (timestamp == null) {
                pstmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            } else {
                pstmt.setTimestamp(6, timestamp);
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding purchase: " + e.getMessage());
        }
    }

    public static double getTotalEarnings(Connection conn) {
        String sql = "SELECT SUM(price) AS total FROM purchases";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating total earnings: " + e.getMessage());
        }
        return 0.0;
    }

    public static int getPurchaseCount(Connection conn) {
        String sql = "SELECT COUNT(*) AS count FROM purchases";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating purchase count: " + e.getMessage());
        }
        return 0;
    }

    public static double getMonthlyTotal(Connection conn) {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();
        // Calculate the start timestamp (last day of previous month at 12 PM)
        LocalDateTime startTimestamp = now.minusMonths(1).withDayOfMonth(1).withHour(12).withMinute(0).withSecond(0).withNano(0).minusDays(1);
        // Calculate the end timestamp (end of this month at 12 PM)
        LocalDateTime endTimestamp = now.withDayOfMonth(1).plusMonths(1).withHour(12).withMinute(0).withSecond(0).withNano(0).minusDays(1);

        String sql = "SELECT SUM(price) AS total FROM purchases WHERE timestamp >= ? AND timestamp < ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set the start and end timestamps
            pstmt.setTimestamp(1, Timestamp.valueOf(startTimestamp));
            pstmt.setTimestamp(2, Timestamp.valueOf(endTimestamp));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error calculating total earnings for current month: " + e.getMessage());
        }
        return 0.0;
    }

    public static void completePurchase(Connection conn, String validation, Pulsarion instance) {
        Purchase purchase = Purchase.getPurchaseByValidation(conn, validation);
        Product product = Product.getProductById(conn, purchase.getProduct());
        String command = product.getCommand().replace("{player}", purchase.getUsername());

        new BukkitRunnable() {
            @Override
            public void run() {
                instance.getServer().dispatchCommand(instance.getServer().getConsoleSender(), command);
            }
        }.runTask(instance);
    }

    public static void completePurchaseAdmin(Connection conn, int id, Pulsarion instance) {
        Purchase purchase = Purchase.getPurchaseById(conn, id);
        Product product = Product.getProductById(conn, purchase.getProduct());
        String command = product.getCommand().replace("{player}", purchase.getUsername());


        new BukkitRunnable() {
            @Override
            public void run() {
                instance.getServer().dispatchCommand(instance.getServer().getConsoleSender(), command);
            }
        }.runTask(instance);

    }

}