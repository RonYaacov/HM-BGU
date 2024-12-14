class SystemBridge{

    public BaseSystem getSystem(){
        return new ProxySystem();
    }
}