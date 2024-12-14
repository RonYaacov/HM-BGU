class ProxySystem implements BaseSystem{


    public boolean publishJobPost(String headline, String description){
        return true;

    }
    public boolean retainEnteredDataOnNavigation(String headline, String description){
        return true;
    }
    public boolean filterExpiredJobPosts(){
        return true;
    }
    public boolean searchWithFilters(String location, String role){
        return true;
    }
}