package article.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.naming.NamingException;

import com.util.ConnectionProvider;
import com.util.JdbcUtil;
import article.dao.ArticleDaoImpl;
import article.domain.Article;

// 싱글톤
public class ArticleListService {
	
	private static ArticleListService  instance = null;
	   
	   private ArticleListService() {}
	   
	   public static ArticleListService getInstance() {
	      if(   instance == null ) {
	         instance = new ArticleListService();
	      }
	      return instance;
	   }

	   // 한 페이지에 출력할 방명록 수 
	   private static final int ARTICLE_COUNT_PER_PAGE = 6;
	   
	   // MessageListView [방명록 목록 + 페이징 처리] 반환하는 메서드
	   public  ArticleListView getArticleList( int pageNumber ) {
	      
	      Connection conn = null;      
	      int currentPageNumber = pageNumber;

	      try {          
	         // DBCP   conn
	         conn = ConnectionProvider.getConnection();          
	         // DAO
	         ArticleDaoImpl articleDao = ArticleDaoImpl.getInstance();

	         // 총 방명록 수
	         int articleTotalCount = articleDao.selectCount(conn);

	         List<Article> articleList = null;

	         int firstRow = 0;
	         int endRow = 0;

	         if ( articleTotalCount > 0 ) {
	            firstRow =   (pageNumber - 1) * ARTICLE_COUNT_PER_PAGE + 1;
	            endRow   = firstRow + ARTICLE_COUNT_PER_PAGE - 1;
	            //      dao.selectList()     해당 페이지의 방명록을 select
	            articleList =         articleDao.selectList(conn, firstRow, endRow);
	         } else {
	            currentPageNumber = 0;
	            articleList = Collections.emptyList();  // 
	         }


	         return new ArticleListView(
	               articleList,
	               articleTotalCount
	               , currentPageNumber,
	               ARTICLE_COUNT_PER_PAGE
	               , firstRow, endRow);

	      } catch (SQLException | NamingException e) {
	         throw new ServiceException("> 목록 구하기 실패: " );
	      } finally {
	         try {
	            conn.close();  // 커넥션 풀 반환
	         } catch (SQLException e) {
	            e.printStackTrace();
	         }  
	      }
	      
	   } // getMessageList

	   public Article getArticle(int articleId) {
	      Connection  conn = null; 
	      try {
	         conn = ConnectionProvider.getConnection();
	         ArticleDaoImpl articleDao = ArticleDaoImpl.getInstance();      
	         
	         return  articleDao.select(conn, articleId); // DI
	         
	      } catch (Exception e) {
	         throw new ServiceException("메세지 구하기 실패 ");
	      }finally {
	         JdbcUtil.close(conn);
	      }
	   }
}
