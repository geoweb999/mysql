import java.sql.*;
import java.util.Scanner;


public class TestApplication {
   static final String DB_URL = "jdbc:mysql://localhost:33061/TUTORIALSPOINT";
   static final String USER = "root";
   static final String PASS = "p4ssw0rd";
   static final String SELECT_QUERY = "select student_id, first_name, last_name, GPA from Students";
   static final String GPA_QUERY = "select student_id, first_name, last_name, GPA from Students ORDER BY GPA desc";
   static final String UPDATE_QUERY = "UPDATE Students set GPA=? WHERE student_id=?";
   static final String INSERT_QUERY = "INSERT into Students (student_id, first_name, last_name, GPA) VALUES (?, ?, ?, ?)";
   static final String GET_NEXT_ID = "select max(student_id) + 1 as ID from Students";

public static int GetMenuChoice() {
   // display simple menu and return user's menu choice
   boolean valid = false;
   int choice = 0;
   Scanner scnr = new Scanner(System.in);
   System.out.println("1 Add a student.");
   System.out.println("2 Update a student.");
   System.out.println("3 List all students.");
   System.out.println("4 List all students by GPA descending.");
   System.out.println("9 Exit.");
   while (!valid) {
      System.out.print("Enter choice: ");
      choice = scnr.nextInt();
      scnr.nextLine();
      valid = (choice == 1 || choice == 2 || choice == 3 || choice == 4 || choice == 9);
   }
   return choice;
}
public static void SelectQuery(Connection conn) throws SQLException {
   // runs SELECT_QUERY and returns row with row labels
   Statement stmt = conn.createStatement();
   ResultSet rs = stmt.executeQuery(SELECT_QUERY);
   // Extract data from result set
   while (rs.next()) {
      // Retrieve by column name
      System.out.print("ID: " + rs.getInt("student_id"));
      System.out.print(", GPA: " + rs.getFloat("GPA"));
      System.out.print(", First: " + rs.getString("first_name"));
      System.out.println(", Last: " + rs.getString("last_name"));
   }
   rs.close();
}

public static void SelectQueryGPA(Connection conn) throws SQLException {
   // runs GPA_QUERY and returns rows with row labels
   Statement stmt = conn.createStatement();
   ResultSet rs = stmt.executeQuery(GPA_QUERY);
   // Extract data from result set
   while (rs.next()) {
      // Retrieve by column name
      System.out.print("ID: " + rs.getInt("student_id"));
      System.out.print(", GPA: " + rs.getFloat("GPA"));
      System.out.print(", First: " + rs.getString("first_name"));
      System.out.println(", Last: " + rs.getString("last_name"));
   }
   rs.close();
}

public static void UpdateGPA(Connection conn) throws SQLException {
   // prompts user for valid student id and new GPA value, updates via prepared statement
   Scanner scnr = new Scanner(System.in);
   System.out.print("Enter student id to update: ");
   int id = scnr.nextInt();
   System.out.print("Enter new GPA: ");
   double GPA = scnr.nextDouble();

   PreparedStatement stmt = conn.prepareStatement(UPDATE_QUERY);
      
   stmt.setDouble(1, GPA);  // This would set GPA
   stmt.setInt(2, id); // This would set ID
   int rows = stmt.executeUpdate();
   System.out.println("Rows impacted : " + rows );
}

public static void AddStudent(Connection conn) throws SQLException {
   // compute next student id (not threadsafe) and prompts user to enter new student data
   // via prepared statement
   Scanner scnr = new Scanner(System.in);
   System.out.print("Enter First Name: ");
   String first = scnr.nextLine();
   System.out.print("Enter Last Name: ");
   String last = scnr.nextLine();
   System.out.print("Enter GPA: ");
   double GPA = scnr.nextFloat();
   Statement st = conn.createStatement();
   ResultSet rs = st.executeQuery(GET_NEXT_ID);
   int id;
   if (rs.next()) {
      // Retrieve by column name
      id = rs.getInt(1);
   } else {
      id = 0;
   }
   rs.close();

   PreparedStatement stmt = conn.prepareStatement(INSERT_QUERY);
      
   stmt.setInt(1, id);  // This would set GPA
   stmt.setString(2, first); // This would set ID
   stmt.setString(3, last); // This would set ID
   stmt.setDouble(4, GPA); // This would set ID
   int rows = stmt.executeUpdate();
   System.out.println("Rows impacted : " + rows );

}


   public static void main(String[] args) {
      boolean done = false;
      int x;
      // Open a connection
      try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);) {		     
         while (!done) {
            x = GetMenuChoice();
            switch (x) {
               case 1:
                  AddStudent(conn);
                  break;
               case 2:
                  UpdateGPA(conn);
                  break;
               case 3:
                  SelectQuery(conn);
                  break;
               case 4:
                  SelectQueryGPA(conn);
                  break;
               case 9:
                  done = true;
                  break;
               default:
                  SelectQuery(conn);
                  break;
            }
         }
      } catch (SQLException e) {
         e.printStackTrace();
      } 
   }
}
