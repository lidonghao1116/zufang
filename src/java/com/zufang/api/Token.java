package com.zufang.api;

import com.zufang.DAO.DAO;
import java.util.Random;

public class Token
{
  private String token;
  private int vaildTime;
  private String forUser;
  private static String characters = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjkklzxcvbnm";

  public Token(String tokenString, int time, String user) {
    this.token = tokenString;
    this.vaildTime = time;
    this.forUser = user;
  }

  public String getTokenString() {
    return this.token;
  }

  public int getVaildTime() {
    return this.vaildTime;
  }

  public String getUsername() {
    return this.forUser;
  }

  public static Token getToken(String username) {
    StringBuilder token = new StringBuilder();
    for (int i = 0; i < 15; i++) {
      int j = Math.abs(new Random().nextInt() % 51);
      token.append(characters.charAt(j));
    }
    Token myToken = new Token(token.toString(), 10, username);
    removeOldToken(username);
    addToken(myToken, username);
    return myToken;
  }

  private static boolean addToken(Token t, String username) {
    String sql = new StringBuilder().append("INSERT INTO token VALUES(DEFAULT,'").append(t.getTokenString()).append("',DATE_ADD(CURRENT_TIMESTAMP(),INTERVAL 10 DAY),0,'").append(username).append("')").toString();
    return DAO.executeRawSQL(sql);
  }

  public static boolean removeOldToken(String username) {
    String sql = new StringBuilder().append("DELETE FROM token WHERE purpose='").append(username).append("'").toString();
    return DAO.executeRawSQL(sql);
  }
}