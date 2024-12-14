public interface BaseSystem {
    public boolean publishJobPost(String headline, String description);
    public boolean retainEnteredDataOnNavigation(String headline, String description);
    public boolean filterExpiredJobPosts();
    public boolean searchWithFilters(String location, String role);
}