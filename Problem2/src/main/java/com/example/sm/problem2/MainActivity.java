package com.example.sm.problem2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MyBaseAdapter adapter;
    ListView listview;

    ArrayList<Employee> emp_list;

    Employee employee;
    int empIndex;
    EditText edit_name;
    EditText edit_age;
    EditText edit_salary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // need something here

        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_age = (EditText) findViewById(R.id.edit_age);
        edit_salary = (EditText) findViewById(R.id.edit_salary);

        employee = null;
        emp_list = new ArrayList<Employee>();

        emp_list.add(new Employee("aa", 10 , 10000));
        emp_list.add(new Employee("bb", 20 , 10000));
        emp_list.add(new Employee("cc", 30 , 10000));

        adapter = new MyBaseAdapter(this, emp_list);
        listview = (ListView) findViewById(R.id.listView1) ;
        listview.setAdapter(adapter);
        listview.setOnItemClickListener((AdapterView.OnItemClickListener)adapter);

        /*
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                employee = (Employee) parent.getItemAtPosition(position);
                empIndex = position;

                edit_name.setText(employee.getName());
                edit_age.setText(employee.getAge());
                edit_salary.setText(employee.getSalary());
            }
        });*/
    }
    public void setEmployee(int pos){
        Employee e = (Employee) adapter.getItem(pos);
        employee = new Employee(e.getName(),e.getAge(),e.getSalary());
    }
    @Override
    public void onClick(View v){
        EditText edit_name = (EditText) findViewById(R.id.edit_name);
        EditText edit_age = (EditText) findViewById(R.id.edit_age);
        EditText edit_salary = (EditText) findViewById(R.id.edit_salary);

        int pos = adapter.selected_position;

        switch (v.getId()){
            case R.id.btn_inc:
                // need something here
                if(employee!=null) {;
                    employee.increase();
                    Log.d("test", ""+employee.getSalary());
                    edit_salary.setText(employee.getSalary()+"");
                }
                break;

            case R.id.btn_dec:
                // need something here
                if(employee!=null) {
                    employee.decrease();
                    edit_salary.setText(employee.getSalary()+"");

                }
                break;

            case R.id.btn_store:
                // need something here
                String name = edit_name.getText().toString();
                int age = Integer.parseInt(edit_age.getText().toString());
                int salary = Integer.parseInt(edit_salary.getText().toString());

                employee = new Employee(name, age, salary);

                adapter.add(employee);

                adapter.notifyDataSetChanged();
                employee=null;
                setEmployee(pos);
                break;

            case R.id.btn_modify:
                // need something here
                name = edit_name.getText().toString();
                age = Integer.parseInt(edit_age.getText().toString());
                salary = Integer.parseInt(edit_salary.getText().toString());

                employee.setName(name);
                employee.setAge(age);
                employee.setSalary(salary);

                adapter.delete(pos);
                emp_list.add(pos, employee);

                adapter.notifyDataSetChanged();

                break;

            case R.id.btn_delete:
                // need something here
                adapter.delete(pos);
                employee=null;
                edit_name.setText(null);
                edit_age.setText(null);
                edit_salary.setText(null);
                break;
        }
    }
}

interface Payment {
    void increase();
    void decrease();
}
