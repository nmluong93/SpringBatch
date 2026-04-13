package com.springbatch.simplespringbatch.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements org.springframework.jdbc.core.RowMapper<Product> {
    private static final Logger log = LoggerFactory.getLogger(ProductRowMapper.class);

    /**
     * Implementations must implement this method to map each row of data in the
     * {@code ResultSet}. This method should not call {@code next()} on the
     * {@code ResultSet}; it is only supposed to map values of the current row.
     *
     * @param rs     the {@code ResultSet} to map (pre-initialized for the current row)
     * @param rowNum the number of the current row
     * @return the result object for the current row (may be {@code null})
     * @throws SQLException if an SQLException is encountered while getting
     *                      column values (that is, there's no need to catch SQLException)
     */
    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.info("Row processed: {} \n ResultSet: {}", rowNum, rs);
        return new Product(rs.getInt("product_id"), rs.getString("product_name"), rs.getString("product_category"), rs.getInt("product_price"));

    }
}
