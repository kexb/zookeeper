package 协变和逆变;

import java.util.*;

class Fruit {}
class Apple extends Fruit {}
class Jonathan extends Apple {}
class Orange extends Fruit {}
class test1 {
    public static void main(String[] args) {
        //testList2();
        List<Apple> src=new ArrayList<>();
        src.add(new Apple());
        src.add(new Apple());
        List<? super Fruit> dest=Arrays.asList(new Fruit[src.size()]);
        copy(dest,src);
        System.out.println();
    }

    public static void testArray() {
        //数组是协变的，编译时数组的符号是Fruit但运行时数组的符号是Apple #1
        Fruit[] fruit = new Apple[10];
        fruit[0] = new Apple();
        fruit[1] = new Jonathan();
        try {
            //由#1 类型检测错误，实际是Apple类型
            fruit[0] = new Fruit();
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            //由#1 类型检测错误，实际是Apple类型
            fruit[0] = new Orange();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void testList() {
        //集合内建类型 不支持协变
        // 使用泛型的时候，类型信息在编译期会被类型擦除，
        // 所以泛型将这种错误检测移到了编译器。所以泛型是 不变的
        //白话(编译时类型擦除我只看等号左边不看右边)
        //List<Fruit> fruitList = new ArrayList<Apple>();
    }

    public static void testList2() {
        List<? extends Fruit> fruitList = new ArrayList<Apple>();
        //在定义了fruitList之后，编译器只知道容器中的类型是Fruit或者它的子类，
        // 但是具体什么类型却不知道，编译器不知道能不能匹配上就都不允许匹配了

        //fruitList.add(new Apple());// 编译错误

        //fruitList.add(new Jonathan());// 编译错误

        //fruitList.add(new Fruit());// 编译错误

        //fruitList.add(new Object());// 编译错误
    }

    public static void 协变取值() {
        List<? extends Fruit> fruitList = new ArrayList();

        Fruit fruit = fruitList.get(0);

        Object object = fruitList.get(0);

        Fruit fruit1 = fruitList.get(0);
        //Apple apple =  fruit1;// 编译错误 具体取出后由于类型擦除，编译器只知道是fruit或者子类，但不能确定真实类型
    }

    public static void 逆变取值() {
        List<? super Apple> appleList = new ArrayList();

//        Fruit fruit = appleList.get(0);// 编译错误 只知道是Apple祖先，但不能确定实际类型
//
//        Apple apple = appleList.get(0);// 编译错误 只知道是Apple祖先，但不能确定实际类型
//
//        Jonathan jonathan = appleList.get(0);// 编译错误 只知道是Apple祖先，但不能确定实际类型
    }

    public static void 逆变放值() {
        //apple或者apple的祖先类 能放apple、apple的子类
        List<? super Apple> appleList = new ArrayList();
        Object object = appleList.get(0);

        appleList.add(new Apple());

        appleList.add(new Jonathan());
        // 编译错误 只能放Apple以及子类
        //appleList.add(new Fruit());
        // 编译错误 只能放Apple以及子类
        //appleList.add(new Object());
    }

    public static void testList3() {
        List<? super Apple> appleList = new ArrayList();
        //编译错误
        //Fruit fruit = appleList.get(0);
        // 编译错误
        //Apple apple = appleList.get(0);
        // 编译错误
        //Jonathan jonathan = appleList.get(0);
    }


    static int COPY_THRESHOLD = 50;

    public static void copy(List<? super Fruit> dest, List<? extends Fruit> src) {
        int srcSize = src.size();
        if (srcSize > dest.size())
            throw new IndexOutOfBoundsException("Source does not fit in dest");

        if (srcSize < COPY_THRESHOLD ||
                (src instanceof RandomAccess && dest instanceof RandomAccess)) {
            for (int i = 0; i < srcSize; i++)
                dest.set(i, src.get(i));
        } else {
            ListIterator<? super Fruit> di = dest.listIterator();
            ListIterator<? extends Fruit> si = src.listIterator();
            for (int i = 0; i < srcSize; i++) {
                di.next();
                di.set(si.next());
            }
        }
    }

}


