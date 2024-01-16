package si.jezen.pulsarion.web.models;

import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Coupon {
    @Getter
    private int id;
    @Getter
    private int percentage;
    @Getter
    private boolean enabled;
    @Getter
    private int maxUses;
    @Getter
    private int currentUses;

    public Coupon(int id, int percentage, boolean enabled, int maxUses, int currentUses) {
        this.id = id;
        this.percentage = percentage;
        this.enabled = enabled;
        this.maxUses = maxUses;
        this.currentUses = currentUses;
    }

    public static List<Coupon> getCoupons(Connection conn) {
        List<Coupon> coupons = new ArrayList<>();
        String sql = "SELECT id, percentage, enabled, max_uses, current_uses FROM coupons";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Coupon coupon = new Coupon(
                        rs.getInt("id"),
                        rs.getInt("percentage"),
                        rs.getBoolean("enabled"),
                        rs.getInt("max_uses"),
                        rs.getInt("current_uses")
                );
                coupons.add(coupon);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return coupons;
    }

    public static Coupon getCouponById(Connection conn, int couponId) {
        String sql = "SELECT id, percentage, enabled, max_uses, current_uses FROM coupons WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, couponId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Coupon(
                        rs.getInt("id"),
                        rs.getInt("percentage"),
                        rs.getBoolean("enabled"),
                        rs.getInt("max_uses"),
                        rs.getInt("current_uses")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error getting coupon: " + e.getMessage());
        }
        return null;
    }

    public static void addCoupon(Connection conn, int percentage, boolean enabled, int maxUses) {
        String sql = "INSERT INTO coupons (percentage, enabled, max_uses, current_uses) VALUES (?, ?, ?, 0)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, percentage);
            pstmt.setBoolean(2, enabled);
            pstmt.setInt(3, maxUses);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding coupon: " + e.getMessage());
        }
    }

    public static void updateCoupon(Connection conn, int id, int percentage, boolean enabled, int maxUses) {
        String sql = "UPDATE coupons SET percentage = ?, enabled = ?, max_uses = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, percentage);
            pstmt.setBoolean(2, enabled);
            pstmt.setInt(3, maxUses);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating coupon: " + e.getMessage());
        }
    }

    public static void deleteCoupon(Connection conn, int couponId) {
        String sql = "DELETE FROM coupons WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, couponId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting coupon: " + e.getMessage());
        }
    }

}
