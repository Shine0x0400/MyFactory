package com.zjl.myfactory;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zjl.uikit.expandablelayout.ExpandableView;

public class ExpandableViewActivity extends AppCompatActivity {
    private ExpandableView expandableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_view);

        expandableView = findViewById(R.id.expandable);

        expandableView.setAdapter(new MyAdapter(this));
    }

    private static class MyAdapter extends ExpandableView.Adapter<MyViewHolder> {
        private Context context;

        public MyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getTotalCount() {
            return 6;
        }

        @Override
        public int getInitialCount() {
            return 3;
        }

        @Override
        public ExpandableView.ToggleHandlerViewHolder createToggleHandlerViewHolder() {
            Button button = new Button(context);
            button.setText("collapse");
            return new ExpandableView.ToggleHandlerViewHolder(button) {

                @Override
                public void onToggle(boolean isExpand) {
                    ((Button) this.mItemView).setText(isExpand ? "expand" : "collapse");
                }
            };
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(new TextView(context));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            ((TextView) holder.itemView).setText("--" + position + "--");
        }
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
