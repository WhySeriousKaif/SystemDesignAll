interface Notifier{
    void send(String mgs);
}
class EmailNotifier implements Notifier{
    public void send(String msg){
        System.out.println("dfdkj");
    }
}

abstract class NotifierDecorator implements Notifier{
    protected Notifier notifier;

    public NotifierDecorator(Notifer no) {
        this.notifier=notifier;
    }
    public void send(String msg){
        notifier.send(mgs);
    }
    
}
class SMSDecorator exten
