package com.example.sm.problem3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<CustomerThread> list = new ArrayList<CustomerThread>();
        Manager manager = new Manager();

        for(int i = 0 ; i < 10 ; i++){
            Customer customer = new Customer("Customer" + i);
            CustomerThread ct = new CustomerThread(customer);
            list.add(ct);
            manager.add_customer(customer);
            ct.start();
        }


        for(CustomerThread ct : list){

            try {
                // need something here
                //모든 쓰레드가 완료될때까지 기다려야 하는데 하는방법을 모르겠음
                //모든 쓰레드가 완료되기 전에 밑에 정렬이되면 정렬이 제대로안될수 있음.

            } catch (Exception e) { }
        }

        manager.sort();

        MyBaseAdapter adapter = new MyBaseAdapter(this, manager.list);
        ListView listview = (ListView) findViewById(R.id.listView1) ;
        listview.setAdapter(adapter);


    }
}

class CustomerThread extends Thread{

    Customer customer;

    CustomerThread(Customer customer){
        this.customer = customer;
    }

    @Override
    public void run() {
        super.run();
        for(int i =0;i<10;i++){
            customer.work();
        }

    }

}

abstract class Person{

    static int money = 100000;
    int spent_money = 0;
    abstract void work();

}


class Customer extends Person{

    String name;
    Customer(String name){
        this.name = name;
    }

    @Override
    void work() {
        Random random = new Random();
        int a = Math.abs(random.nextInt()%1001);
        Log.d("test", a+"");
        spent_money+=a;
        money-=a;
    }

    // need something here
}


class Manager extends Person{
    ArrayList <Customer> list = new ArrayList<Customer>();

    void add_customer(Customer customer) {
        list.add(customer);
    }

    void sort(){ // 직접 소팅 알고리즘을 이용하여 코딩해야함. 자바 기본 정렬 메소드 이용시 감
        ArrayList<Customer> a = new ArrayList<Customer>();
        // need something here
        while (list.size()!=0) {
            Customer customer = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                if (customer.spent_money < list.get(i).spent_money) customer = list.get(i);
            }
            list.remove(customer);
            a.add(customer);
        }
        list = a;
        //효율 안좋은 정렬
    }

    @Override
    void work() {
        sort();
    }
}

// need something here

