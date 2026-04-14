package com.springbatch.simplespringbatch.domain;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductItemPrepareStatementSetter implements org.springframework.batch.infrastructure.item.database.ItemPreparedStatementSetter<Product> {
    /**
     * Set parameter values on the given PreparedStatement as determined from the provided
     * item.
     *
     * @param item the item to obtain the values from
     * @param ps   the PreparedStatement to invoke setter methods on
     * @throws SQLException if a SQLException is encountered (i.e. there is no need to
     *                      catch SQLException)
     */
    @Override
    public void setValues(Product item, PreparedStatement ps) throws SQLException {
        ps.setInt(1, item.getProductId());
        ps.setString(2, item.getProductName());
        ps.setString(3, item.getProductCategory());
        ps.setInt(4, item.getProductPrice());
    }
}
