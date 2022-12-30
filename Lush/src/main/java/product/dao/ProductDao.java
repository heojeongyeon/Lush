package product.dao;

import product.domian.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ProductDao {

    public int getProductCategoriesRecord(Connection conn, int categoriesID) throws SQLException;

    public ArrayList<Product> productList(Connection conn, ArrayList<Integer> categoriesID, int selectStatus, int currentPage, int numberPerPage) throws SQLException;
}