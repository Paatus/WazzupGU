package Server;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MainTest {

	MainServer server;
	@Before
	public void initServer()
	{
		server = new MainServer();
	}
	
	@Test
	public void testAdd()
	{
		int ret = server.add("hello", "001234124124", "001231241241");
		assertTrue("Expected integer above 0 when adding message.", ret > 0);
	}
	
	@Test
	public void testEmptyAdd()
	{
		int ret = server.add("", "", "");
		assertTrue("Expected -1 if arguments are not well formed.",ret == -1);
 	}
	
	@Test
	public void testHugeAdd()
	{
        String huge_msg = String.format("%0" + 65536 + "d", 0);
		int ret = server.add(huge_msg, "001234124124", "001231241241");
		assertFalse("Expected error indication when adding huge message.", ret > 0);
	}

	@Test
	public void testWrongNumberAdd()
	{
		String my_number = "123123123123123";
		int ret = server.add("I lolz in da lolinator!", my_number, my_number);
		assertTrue("Expecting error number from add.", ret < 0);
	}
	
	@Test
	public void testDelete()
	{
		int add_key = server.add("hello", "001234124124", "001231241241");
		int del_key = server.delete(add_key);
		assertTrue("Expected response from delete to be key from add.", add_key == del_key);
	}
	
	@Test
	public void testWrongDelete()
	{
		int add_key = server.add("hello", "001234124124", "001231241241");
		int del_key = server.delete(add_key-2);
		assertTrue("Expected response from delete with faulty id to be -1", del_key == -1 );
	}
	
	@Test
	public void testReplace()
	{
		int add_key = server.add("hello", "001234124124", "001231241241");
		int rep_key = server.replace(add_key, "hejsan svejsan");
		assertTrue("Expecting integer above 0 on successful replace.", rep_key > 0);
	}
	
	@Test
	public void testWrongReplace()
	{
		int add_key = server.add("hello", "001234124124", "001231241241");
		int rep_key = server.replace(add_key-2, "hejsan svejsan");
		assertTrue("Expecting -1 on replace with wrong key.", rep_key == -1);
	}

	@Test
	public void testEmptyReplace()
	{
		int add_key = server.add("hello", "001234124124", "001231241241");
		int rep_key = server.replace(add_key, "");
		assertTrue("Expecting -1 on replace with non well formed message.", rep_key == -1);
	}
	
	@Test
	public void testHugeReplace()
	{
        String huge_msg = String.format("%0" + 65536 + "d", 0);
		int add_key = server.add("lolz", "001234124124", "001231241241");
		int rep_key = server.replace(add_key, huge_msg);
		assertFalse("Expected error indication when adding huge message.", rep_key != -1);
	}
	
	@Test
	public void testXMLString()
	{
		String first_number = "0046735000001";
		String second_number = "0046735000002";
		String third_number = "0046735000003";
		String my_number = "0046735000004";
		server.add("hello", first_number, my_number);
		server.add("hello i m kenny", second_number, my_number);
		server.add("hello i am andreas", third_number, my_number);
		server.add("hello again", first_number, my_number);
		String xml = server.fetch(my_number);
		assertTrue("Expecting returned string to start with xml on success.", xml.substring(0, 5).equals("<?xml"));
	}
	
	@Test
	public void testXMLStringWrong()
	{
		server.add("hello", "001234124124", "001231241241");
		String xml = server.fetch("9999999999");
		assertFalse("Expecting returned string to not start with xml on fail.", xml.substring(0, 5).equals("<?xml"));
	}
	
	@Test
	public void testXMLNumberFormatWrong()
	{
		String my_number = "123123123123123";
		server.add("hello", my_number, my_number);
		String xml = server.fetch(my_number);
		assertFalse("Expecting returned string to not start with xml on fail.", xml.substring(0, 5).equals("<?xml"));
	}

	@Test
	public void testWrongNumberXML()
	{
		String my_number = "123123123123123";
		int ret = server.add("I lolz in da lolinator!", my_number, my_number);
		assertTrue("Expecting error number from add.", ret < 0);
	}
	
	@Test
	public void testFetchComplete()
	{
		server.add("hello", "001234124124", "001231241241");
		server.fetch("001231241241");
		int res = server.fetch_complete("001231241241");
		assertTrue("Expecting positive integer for successful fetch completion.", res > 0);
	}
	
	@Test
	public void testWrongFetchComplete()
	{
		server.add("hello", "001234124124", "001231241241");
		server.fetch("001231241241");
		int res = server.fetch_complete("00102984234");
		assertTrue("Expecting positive integer for successful fetch completion.", res < 0);
	}
	
	@Test
	public void testWrongNumberFetchComplete()
	{
		String my_number = "123123123123123";
		server.add("hello", my_number, my_number);
		server.fetch(my_number);
		int res = server.fetch_complete(my_number);
		assertTrue("Expecting error number from fetch completion.", res < 0);
	}
}
