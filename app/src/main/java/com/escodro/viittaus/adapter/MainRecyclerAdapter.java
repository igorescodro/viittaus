package com.escodro.viittaus.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.escodro.viittaus.R;
import com.escodro.viittaus.view.CustomView;

import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * {@link android.support.v7.widget.RecyclerView.Adapter} of the Custom View list.]
 * <p/>
 * Created by IgorEscodro on 16/11/16.
 */
public class MainRecyclerAdapter extends
        RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder> {

    /**
     * {@link List} with all the {@link CustomView}.
     */
    private List<CustomView> mViewList;

    /**
     * {@link rx.subjects.PublishSubject} to notify when a item was clicked.
     */
    private final BehaviorSubject<CustomView> onClickSubject;

    /**
     * Default constructor.
     *
     * @param viewList custom view list
     */
    public MainRecyclerAdapter(List<CustomView> viewList) {
        mViewList = viewList;
        onClickSubject = BehaviorSubject.create();
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_main_list, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        final CustomView customView = mViewList.get(position);
        holder.icon.setImageResource(customView.getIconId());
        holder.title.setText(customView.getName());
        holder.description.setText(customView.getDescription());
        holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSubject.onNext(customView);
            }
        });
    }

    /**
     * Get the item click {@link Observable} to be subscribed.
     *
     * @return observable to get item cicked
     */
    public Observable<CustomView> getPositionClicks() {
        return onClickSubject.asObservable();
    }

    @Override
    public int getItemCount() {
        return mViewList.size();
    }

    /**
     * {@link android.support.v7.widget.RecyclerView.ViewHolder} with the item components.
     */
    class MainViewHolder extends RecyclerView.ViewHolder {

        /**
         * {@link RelativeLayout} containing all the components.
         */
        RelativeLayout background;

        /**
         * {@link ImageView} with the item icon.
         */
        ImageView icon;

        /**
         * {@link TextView} with the view name.
         */
        TextView title;

        /**
         * {@link TextView} with the view description.
         */
        TextView description;

        /**
         * Create a new instance of {@link MainViewHolder}.
         *
         * @param itemView view with the components.
         */
        MainViewHolder(View itemView) {
            super(itemView);
            background = (RelativeLayout) itemView
                    .findViewById(R.id.relativelayout_menu_background);
            icon = (ImageView) itemView.findViewById(R.id.imageview_menu_icon);
            title = (TextView) itemView.findViewById(R.id.textview_menu_title);
            description = (TextView) itemView.findViewById(R.id.textview_menu_description);
        }
    }
}
