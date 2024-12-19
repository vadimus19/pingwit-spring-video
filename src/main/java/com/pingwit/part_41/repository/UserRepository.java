package com.pingwit.part_41.repository;

import com.pingwit.part_41.entity.Clothes;
import com.pingwit.part_41.entity.Order;
import com.pingwit.part_41.entity.User;
import com.pingwit.part_41.service.DatabaseConnectorService;
import io.micrometer.common.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final String INSERT_USER = "INSERT INTO users VALUES (nextval('users_id_seq'), ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String FIND_ALL_BY_AGE_FROM_AND_PAYMENT_FROM = "SELECT * from users where age >= ? AND payment >= ?";
    private static final String FIND_BY_ID = "SELECT * from users where id = ?";
    private static final String FIND_ORDER_BY_USER_ID = """
            select o.id id, o.address address, o.email email, c.id clothes_id, c.article article, c.size size, c.quantity quantity, c.description description
            	from orders o
            	left join clothes c
            	on o.clothes_id = c.id
            where o.user_id  = ?;
            """;

    private final DatabaseConnectorService connectorService;

    public UserRepository(DatabaseConnectorService connectorService) {
        this.connectorService = connectorService;
    }

    public void insert(List<User> users) {
        try (Connection connection = connectorService.getConnection();
             PreparedStatement prs = connection.prepareStatement(INSERT_USER)
        ) {
            for (User user : users) {
                prepareUser(user, prs);
                prs.addBatch();
                prs.clearParameters();
            }

            prs.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(User user) {
        try (Connection connection = connectorService.getConnection();
             PreparedStatement prs = connection.prepareStatement(INSERT_USER)
        ) {
            prepareUser(user, prs);

            prs.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(User user, Boolean autoCommit) {
        try (Connection connection = connectorService.getConnection();
             PreparedStatement prs = connection.prepareStatement(INSERT_USER)
        ) {
            connection.setAutoCommit(autoCommit);

            prepareUser(user, prs);

            prs.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findByIdSqlInjectionVulnerability(String id) {
        try (Connection connection = connectorService.getConnection();
             Statement statement = connection.createStatement()
        ) {
            ResultSet rs = statement.executeQuery("SELECT * from users where id = '" + id + "'");

            List<User> singleUser = getFromResultSet(rs);

            if (CollectionUtils.isNotEmpty(singleUser)) {
                return singleUser.get(0);
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findById(Long id) {
        try (Connection connection = connectorService.getConnection();
             PreparedStatement prs = connection.prepareStatement(FIND_BY_ID)
        ) {
            prs.setLong(1, id);

            ResultSet rs = prs.executeQuery();

            List<User> singleUser = getFromResultSet(rs);

            if (CollectionUtils.isNotEmpty(singleUser)) {
                PreparedStatement orderPrs = connection.prepareStatement(FIND_ORDER_BY_USER_ID);
                orderPrs.setLong(1, id);
                ResultSet orderRs = orderPrs.executeQuery();

                while (orderRs.next()) {
                    Order order = new Order();
                    order.setId(orderRs.getLong("id"));
                    order.setUser(singleUser.get(0));
                    order.setAddress(orderRs.getString("address"));
                    order.setEmail("email");

                    Clothes clothes = new Clothes();
                    clothes.setId(orderRs.getLong("clothes_id"));
                    clothes.setArticle(orderRs.getString("article"));
                    clothes.setSize(orderRs.getString("size"));
                    clothes.setQuantity(orderRs.getInt("quantity"));
                    clothes.setDescription(orderRs.getString("description"));
                    order.setClothes(clothes);

                    singleUser.get(0).getOrders().add(order);
                }

                return singleUser.get(0);
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> findAll() {
        try (Connection connection = connectorService.getConnection();
             Statement statement = connection.createStatement()
        ) {
            ResultSet rs = statement.executeQuery("SELECT * FROM users order by id");

            return getFromResultSet(rs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> findAllByAgeAndPaymentFrom(Integer age, Double payment) {
        try (Connection connection = connectorService.getConnection();
             PreparedStatement prs = connection.prepareStatement(FIND_ALL_BY_AGE_FROM_AND_PAYMENT_FROM)
        ) {
            prs.setInt(1, age);
            prs.setDouble(2, payment);

            ResultSet rs = prs.executeQuery();

            return getFromResultSet(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<User> getFromResultSet(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();

        while (rs.next()) {
            User user = new User();

            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setAge(rs.getInt("age"));
            user.setRating(rs.getDouble("rating"));
            user.setPayment(rs.getDouble("payment"));
            user.setActive(rs.getBoolean("active"));
            user.setCountry(rs.getString("country"));
            user.setCurrency(rs.getString("currency"));
            user.setBio(rs.getString("bio"));

            users.add(user);
        }

        return users;
    }

    private void prepareUser(User user, PreparedStatement prs) throws SQLException {
        prs.setString(1, user.getName());
        prs.setInt(2, user.getAge());
        prs.setDouble(3, user.getRating());
        prs.setDouble(4, user.getPayment());
        prs.setBoolean(5, user.getActive());
        prs.setString(6, user.getCountry());
        prs.setString(7, user.getCurrency());
        prs.setString(8, user.getBio());
    }
}
