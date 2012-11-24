package ubadb.core.components.bufferManager.bufferPool.replacementStrategies.MRU;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ubadb.core.components.bufferManager.bufferPool.BufferFrame;
import ubadb.core.exceptions.PageReplacementStrategyException;
import ubadb.core.testDoubles.DummyObjectFactory;
import ubadb.core.util.TestUtil;


public class MRUReplacementStrategyTest
{
	private MRUReplacementStrategy strategy;
	
	@Before
	public void setUp()
	{
		strategy = new MRUReplacementStrategy();
	}
	
	@Test(expected=PageReplacementStrategyException.class)
	public void testNoPageToReplace() throws Exception
	{
		BufferFrame frame0 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		BufferFrame frame1 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		
		frame0.pin();
		frame1.pin();
		
		strategy.findVictim(Arrays.asList(frame0,frame1));
	}
	
	@Test
	public void testOnlyOneToReplace() throws Exception
	{
		BufferFrame frame0 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		BufferFrame frame1 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		BufferFrame frame2 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		
		frame0.pin();
		frame1.pin();
		
		assertEquals(frame2,strategy.findVictim(Arrays.asList(frame0,frame1,frame2)));
	}
	
	@Test
	public void testMultiplePagesToReplace() throws Exception
	{
		BufferFrame frame0 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		BufferFrame frame1 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		BufferFrame frame2 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		
		assertEquals(frame0,strategy.findVictim(Arrays.asList(frame0,frame1,frame2)));
	}

	@Test
	public void testMultiplePagesToReplaceButOldestOnePinned() throws Exception
	{
		BufferFrame frame0 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		BufferFrame frame1 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		BufferFrame frame2 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		
		frame0.pin();
		
		assertEquals(frame1,strategy.findVictim(Arrays.asList(frame0,frame1,frame2)));
	}
	
	@Test
	public void testMultiplePagesToReplaceWithPinAndUnpin() throws Exception
	{
		BufferFrame frame0 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		frame0.pin();
		frame0.unpin();
		Thread.sleep(TestUtil.PAUSE_INTERVAL);	//Add a sleep so that frame dates are different
		BufferFrame frame1 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		frame1.pin();
		frame1.unpin();
		Thread.sleep(TestUtil.PAUSE_INTERVAL);
		BufferFrame frame2 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		frame2.pin();
		frame2.unpin();
		
		assertEquals(frame2,strategy.findVictim(Arrays.asList(frame0,frame1,frame2)));
	}

	@Test
	public void testMultiplePagesToReplaceButOldestOnePinnedWithPinAndUnpin() throws Exception
	{
		BufferFrame frame0 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		Thread.sleep(TestUtil.PAUSE_INTERVAL);	//Add a sleep so that frame dates are different
		BufferFrame frame1 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		frame1.pin();
		frame1.unpin();
		Thread.sleep(TestUtil.PAUSE_INTERVAL);
		BufferFrame frame2 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		frame2.pin();
		frame2.unpin();
		
		frame0.pin();
		
		assertEquals(frame2,strategy.findVictim(Arrays.asList(frame0,frame1,frame2)));
	}
	
	@Test
	public void testMultiplePagesToReplaceWithPinAndUnpin2() throws Exception
	{
		BufferFrame frame0 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		frame0.pin();
		frame0.unpin();
		Thread.sleep(TestUtil.PAUSE_INTERVAL);	//Add a sleep so that frame dates are different
		BufferFrame frame1 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		frame1.pin();
		frame1.unpin();
		Thread.sleep(TestUtil.PAUSE_INTERVAL);
		BufferFrame frame2 = strategy.createNewFrame(DummyObjectFactory.PAGE);
		frame2.pin();
		frame2.unpin();
		Thread.sleep(TestUtil.PAUSE_INTERVAL);
		frame1.pin();
		frame1.unpin();
		
		assertEquals(frame1,strategy.findVictim(Arrays.asList(frame0,frame1,frame2)));
	}
}
