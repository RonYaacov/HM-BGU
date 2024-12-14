public interface BaseSystem {
    boolean publishJobPost(String headline, String description);
    boolean retainEnteredDataOnNavigation(String headline, String description);
    boolean filterExpiredJobPosts();
    boolean searchWithFilters(String location, String role);
}