package app.parviz.com.simpleyoutubedownloader;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TestListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NestedScrollView scrollView;
    TextView help;
    TextView sort;

    int count = 0;
    int prevYPosition = 0;
    int scrollDownValue = 0;

    boolean doScroll = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);

        recyclerView = findViewById(R.id.my_recycler_view);
        scrollView = findViewById(R.id.scroll);
        help = findViewById(R.id.help);
        sort = findViewById(R.id.sort);

        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            list.add(i);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new MyAdapter(list));

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doScroll = true;
            }
        });

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e("YOYO", scrollY + "");

                int helpHeight = help.getMeasuredHeight();

                if (doScroll) {

                    if (scrollY > oldScrollY) { //down
                        int step = scrollY - prevYPosition;
//                        if (scrollDownValue + step < helpHeight) {
//                            scrollDownValue += step;
//                            sort.setTranslationY(step * (-1));
//                        } else if (scrollDownValue <= helpHeight) {
//                            int diff = helpHeight - scrollDownValue;
//                            scrollDownValue += diff;
//                            sort.setTranslationY(diff * (-1));
//                        }
                        sort.setTranslationY(count);
                        count += 10;
                    }

                    if (scrollY < oldScrollY) {// down

                    }
                }

//                if (scrollY > oldScrollY) { //down
//                        help.setVisibility(View.GONE);
//                    }
//
//                if (scrollY < oldScrollY) {// up
//                    if (scrollY <= helpHeight && help.getVisibility() == View.GONE) {
//                        help.setVisibility(View.VISIBLE);
//                    }
//                }

//                if (scrollY > oldScrollY) {
//                    Log.e("YOYO", "DOWN");
//
//                    if (scrollDownValue < helpHeight) {
//                        Log.e("YOYO", scrollY + " : " + oldScrollY);
//                        count++;
//                        sort.setTranslationY((scrollY - prevYPosition) * (-1));
//                    } else if (prevYPosition == 0) {
//                        sort.setTranslationY(helpHeight);
//                    }
//                }
//                if (scrollY < oldScrollY) {
//                    Log.e("YOYO", "UP");
//
//                    if (scrollY < helpHeight) {
//                        count = 0;
//                        sort.setTranslationY(prevYPosition - scrollY);
//                    }
//                }

//                if (scrollY >= helpHeight) { // значить help внене поле зрения
//                    // set sorting top to parent and
//                } else {
//                    //
//                }

                prevYPosition = scrollY;
            }
        });

    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        ArrayList<Integer> list;
        public MyAdapter(ArrayList<Integer> mList ) {
            this.list = mList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.title.setText("bullshit " + list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView title;

            public MyViewHolder(View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.title);
            }
        }
    }



}
