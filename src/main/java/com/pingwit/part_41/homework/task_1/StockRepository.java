package com.pingwit.part_41.homework.task_1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockRepository {
    private final StockDatabaseConnectorService connectorService;

    public StockRepository(StockDatabaseConnectorService connectorService) {
        this.connectorService = connectorService;
    }

    public void createStocksTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS stocks (" +
                "id SERIAL PRIMARY KEY, " +
                "ticker VARCHAR(5) NOT NULL, " +
                "company_name VARCHAR(100) NOT NULL, " +
                "location VARCHAR(100), " +
                "last_price NUMERIC(10, 2), " +
                "update_date_time TIMESTAMP NOT NULL" +
                ");";

        try (Connection conn = connectorService.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertSampleData() {
        String checkDataSQL = "SELECT COUNT(*) FROM stocks";
        String insertSQL = "INSERT INTO stocks (ticker, company_name, location, last_price, update_date_time) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connectorService.getConnection();
             Statement checkStmt = conn.createStatement();
             ResultSet rs = checkStmt.executeQuery(checkDataSQL)) {

            rs.next();
            if (rs.getInt(1) > 0) {
                return;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

                pstmt.setString(1, "AAPL");
                pstmt.setString(2, "Apple Inc.");
                pstmt.setString(3, "Cupertino, CA");
                pstmt.setDouble(4, 175.64);
                pstmt.setTimestamp(5, Timestamp.valueOf("2023-12-01 10:00:00"));
                pstmt.executeUpdate();

                for (int i = 6; i <= 20; i++) {
                    pstmt.setString(1, "TICK" + i);
                    pstmt.setString(2, "Company " + i);
                    pstmt.setString(3, "Location " + i);
                    pstmt.setDouble(4, i * 100.0);
                    pstmt.setTimestamp(5, Timestamp.valueOf("2023-12-06 15:00:00"));
                    pstmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getStocksByPage(int pageNumber, int pageSize) {
        String query = "SELECT ticker, company_name, location, last_price, update_date_time " +
                "FROM stocks ORDER BY id LIMIT ? OFFSET ?";

        List<String> stocks = new ArrayList<>();

        try (Connection conn = connectorService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, (pageNumber - 1) * pageSize);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String stock = String.format("Ticker: %s, Company: %s, Location: %s, Price: %.2f, Updated: %s",
                        rs.getString("ticker"),
                        rs.getString("company_name"),
                        rs.getString("location"),
                        rs.getDouble("last_price"),
                        rs.getTimestamp("update_date_time"));
                stocks.add(stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stocks;
    }

    public List<String> getTop3ExpensiveStocksWithinWeek() {
        String query = "SELECT ticker, company_name, last_price, update_date_time " +
                "FROM stocks " +
                "WHERE update_date_time >= NOW() - INTERVAL '7 days' " +
                "ORDER BY last_price DESC " +
                "LIMIT 3";

        List<String> stocks = new ArrayList<>();

        try (Connection conn = connectorService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String stock = String.format("Ticker: %s, Company: %s, Price: %.2f, Updated: %s",
                        rs.getString("ticker"),
                        rs.getString("company_name"),
                        rs.getDouble("last_price"),
                        rs.getTimestamp("update_date_time"));
                stocks.add(stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stocks;
    }
}

