package com.javagyeongmin.spring22_1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class TicketDao {

   JdbcTemplate template;
   PlatformTransactionManager transactionManager;
   
   public void setTemplate(JdbcTemplate template) {
      this.template = template;
   }

   public void setTransactionManager(PlatformTransactionManager transactionManager) {
	  this.transactionManager = transactionManager;
   }

   
public TicketDao() {
      super();
      // TODO Auto-generated constructor stub
   }
   
   public void buyTicket(final TicketDto dto) {
	   
	   TransactionDefinition definition = new DefaultTransactionDefinition();
	   TransactionStatus status = transactionManager.getTransaction(definition);
	   
	   try {
	         template.update(new PreparedStatementCreator() {
	            
	            // 카드 결제
	            @Override
	            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
	               // TODO Auto-generated method stub
	               
	               String query="INSERT INTO card (consumerid, amount) VALUES (?, ?)";
	               PreparedStatement pstmt = con.prepareStatement(query);
	               pstmt.setString(1, dto.getConsumerid());
	               pstmt.setInt(2, dto.getAmount());
	               
	               return pstmt;
	            }
	         });
	         
	         template.update(new PreparedStatementCreator() {
	            
	            // 티켓 구매
	            @Override
	            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
	               // TODO Auto-generated method stub
	               String query="INSERT INTO ticket (consumerid, countnum) VALUES (?, ?)";
	               PreparedStatement pstmt = con.prepareStatement(query);
	               pstmt.setString(1, dto.getConsumerid());
	               pstmt.setInt(2, dto.getAmount());
	               
	               return pstmt;
	            }
	         });
	         
	         	transactionManager.commit(status);
       
	         }catch(Exception e) {
	            e.printStackTrace();
	            
	            HomeController cont = new HomeController();
	            
	            cont.countError();
	            
	            System.out.println("Rollback!!!!!");
	            
	            transactionManager.rollback(status);
	         }
	      }
	  
}
  