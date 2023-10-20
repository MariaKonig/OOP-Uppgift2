import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FileHandlerTest {
    FileHandler fH = new FileHandler();
    TestLibrary tL = new TestLibrary();
    Path testData = Paths.get("Test/TestData.txt");

    @Test
    void createMemberTest() {
        String testNameAndPnr = "7502031234, Anna Andersson";
        String testDate = "2023-05-03";
        Member testMember = fH.createMember(testNameAndPnr, testDate);

        assert testMember != null;
        assertEquals("7502031234", testMember.getIdNr());
        assertEquals("Anna Andersson".toUpperCase(), testMember.getName().toUpperCase());
        assertEquals("2023-05-03", testMember.getMembershipDate());
    }

    @Test
    void createAllMemberTest() {
        ArrayList<String[]> stringList = new ArrayList<>();
        stringList.add(new String[]{"7502031234, Anna Andersson", "2023-05-03"});
        stringList.add(new String[]{"8505132345, Per Persson", "2019-12-29"});
        ArrayList<Member> memberList = fH.createAllMembers(stringList);

        assertEquals(2, memberList.size());
        assertEquals("Anna Andersson", memberList.get(0).getName());
        assertEquals("2019-12-29", memberList.get(1).getMembershipDate());
    }

    @Test
    void readsFromFileTest() {
        ArrayList<String[]> list = fH.readFromFile(testData);

        assert (list.get(0) != null);
        assertEquals(2, list.size());
        assertEquals("7502031234, Anna Andersson", list.get(0)[0]);
        assertEquals("2019-12-29", list.get(1)[1]);
    }

    @Test
    void createFileTest(){
        String text ="2023-05-03";
        Path testFile = Paths.get("Test/newFile.txt");
        assertFalse(Files.exists(testFile));
        fH.createFile(testFile);
        assertTrue(Files.exists(testFile));

        try (BufferedReader br = Files.newBufferedReader(testFile);
             BufferedWriter bw = Files.newBufferedWriter(testFile)) {

            bw.write(text);
            bw.close();
            String textInFile = br.readLine();
            assertEquals("2023-05-03", textInFile);
            Files.delete(testFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void setWorkOutHistoryFromFileTest() {
        ArrayList<Member> list = tL.getTestMemberList();
        Path path = Paths.get("Test/setWorkoutTest.txt");
        Member testAnna = list.get(0);
        assertEquals(0, testAnna.getWorkoutList().size());
        fH.setWorkoutHistory(list, path);
        assertEquals(3,testAnna.getWorkoutList().size());
        assertEquals("2023-05-03", testAnna.getWorkoutList().get(0));
    }

    @Test
    void writeCorrectDataToFileTest() {
        Path testPTFile = Paths.get("Test/TestPTFile.txt");
        ArrayList<Member> list = tL.getTestMemberList();

        list.get(0).setWorkoutDate("2023-08-10");
        list.get(0).setWorkoutDate("2023-09-10");
        list.get(0).setWorkoutDate("2023-10-23");
        fH.writeDataToFile(list, testPTFile);
        try (BufferedReader br = Files.newBufferedReader(testPTFile)) {
            assertEquals("7502031234, Anna Andersson", br.readLine());
            assertEquals("2023-08-10, 2023-09-10, 2023-10-23", br.readLine());
            assertNull(br.readLine());
            Files.delete(testPTFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}