public class Test {
    
    int x;
    int y;
    int z;

    public Test(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int mag()
    {
        return z;
    }

    public void calc_mag()
    {
        this.z = x*x + y*y;
    }



    public static void main(String []args)
    {
        Test mytest = new Test(3,4);
        System.out.println(mytest.mag());
    }
}
