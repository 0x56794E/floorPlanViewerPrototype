/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package partitioner;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author VyNguyen
 */
public class SpectralPartionerTest
{
    
    public SpectralPartionerTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    @Test
    public void testGetSecondSmallest() throws Exception
    {
        double[] array = {3, 2, 4, 12, 4, 3, 45};
        double expected = 3;
        double actual = SpectralPartitioner.getSecondSmallest(array);
        assertEquals(expected, actual, 0.0000001);
    }
    
    @Test
    public void testGetSecondSmallestIndex() throws Exception
    {
        double[] array = {3, 2, 4, 12, 4, 3, 45};
        int expected = 0;
        int actual = SpectralPartitioner.getSecondSmallestIndex(array);
        assertEquals(expected, actual);
    }
}
