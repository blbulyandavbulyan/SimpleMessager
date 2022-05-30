public class TestThreadExtend extends Thread{
    private int i;
    public TestThreadExtend(int i){
        this.i = i;
        start();
    }
    @Override
    public void run() {
        System.out.println("I ran! " + i);
    }
}
