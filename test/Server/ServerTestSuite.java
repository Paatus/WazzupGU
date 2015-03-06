package Server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
   MainTest.MainAddTest.class,
   MainTest.MainDeleteTest.class,
   MainTest.MainReplaceTest.class,
   MainTest.MainFetchTest.class,
   MainTest.MainFetchCompleteTest.class,
   MockConnectionHandler.class,
   MainTest.MainFunctionTest.class
})
public class ServerTestSuite {}
