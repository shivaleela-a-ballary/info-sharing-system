import java.sql.*;
import java.util.Scanner;

public class InfoShareMySQL {

    static final String URL = "jdbc:mysql://localhost:3306/infoshare";
    static final String USER = "root";     // your MySQL username
    static final String PASS = "root";         // your MySQL password

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        menu();
    }

    static void menu() {
        while (true) {
            System.out.println("\n--- INFORMATION SHARING SYSTEM (MYSQL VERSION) ---");
            System.out.println("1. Add Organization");
            System.out.println("2. List Organizations");
            System.out.println("3. Create Info Item");
            System.out.println("4. Share Info Item");
            System.out.println("5. View Incoming Requests");
            System.out.println("6. Accept Request");
            System.out.println("7. Show Entire Database");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1: addOrg(); break;
                case 2: listOrgs(); break;
                case 3: createInfo(); break;
                case 4: shareInfo(); break;
                case 5: viewIncoming(); break;
                case 6: acceptRequest(); break;
                case 7: showDB(); break;
                case 0: return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    // ------------------ FEATURES ---------------------
    static void addOrg() {
        System.out.print("Enter organization name: ");
        String name = sc.nextLine();

        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO organization(name) VALUES(?)");
            ps.setString(1, name);
            ps.executeUpdate();
            System.out.println("Organization added!");
        } catch (Exception e) { System.out.println(e); }
    }

    static void listOrgs() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM organization");
            System.out.println("\nOrganizations:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " + rs.getString("name"));
            }
        } catch (Exception e) { System.out.println(e); }
    }

    static void createInfo() {
        listOrgs();
        System.out.print("Enter organization ID: ");
        int orgID = Integer.parseInt(sc.nextLine());

        System.out.print("Enter title: ");
        String title = sc.nextLine();
        System.out.print("Enter content: ");
        String content = sc.nextLine();

        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO info_item(org_id, title, content) VALUES(?, ?, ?)");
            ps.setInt(1, orgID);
            ps.setString(2, title);
            ps.setString(3, content);
            ps.executeUpdate();
            System.out.println("Info item created!");
        } catch (Exception e) { System.out.println(e); }
    }

    static void shareInfo() {
        listOrgs();
        System.out.print("Enter sender org ID: ");
        int senderID = Integer.parseInt(sc.nextLine());

        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            ResultSet rs = con.createStatement()
                .executeQuery("SELECT * FROM info_item WHERE org_id=" + senderID);
            System.out.println("\nInfo Items:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " + rs.getString("title"));
            }

            System.out.print("Choose info ID: ");
            int infoID = Integer.parseInt(sc.nextLine());

            listOrgs();
            System.out.print("Enter receiver org ID: ");
            int receiverID = Integer.parseInt(sc.nextLine());

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO share_request(from_org, to_org, info_id) VALUES(?, ?, ?)");
            ps.setInt(1, senderID);
            ps.setInt(2, receiverID);
            ps.setInt(3, infoID);
            ps.executeUpdate();

            System.out.println("Share request sent!");

        } catch (Exception e) { System.out.println(e); }
    }

    static void viewIncoming() {
        listOrgs();
        System.out.print("Enter org ID: ");
        int orgID = Integer.parseInt(sc.nextLine());

        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            String sql = "SELECT share_request.id, organization.name, info_item.title " +
                    "FROM share_request " +
                    "JOIN organization ON share_request.from_org = organization.id " +
                    "JOIN info_item ON share_request.info_id = info_item.id " +
                    "WHERE to_org = ? AND status='PENDING'";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, orgID);
            ResultSet rs = ps.executeQuery();

            System.out.println("\nIncoming Requests:");
            while (rs.next()) {
                System.out.println(rs.getInt("id") +
                        ". From: " + rs.getString("name") +
                        " | Info: " + rs.getString("title"));
            }

        } catch (Exception e) { System.out.println(e); }
    }

    static void acceptRequest() {
        System.out.print("Enter request ID: ");
        int reqID = Integer.parseInt(sc.nextLine());

        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            PreparedStatement ps = con.prepareStatement(
                "UPDATE share_request SET status='ACCEPTED' WHERE id=?");
            ps.setInt(1, reqID);
            ps.executeUpdate();
            System.out.println("Request accepted!");
        } catch (Exception e) { System.out.println(e); }
    }

    // ------------------ SHOW ENTIRE DB ---------------------
    static void showDB() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            System.out.println("\n===== ORGANIZATION TABLE =====");
            ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM organization");
            while (rs1.next()) {
                System.out.println(rs1.getInt("id") + " | " + rs1.getString("name"));
            }

            System.out.println("\n===== INFO_ITEM TABLE =====");
            ResultSet rs2 = con.createStatement().executeQuery("SELECT * FROM info_item");
            while (rs2.next()) {
                System.out.println(
                        rs2.getInt("id") + " | " +
                        rs2.getInt("org_id") + " | " +
                        rs2.getString("title") + " | " +
                        rs2.getString("content"));
            }

            System.out.println("\n===== SHARE_REQUEST TABLE =====");
            ResultSet rs3 = con.createStatement().executeQuery("SELECT * FROM share_request");
            while (rs3.next()) {
                System.out.println(
                    rs3.getInt("id") + " | " +
                    rs3.getInt("from_org") + " | " +
                    rs3.getInt("to_org") + " | " +
                    rs3.getInt("info_id") + " | " +
                    rs3.getString("status"));
            }

        } catch (Exception e) {
            System.out.println("Error displaying DB: " + e.getMessage());
        }
    }
}
