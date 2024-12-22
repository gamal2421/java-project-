import java.util.Date;
public class transaction {
    int id_trans;
    int member_id;
    String item_id;
    boolean return1;
    boolean borrow;
    Date start_date;
    Date end_date;

    public enum TransactionType {
        RETURN1,
        borrow;
    }

    public void setId_trans(int id_trans) {
        this.id_trans = id_trans;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public void setReturn1(boolean return1) {
        this.return1 = return1;
    }

    public void setBorrow(boolean borrow) {
        this.borrow = borrow;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public int getMember_id() {
        return member_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public int getId_trans() {
        return id_trans;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public Date getStart_date() {
        return start_date;
    }
    public void displayAllDetails() {
        System.out.println("Transaction Details:");
        System.out.println("ID: " + id_trans);
        System.out.println("Member: " + member_id);
        System.out.println("Item: " + item_id);
        System.out.println("Transaction Type: " + return1 );
        System.out.println("Transaction Type: " + borrow );
        System.out.println("Start Date: " + start_date);
        System.out.println("End Date: " + end_date);
    }
}
