package src;
public class SystemBridge{

    public BaseSystem getSystem(){
        return new ProxySystem();
    }
}