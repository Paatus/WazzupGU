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
		int ret = server.add("hello", "1234124124", "1231241241");
		assertTrue("Expected integer above 0 when adding message.", ret > 0);
	}
	
	@Test
	public void testEmptyAdd()
	{
		int ret = server.add("", "", "");
		assertTrue("Expected -1 if arguments are not well formed.",ret == -1);
 	}
	
	@Test
	public void testDelete()
	{
		int add_key = server.add("hello", "1234124124", "1231241241");
		int del_key = server.delete(add_key);
		assertTrue("Expected response from delete to be key from add.", add_key == del_key);
	}
	
	@Test
	public void testWrongDelete()
	{
		int add_key = server.add("hello", "1234124124", "1231241241");
		int del_key = server.delete(add_key-2);
		assertTrue("Expected response from delete with faulty id to be -1", del_key == -1 );
	}
	
	@Test
	public void testReplace()
	{
		int add_key = server.add("hello", "1234124124", "1231241241");
		int rep_key = server.replace(add_key, "hejsan svejsan");
		assertTrue("Expecting integer above 0 on successful replace.", rep_key > 0);
	}
	
	@Test
	public void testWrongReplace()
	{
		int add_key = server.add("hello", "1234124124", "1231241241");
		int rep_key = server.replace(add_key-2, "hejsan svejsan");
		assertTrue("Expecting -1 on replace with wrong key.", rep_key == -1);
	}

	@Test
	public void testEmptyReplace()
	{
		int add_key = server.add("hello", "1234124124", "1231241241");
		int rep_key = server.replace(add_key, "");
		assertTrue("Expecting -1 on replace with non well formed message.", rep_key == -1);
	}
	
	@Test
	public void testXMLString()
	{
		int add_key = server.add("hello", "1234124124", "1231241241");
		String xml = server.fetch("1231241241");
		assertTrue("Expecting returned string to start with xml on success.", xml.substring(0, 5).equals("<?xml"));
	}
	
	@Test
	public void testXMLStringWrong()
	{
		int add_key = server.add("hello", "1234124124", "1231241241");
		String xml = server.fetch("9999999999");
		assertFalse("Expecting returned string to not start with xml on fail.", xml.substring(0, 5).equals("<?xml"));
	}
	
	@Test
	public void testFetchComplete()
	{
		int add_key = server.add("hello", "1234124124", "1231241241");
		server.fetch("1231241241");
		int res = server.fetch_complete("1231241241");
		assertTrue("Expecting positive integer for successful fetch completion.", res > 0);
	}
	
	@Test
	public void testWrongFetchComplete()
	{
		int add_key = server.add("hello", "1234124124", "1231241241");
		server.fetch("1231241241");
		int res = server.fetch_complete("102984234");
		assertTrue("Expecting positive integer for successful fetch completion.", res < 0);
	}
}
