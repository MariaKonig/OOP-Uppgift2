import java.util.ArrayList;

public class TestLibrary {
    public ArrayList<Member> getTestMemberList() {

        ArrayList<Member> membersList = new ArrayList<>();

        ArrayList<String> arrList1 = new ArrayList<>();
        ArrayList<String> arrList2 = new ArrayList<>();
        arrList1.add("7502031234");
        arrList1.add("Anna Andersson");
        arrList1.add("2023-05-03");
        arrList2.add("8505132345");
        arrList2.add("Per Persson");
        arrList2.add("2019-12-29");
        Member m1 = new Member(arrList1);
        Member m2 = new Member(arrList2);
        membersList.add(m1);
        membersList.add(m2);

        return membersList;
    }
}
