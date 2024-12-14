import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

class TestStstem{
    BaseSystem system;

    @Before
    public void setUp() {
        SystemBridge bridge = new SystemBridge();
        system = bridge.getSystem();
    }
    @Test
    public void testPublishJobPost() {
        assertTrue(system.publishJobPost("Headline", "Description"));
    }

    @Test
    public void testRetainEnteredDataOnNavigation() {
        assertTrue(system.retainEnteredDataOnNavigation("Headline", "Description"));
    }

    @Test
    public void testFilterExpiredJobPosts() {
        assertTrue(system.filterExpiredJobPosts());
    }

    @Test
    public void testSearchWithFilters() {
        assertTrue(system.searchWithFilters("New York", "Engineer"));
    }
    
}