package edu.neu.ccs.util.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * @author create by Xiao Han 11/14/18
 * @version 1.0
 * @since jdk 1.8
 */
public class Demo {

   class User {

    @Excel(name = "filed1")
    private String name;
    @Excel(name = "filed2")
    private String nameEn;
    @Excel(name = "filed3")
    private Integer age;
    @Excel(name = "filed4")
    private String six;
    @Excel(name = "filed5")
    private String weight;

    // ...getter setter
  }


  public static void main (String[] args) {
    FileInputStream fileInputStream = null;
    try {
      fileInputStream = new FileInputStream("D://data.xlsx");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    ExcelUtil<User> util = new ExcelUtil<>(User.class);
    List<User> jalanHotelList = util.importExcel("user", fileInputStream);
    // do something
  }
}
