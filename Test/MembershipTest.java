import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MembershipTest {
    Membership m = new Membership();
    TestLibrary tl = new TestLibrary();

    @Test
    void lessThanAYearTest() {
        LocalDate compareDate = LocalDate.of(1990, 11, 29);
        LocalDate dateLessThanAYear = LocalDate.of(1991, 11, 28);
        LocalDate dateMoreThanAYear = LocalDate.of(1991, 11, 30);

        assertEquals(true, m.getIsLessThanAYear(compareDate, dateLessThanAYear));
        assertEquals(false, m.getIsLessThanAYear(compareDate, dateMoreThanAYear));
        assertEquals(true, m.getIsLessThanAYear(compareDate, compareDate));
    }

    @Test
    void findMemberInTestList() {
        ArrayList<Member> list = tl.getTestMemberList();
        Member m1 = m.findMemberInList("Anna Andersson", list);
        Member m2 = m.findMemberInList("8505132345", list);
        assertEquals("7502031234", m1.getIdNr());
        assertEquals("Per Persson", m2.getName());
    }

    @Test
    void setMembershipStatusTest() {
        ArrayList<Member> list = tl.getTestMemberList();
        Member active = list.get(0); //"2023-05-03"
        Member expired = list.get(1); //"2019-12-29"
        LocalDate testDate = LocalDate.of(2023, 7, 27);

        assertNull(active.getHasActiveMembership());
        assertNull(expired.getHasActiveMembership());

        m.setMembershipStatus(active, testDate);
        m.setMembershipStatus(expired, testDate);

        assertEquals(true, active.getHasActiveMembership());
        assertEquals(false, expired.getHasActiveMembership());
    }

    @Test
    void testSetDaysOfWorkOutToList() {
        ArrayList<Member> list = tl.getTestMemberList();
        list.get(0).setWorkoutDate(LocalDate.now().toString());
        ArrayList<String> workOutList = list.get(0).getWorkoutDates();
        assertEquals(LocalDate.now().toString(), workOutList.get(0));
    }

    @Test
    void personSearchOutputTest() {
        ArrayList<Member> list = tl.getTestMemberList();
        LocalDate testDate = LocalDate.of(2023, 7, 27);
        String search1 = "Anna Andersson";
        String search2 = "Per Persson";
        String search3 = "Pelle Snyltare";
        String search4 = "Anna";
        assertEquals("kund", m.processInput(search1, list, testDate));
        assertEquals("f.d. kund", m.processInput(search2, list, testDate));
        assertEquals("obehörig", m.processInput(search3, list, testDate));
        assertEquals("obehörig", m.processInput(search4, list, testDate));
    }

    @Test
    void inputReadsCorrectTest() throws IllegalArgumentException {
        m.test = true;
        String testInput = "anna andersson";
        String testInputWithSpace = "  7502031234  ";
        assertEquals("anna andersson", m.readInput(testInput));
        assertEquals("7502031234", m.readInput(testInputWithSpace));
    }

    @Test
    void inputIsValidTest() throws IllegalArgumentException {
        m.test = true;
        String testInputWithInvalidCharacter = "@nn@ Andersson";
        String testInputTooManyNumbers = "75020312340";
        String testInputWrongFormat = "750203-1234";
        String testValidString = "7502031234";

        assertFalse(m.isValidInput(testInputWithInvalidCharacter));
        assertFalse(m.isValidInput(testInputTooManyNumbers));
        assertFalse(m.isValidInput(testInputWrongFormat));
        assertTrue(m.isValidInput(testValidString));
    }
}